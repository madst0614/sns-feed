package wanted.n.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wanted.n.domain.Posting;
import wanted.n.repository.custom.PostingRepositoryCustom;

public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryCustom {
}
