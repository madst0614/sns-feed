package wanted.n.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import wanted.n.enums.DateType;

import javax.persistence.*;


@Entity
@Table(name="statistics_hashtag")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsHashTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTag;

    @Column(name= "date_type")
    private DateType dateType;

    @Column(name = "posting_count",nullable = false)
    @ColumnDefault("0")
    private Long postingCount;

    @Column(name = "view_count_sum",nullable = false)
    @ColumnDefault("0")
    private Long viewCountSum;

    @Column(name = "like_count_sum",nullable = false)
    @ColumnDefault("0")
    private Long likeCountSum;

    @Column(name = "share_count_sum",nullable = false)
    @ColumnDefault("0")
    private Long shareCountSum;

    public static StatisticsHashTag from(StatisticsHashTagCountSum statisticsHashTagCountSum, DateType dateType){
        return StatisticsHashTag.builder()
                .hashTag(HashTag.builder().id(statisticsHashTagCountSum.getHashTagId()).build())
                .dateType(dateType)
                .postingCount(statisticsHashTagCountSum.getPostingCount())
                .viewCountSum(statisticsHashTagCountSum.getViewCountSum())
                .likeCountSum(statisticsHashTagCountSum.getLikeCountSum())
                .shareCountSum(statisticsHashTagCountSum.getShareCountSum())
                .build();
    }
}
