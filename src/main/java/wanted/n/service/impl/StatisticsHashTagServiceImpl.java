package wanted.n.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wanted.n.domain.HashTag;
import wanted.n.domain.StatisticsHashTag;
import wanted.n.domain.StatisticsHashTagCountSum;
import wanted.n.domain.StatisticsHashtagSearchResult;
import wanted.n.dto.statistics.StatisticsSearchConditionDTO;
import wanted.n.dto.statistics.request.StatisticsSearchRequestDTO;
import wanted.n.dto.statistics.response.StatisticsSearchResponseDTO;
import wanted.n.enums.DateType;
import wanted.n.exception.CustomException;
import wanted.n.exception.ErrorCode;
import wanted.n.repository.HashTagRepository;
import wanted.n.repository.StatisticsRepository;
import wanted.n.service.StatisticsHashTagService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static wanted.n.domain.StatisticsHashTag.of;

@RequiredArgsConstructor
@Service
public class StatisticsHashTagServiceImpl implements StatisticsHashTagService {

    private final StatisticsRepository StatisticsRepository;
    private final HashTagRepository hashTagRepository;

    @Override
    @Transactional
    public void doStatistics(DateType dateType) {
        Optional<List<StatisticsHashTagCountSum>> list = StatisticsRepository.findAllCountSumByAllHashTags();
        List<StatisticsHashTag> saveList = new ArrayList<>();
        if(list.isPresent()){
            list.get().forEach(e -> saveList.add(of(e, DateType.HOUR)));
            StatisticsRepository.saveAll(saveList);
        }
    }

    @Override
    public StatisticsSearchResponseDTO getStatisticsHashTag(StatisticsSearchRequestDTO statisticsSearchRequestDTO) {
        HashTag hashTag = hashTagRepository.findByName
                        (statisticsSearchRequestDTO.getHashTagName())
                .orElseThrow(()->new CustomException(ErrorCode.HASHTAG_NOT_FOUND));

        List<StatisticsHashtagSearchResult> statisticsHashtagSearchResult
                = StatisticsRepository.findStatisticsHashTagAndCalcByCondition
                        (StatisticsSearchConditionDTO
                                .builder()
                                .hashTagId(hashTag.getId())
                                .datetype(statisticsSearchRequestDTO.getDateType())
                                .start(statisticsSearchRequestDTO.getStart())
                                .end(statisticsSearchRequestDTO.getEnd())
                                .countType(statisticsSearchRequestDTO.getCountType())
                                .build()
                        )
                .orElseThrow(()->new CustomException(ErrorCode.Statistics_NOT_FOUND));

        return StatisticsSearchResponseDTO
                .builder()
                .statisticsHashtagSearchResultList(statisticsHashtagSearchResult)
                .build();
    }
}
