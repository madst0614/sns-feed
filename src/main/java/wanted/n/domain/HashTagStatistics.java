package wanted.n.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import wanted.n.domain.pk.HashTagStatisticsPK;

import javax.persistence.*;


@Entity
@Table(name="hashtag_statistics")
@IdClass(HashTagStatisticsPK.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashTagStatistics extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private HashTag hashTagId;

    @Column(name = "view_count_sum",nullable = false)
    @ColumnDefault("0")
    private Long viewCountSum;

    @Column(name = "like_count_sum",nullable = false)
    @ColumnDefault("0")
    private Long likeCountSum;

    @Column(name = "share_count_sum",nullable = false)
    @ColumnDefault("0")
    private Long shareCountSum;
}
