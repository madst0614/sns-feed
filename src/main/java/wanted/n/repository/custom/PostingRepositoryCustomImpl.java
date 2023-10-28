package wanted.n.repository.custom;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wanted.n.domain.Posting;
import wanted.n.dto.PostingSearchConditionDTO;
import wanted.n.enums.PostingType;

import java.util.ArrayList;
import java.util.List;

import static wanted.n.domain.QPosting.posting;
import static wanted.n.domain.QPostingHashTag.postingHashTag;

@RequiredArgsConstructor
public class PostingRepositoryCustomImpl implements PostingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Posting> findPostingPageByCondition(PostingSearchConditionDTO dto, Pageable pageable){

        // (베이스 쿼리) hashtag, type 조건
        JPQLQuery<Posting> query = queryFactory
                .select(Projections.fields(Posting.class, posting.id, posting.title,
                        new CaseBuilder()
                                .when(posting.content.length().gt(20))
                                .then(posting.content.substring(0,20))
                                .otherwise(posting.content)
                                .as("content"),
                        posting.type, posting.likeCount, posting.shareCount, posting.viewCount, posting.createdAt, posting.updatedAt))
                .from(postingHashTag)
                .leftJoin(postingHashTag.posting, posting)
                .where(postingHashTag.hashTag.id.eq(dto.getHashTagId()),eqType(dto.getType())).distinct();

        // (쿼리 1차 가공) searchType != null 일때
        if(dto.getSearchType()!=null){
            switch(dto.getSearchType()){
                case T : query = query.where(posting.title.contains(dto.getSearchKeyword()));
                case C : query = query.where(posting.content.contains(dto.getSearchKeyword()));

                default : query = query.where(posting.title.contains(dto.getSearchKeyword())
                        .or(posting.content.contains(dto.getSearchKeyword())));
            };
        }

        // Pageable에서 order 조건 추출
        List<OrderSpecifier> order = new ArrayList<>();
        pageable.getSort().stream().forEach(o -> {
            order.add( new OrderSpecifier((o.getDirection().isAscending() ? Order.ASC : Order.DESC),
                    new PathBuilder(Posting.class,"posting").get(o.getProperty())));
        });

        // (쿼리 2차 가공) order 조건과 Pagination
        query = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.stream().toArray((OrderSpecifier[]::new)))
                ;

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }

    // type이 null 일때 where절 처리
    private BooleanExpression eqType(PostingType type) {
        return type != null ? posting.type.eq(type) : null;
    }

}


