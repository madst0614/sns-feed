package wanted.n.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.domain.Posting;
import wanted.n.repository.custom.PostingRepositoryCustom;

import java.util.Optional;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryCustom {
    @Override
    Optional<Posting> findById(Long postId);
}
