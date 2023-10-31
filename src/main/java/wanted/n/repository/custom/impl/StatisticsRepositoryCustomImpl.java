package wanted.n.repository.custom.impl;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import wanted.n.domain.StatisticsHashTag;
import wanted.n.domain.StatisticsHashTagCountSum;
import wanted.n.domain.StatisticsHashtagSearchResult;
import wanted.n.dto.statistics.StatisticsSearchConditionDTO;
import wanted.n.enums.CountType;
import wanted.n.repository.custom.StatisticsRepositoryCustom;

import java.util.List;
import java.util.Optional;

import static wanted.n.domain.QPosting.posting;
import static wanted.n.domain.QPostingHashTag.postingHashTag;
import static wanted.n.domain.QStatisticsHashTag.statisticsHashTag;

@RequiredArgsConstructor
public class StatisticsRepositoryCustomImpl implements StatisticsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /*
        SELECT posting_hashtag.hashtag_id, COUNT(posting.id), SUM(view_count), SUM(like_count), SUM(share_count)
        FROM posting_hashtag
                LEFT JOIN posting on posting_hashtag.posting_id = posting.id
                group by posting_hashtag.hashtag_id ;
     */
    @Override
    public List<StatisticsHashTagCountSum> findAllCountSumByAllHashTags() {

        JPQLQuery<StatisticsHashTagCountSum> query = queryFactory
                .select(Projections.constructor(
                        StatisticsHashTagCountSum.class
                        , postingHashTag.hashTag.id
                        , posting.id.count()
                        , posting.viewCount.sum()
                        , posting.likeCount.sum()
                        , posting.shareCount.sum()))
                .from(postingHashTag)
                .join(postingHashTag).on(posting.eq(postingHashTag.posting)).fetchJoin()
                .groupBy(postingHashTag.hashTag.id)
                .distinct()
                .fetchAll();

        return query.fetch();
    }

    /*
        SELECT statistics_hashtag.hashtag_id, SUM(posting_count)
        , SUM(view_count_sum), SUM(like_count_sum), SUM(share_count_sum)
        FROM statistics_hashtag
        WHERE statistics_hashtag.hashtag_id = {hashTagId};
     */
    @Override
    public List<StatisticsHashtagSearchResult> findStatisticsHashTagAndCalcByCondition(StatisticsSearchConditionDTO dto) {
        JPQLQuery<StatisticsHashtagSearchResult> query = queryFactory
                .select(getCntTypeQBean(dto.getCountType())
                )
                .from(statisticsHashTag)
                .where(statisticsHashTag.hashTag.id.eq(dto.getHashTagId())
                        ,statisticsHashTag.dateType.eq(dto.getDatetype())
                        ,statisticsHashTag.createdAt.goe(dto.getStart())
                        ,statisticsHashTag.createdAt.loe(dto.getEnd())
                );

        return query.fetch();
    }

    public static ConstructorExpression<StatisticsHashtagSearchResult> getCntTypeQBean(CountType countType){
        ConstructorExpression<StatisticsHashtagSearchResult> qBean = null;

        switch(countType) {
            case POSTINGCOUNT:
                qBean = Projections.constructor(
                        StatisticsHashtagSearchResult.class
                        , statisticsHashTag.id
                        , Expressions.as(Expressions.constant(countType),"countType")
                        , statisticsHashTag.postingCount
                        , statisticsHashTag.createdAt
                );
                break;
            case VIEWCOUNT:
                qBean = Projections.constructor(
                        StatisticsHashtagSearchResult.class
                        , statisticsHashTag.id
                        , Expressions.as(Expressions.constant(countType),"countType")
                        , statisticsHashTag.viewCountSum
                        , statisticsHashTag.createdAt
                );
                break;
            case LIKECOUNT:
                qBean = Projections.constructor(
                        StatisticsHashtagSearchResult.class
                        , statisticsHashTag.id
                        , Expressions.as(Expressions.constant(countType),"countType")
                        , statisticsHashTag.likeCountSum
                        , statisticsHashTag.createdAt
                );
                break;
            case SHARECOUNT:
                qBean = Projections.constructor(
                        StatisticsHashtagSearchResult.class
                        , statisticsHashTag.id
                        , Expressions.as(Expressions.constant(countType),"countType")
                        , statisticsHashTag.shareCountSum
                        , statisticsHashTag.createdAt
                );
                break;
        }

        return qBean;
    }
}
