package wanted.n.repository.custom;

import wanted.n.domain.StatisticsHashTag;
import wanted.n.domain.StatisticsHashTagCountSum;
import wanted.n.domain.StatisticsHashtagSearchResult;
import wanted.n.dto.statistics.StatisticsSearchConditionDTO;
import wanted.n.enums.DateType;

import java.util.List;
import java.util.Optional;

public interface StatisticsRepositoryCustom {

    // 스케쥴링 통계 기능 : TABLE posting, posting_hashtag 조인을 통해 모든 해시태그 당 모든 sum(count)을 가져옴
    public Optional<List<StatisticsHashTagCountSum>> findAllCountSumByAllHashTags();

    // 통계 기능 : 해시태그 ID를 통해 TABLE hashtag_statistics에서 조건 기반으로 가져옴.
    public Optional<List<StatisticsHashtagSearchResult>> findStatisticsHashTagAndCalcByCondition(StatisticsSearchConditionDTO statisticsSearchConditionDTO);
}
