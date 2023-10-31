package wanted.n.dto.statistics.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wanted.n.domain.StatisticsHashTag;
import wanted.n.domain.StatisticsHashTagCountSum;
import wanted.n.domain.StatisticsHashtagSearchResult;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsSearchResponseDTO {
    List<StatisticsHashtagSearchResult> statisticsHashtagSearchResultList;
}
