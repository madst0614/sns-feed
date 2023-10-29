package wanted.n.repository.custom;

import wanted.n.domain.HashTag;

import java.util.Optional;

public interface HashTagRepositoryCustom {
    Optional<HashTag> findByName(String name);
}
