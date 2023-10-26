package wanted.n.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wanted.n.domain.Posting;
import wanted.n.dto.PostingSearchConditionDto;

public interface PostingRepositoryCustom {
    Page<Posting> searchByCondition(PostingSearchConditionDto condition, Pageable pageable);


}
