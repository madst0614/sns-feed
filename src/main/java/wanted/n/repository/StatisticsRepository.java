package wanted.n.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.domain.StatisticsHashTag;
import wanted.n.repository.custom.StatisticsRepositoryCustom;

@Repository
public interface StatisticsRepository extends JpaRepository<StatisticsHashTag, Long>, StatisticsRepositoryCustom {
}
