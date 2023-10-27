package wanted.n.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.domain.HashTag;
import wanted.n.repository.custom.HashTagRepositoryCustom;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long>, HashTagRepositoryCustom {

}
