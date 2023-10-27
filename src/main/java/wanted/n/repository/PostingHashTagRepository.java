package wanted.n.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wanted.n.domain.PostingHashTag;
import wanted.n.domain.pk.PostingHashTagPK;

@Repository
public interface PostingHashTagRepository extends JpaRepository<PostingHashTag, PostingHashTagPK> {
}
