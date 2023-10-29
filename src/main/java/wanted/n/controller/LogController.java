package wanted.n.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wanted.n.dto.LogPostingDTO;
import wanted.n.service.RedisService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {

    private final RedisService redisService;

    /**
     * 임시 posting 저장 api
     */
    @GetMapping("/save")
    public String savePosting(@RequestBody LogPostingDTO dto) {
        return "LogController.savePosting";
    }

}
