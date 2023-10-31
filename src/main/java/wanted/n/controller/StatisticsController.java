package wanted.n.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.config.JwtTokenProvider;
import wanted.n.dto.statistics.request.StatisticsSearchRequestDTO;
import wanted.n.dto.statistics.response.StatisticsSearchResponseDTO;
import wanted.n.enums.CountType;
import wanted.n.enums.DateType;
import wanted.n.exception.CustomException;
import wanted.n.exception.ErrorCode;
import wanted.n.service.StatisticsHashTagService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.StringTokenizer;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@Api(tags="Statistics API", description="통계와 관련된 API")
@RestController
public class StatisticsController {
    private final StatisticsHashTagService statisticsHashTagService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/hashtags")
    @ApiOperation(value="해시태그 통계", notes="요청 조건 기반 검색해서 해당 통계를 가져옵니다.")
    public ResponseEntity<StatisticsSearchResponseDTO> getStatisticsHashTagByCondition(
            @RequestHeader(AUTHORIZATION) String token
            , @RequestParam("hashtagname") String hashtagname
            , @RequestParam("datetype") DateType dateType
            , @RequestParam("start")String startTime
            , @RequestParam("end") String endTime
            , @RequestParam("counttype") CountType countType
    ){

        LocalDateTime start = stringToLocalDateTime(startTime);
        LocalDateTime end = stringToLocalDateTime(endTime);

        Duration between = Duration.between(start,end);

        if(between.isNegative())
            throw new CustomException(ErrorCode.INVALID_PERIOD);

        if(dateType==DateType.DAY){
            if(between.toDays() > 30L)
                throw new CustomException(ErrorCode.INVALID_DURATIONWITHDAY);
        }else if(dateType==DateType.HOUR){
            if(between.toDays() > 7L)
                throw new CustomException(ErrorCode.INVALID_DURATIONWITHHOUR);
        }

        if(hashtagname==null){
            hashtagname = jwtTokenProvider.getAccountFromToken(token);
        }

        return new ResponseEntity<>(statisticsHashTagService.getStatisticsHashTag(
                StatisticsSearchRequestDTO
                        .builder()
                        .hashTagName(hashtagname)
                        .dateType(dateType)
                        .start(start)
                        .end(end)
                        .countType(countType)
                        .build()
        ), HttpStatus.OK);
    }

    private static LocalDateTime stringToLocalDateTime(String date){
        StringTokenizer st = new StringTokenizer(date, ".-/");

        return LocalDateTime.of(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),Integer.parseInt(st.nextToken()),0,0);
    }
}
