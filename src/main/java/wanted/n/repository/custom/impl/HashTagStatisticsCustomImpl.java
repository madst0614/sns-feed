package wanted.n.repository.custom.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import wanted.n.domain.HashTagAllCountSum;
import wanted.n.repository.custom.HashTagStatisticsCustom;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static wanted.n.domain.QPosting.posting;
import static wanted.n.domain.QPostingHashTag.postingHashTag;

@RequiredArgsConstructor
public class HashTagStatisticsCustomImpl implements HashTagStatisticsCustom {
    private final JPAQueryFactory queryFactory;
    @Override
    /*
        SELECT posting_hashtag.hashtag_id, COUNT(posting.id), SUM(view_count), SUM(like_count), SUM(share_count)
        FROM posting_hashtag
                LEFT JOIN posting on posting_hashtag.posting_id = posting.id
                group by posting_hashtag.hashtag_id ;
     */
    public Optional<List<HashTagAllCountSum>> findAllCountSumByAllHashTags() {
        JPQLQuery<HashTagAllCountSum> query = queryFactory
                .select(Projections.constructor(
                        HashTagAllCountSum.class, postingHashTag.hashTag.id
                        , posting.id.count(), posting.viewCount.sum(), posting.likeCount.sum(), posting.shareCount.sum()))
                .from(postingHashTag)
                .join(postingHashTag).on(posting.eq(postingHashTag.posting)).fetchJoin()
                .groupBy(postingHashTag.hashTag.id)
                .distinct();

        return Optional.of(query.fetchAll().fetch());
    }
}
