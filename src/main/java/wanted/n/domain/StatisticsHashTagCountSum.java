package wanted.n.domain;

import lombok.*;

// 스케쥴용 HashTagCountSum
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsHashTagCountSum {
    private Long hashTagId;

    private Long postingCount;

    private Long viewCountSum;

    private Long likeCountSum;

    private Long shareCountSum;
}
