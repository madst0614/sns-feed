package wanted.n.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wanted.n.domain.Posting;
import wanted.n.dto.posting.PostingSearchConditionDTO;

public interface PostingRepositoryCustom {
    Page<Posting> findPostingPageByCondition(PostingSearchConditionDTO condition, Pageable pageable);
}
