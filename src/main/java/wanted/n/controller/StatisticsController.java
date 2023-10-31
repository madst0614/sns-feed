package wanted.n.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.dto.statistics.request.StatisticsSearchRequestDTO;
import wanted.n.dto.statistics.response.StatisticsSearchResponseDTO;
import wanted.n.enums.CountType;
import wanted.n.enums.DateType;
import wanted.n.service.StatisticsHashTagService;


@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
@Api(tags="Statistics API", description="통계와 관련된 API")
@RestController
@Slf4j
public class StatisticsController {
    private final StatisticsHashTagService statisticsHashTagService;

    @GetMapping("/hashtags")
    @ApiOperation(value="해시태그 통계", notes="요청 조건 기반 검색해서 해당 통계를 가져옵니다.")
    public ResponseEntity<StatisticsSearchResponseDTO> getStatisticsHashTagByCondition(
            @RequestParam("hashtagname") String hashtagname
            , @RequestParam("datetype") DateType dateType
            , @RequestParam("start")String start
            , @RequestParam("end") String end
            , @RequestParam("counttype") CountType countType
    ){

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


}
