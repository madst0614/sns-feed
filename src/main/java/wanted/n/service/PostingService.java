package wanted.n.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wanted.n.domain.Posting;
import wanted.n.dto.PostingSearchRequestDto;

public interface PostingService {
    Page<Posting> getPostingList(PostingSearchRequestDto dto, Pageable pageable);
    Posting getPostingDetail(Long postingId);

    void likePosting(Long postingId);

    void sharePosting(Long postingId);
}
