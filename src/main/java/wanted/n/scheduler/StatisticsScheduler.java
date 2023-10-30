package wanted.n.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wanted.n.enums.DateType;
import wanted.n.service.StatisticsHashTagService;

import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatisticsScheduler {
    private final StatisticsHashTagService statisticsHashTagService;

    @Transactional
    @Scheduled(cron = "0 0 1-23/1 * * *")
    public void doStatisticsPerOneHour(){
        log.info("1H통계 작업 수행");
        statisticsHashTagService.doStatistics(DateType.HOUR);

    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void doStatisticsPerDay(){
        log.info("1D통계 작업 수행");
        statisticsHashTagService.doStatistics(DateType.DAY);

    }
}
