package wanted.n.scheduler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wanted.n.service.LogService;

import java.time.LocalDateTime;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class RedisScheduler {

    private static final Logger log = LoggerFactory.getLogger(LogService.class);
    private final LogService logService;

    @Scheduled(cron = "0 0 */1 * * *") // 매 1시간마다 실행
    public void saveScheduledTag() {
        logService.getCountByTagForLast3Hours();

        // 로그 추가
        log.info("최근 3시간 빈도높은 태그 저장 완료: " + LocalDateTime.now());
    }
}
