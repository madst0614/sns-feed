package wanted.n.service;

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

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static wanted.n.domain.StatisticsHashTag.from;

@RequiredArgsConstructor
@Service
public class StatisticsHashTagService{

    private final StatisticsRepository StatisticsRepository;
    private final HashTagRepository hashTagRepository;

    @Transactional
    public void doStatistics(DateType dateType) {
        List<StatisticsHashTagCountSum>list = StatisticsRepository.findAllCountSumByAllHashTags();
        List<StatisticsHashTag> saveList = new ArrayList<>();

        list.forEach(e -> saveList.add(from(e, DateType.HOUR)));
        StatisticsRepository.saveAll(saveList);

    }

    public StatisticsSearchResponseDTO getStatisticsHashTag(StatisticsSearchRequestDTO statisticsSearchRequestDTO) {

        LocalDateTime start = stringToLocalDateTime(statisticsSearchRequestDTO.getStart());
        LocalDateTime end = stringToLocalDateTime(statisticsSearchRequestDTO.getEnd());

        Duration between = Duration.between(start,end);

        if(between.isNegative())
            throw new CustomException(ErrorCode.INVALID_PERIOD);

        //IF : 요청이 == DAY라면
        if(statisticsSearchRequestDTO.getDateType()==DateType.DAY){
            if(between.toDays() > 30L)
                throw new CustomException(ErrorCode.INVALID_DURATIONWITHDAY);
        }
        //ELSE IF : 요청이 == HOUR이면
        else if(statisticsSearchRequestDTO.getDateType()==DateType.HOUR){
            if(between.toDays() > 7L)
                throw new CustomException(ErrorCode.INVALID_DURATIONWITHHOUR);
        }
        HashTag hashTag = hashTagRepository.findByName
                        (statisticsSearchRequestDTO.getHashTagName())
                .orElseThrow(()->new CustomException(ErrorCode.HASHTAG_NOT_FOUND));


        List<StatisticsHashtagSearchResult> statisticsHashtagSearchResult
                = StatisticsRepository.findStatisticsHashTagAndCalcByCondition
                        (StatisticsSearchConditionDTO
                                .builder()
                                .hashTagId(hashTag.getId())
                                .datetype(statisticsSearchRequestDTO.getDateType())
                                .start(start)
                                .end(end)
                                .countType(statisticsSearchRequestDTO.getCountType())
                                .build()
                        );

        if(statisticsHashtagSearchResult.isEmpty())
            throw(new CustomException(ErrorCode.Statistics_NOT_FOUND));

        return StatisticsSearchResponseDTO
                .builder()
                .statisticsHashtagSearchResultList(statisticsHashtagSearchResult)
                .build();
    }

    private static LocalDateTime stringToLocalDateTime(String date){
        StringTokenizer st = new StringTokenizer(date, ".-/");

        return LocalDateTime.of(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()),0,0);
    }
}
