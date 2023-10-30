package wanted.n.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.domain.HashTagStatistics;
import wanted.n.domain.pk.HashTagStatisticsPK;
import wanted.n.repository.custom.HashTagStatisticsCustom;

@Repository
public interface HashTagStatisticsRepository extends JpaRepository<HashTagStatistics, HashTagStatisticsPK>, HashTagStatisticsCustom {
}
