package wanted.n.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.dto.HashtagsResponseDTO;
import wanted.n.dto.LogPostingDTO;
import wanted.n.scheduler.RedisScheduler;
import wanted.n.service.LogService;

@RestController
@RequiredArgsConstructor
@Api(tags = "Log API", description = "hot hashtag 관련 API")
@RequestMapping("/api/v1/logs")
public class LogController {

    private final LogService logService;
    private final RedisScheduler redisScheduler;

    /**
     * 임시 posting 저장 api
     */
    @PostMapping("/postings")
    public String savePosting(@RequestBody LogPostingDTO dto) {
        return "LogController.save";
    }

    /**
     * 임시 특정 posting 조회 api
     */
    @GetMapping("/postings/{id}")
    public String searchPosting(@PathVariable Long id) {
        return "LogController.search";
    }

    @ApiOperation(value = "hot hashtag 조회", notes = "최근 3시간 빈번하게 사용된 hashtag 리스트가 반환됩니다.")
    @GetMapping("/hashtags")
    public ResponseEntity<Object> findTagList() {
        redisScheduler.saveScheduledTag();
        HashtagsResponseDTO response = logService.getSortedTags();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
