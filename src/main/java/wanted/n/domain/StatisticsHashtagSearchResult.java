package wanted.n.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.enums.CountType;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsHashtagSearchResult {
    private Long id;

    private CountType countType;

    private Long count;

    @JsonFormat(pattern = "yyyy-MM-dd HH")
    private LocalDateTime time;
}
