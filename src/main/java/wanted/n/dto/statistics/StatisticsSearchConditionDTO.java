package wanted.n.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.enums.CountType;
import wanted.n.enums.DateType;

import java.time.LocalDateTime;

@Getter
@Builder
public class StatisticsSearchConditionDTO {
    // id
    private Long hashTagId;

    // 시간 타입 date or hour
    private DateType datetype;

    // start ~ end 시간
    private LocalDateTime start;
    private LocalDateTime end;

    // count, view_count, like_count, share_count
    private CountType countType;
}
