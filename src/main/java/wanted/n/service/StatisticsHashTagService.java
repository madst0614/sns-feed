package wanted.n.service;

import wanted.n.domain.StatisticsHashTag;
import wanted.n.dto.statistics.request.StatisticsSearchRequestDTO;
import wanted.n.dto.statistics.response.StatisticsSearchResponseDTO;
import wanted.n.enums.DateType;

import java.util.List;

public interface StatisticsHashTagService {
    void doStatistics(DateType dateType);

    StatisticsSearchResponseDTO getStatisticsHashTag
            (StatisticsSearchRequestDTO statisticsSearchRequestDTO);


}
