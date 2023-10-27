package wanted.n.repository.custom;

import java.util.Optional;

public interface HashTagRepositoryCustom {
    Optional<Long> findIdByName(String name);
}
