package wanted.n.repository.custom;

import wanted.n.domain.HashTagAllCountSum;

import java.util.List;
import java.util.Optional;

public interface HashTagStatisticsCustom {
    public Optional<List<HashTagAllCountSum>> findAllCountSumByAllHashTags();
}
