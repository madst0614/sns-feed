package wanted.n.dto.statistics.request;

import lombok.Builder;
import lombok.Getter;
import wanted.n.enums.CountType;
import wanted.n.enums.DateType;

import java.time.LocalDateTime;

@Getter
@Builder
public class StatisticsSearchRequestDTO {
    private String hashTagName;
    private DateType dateType;
    private LocalDateTime start;
    private LocalDateTime end;
    private CountType countType;
}
