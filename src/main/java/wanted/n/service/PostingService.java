package wanted.n.service;


import org.springframework.data.domain.Pageable;
import wanted.n.dto.posting.request.PostingDetailRequestDTO;
import wanted.n.dto.posting.request.PostingExternalFeaturesRequestDTO;
import wanted.n.dto.posting.response.PostingDetailResponseDTO;
import wanted.n.dto.posting.request.PostingSearchRequestDTO;
import wanted.n.dto.posting.response.PostingSearchResponseDTO;

public interface PostingService {
    PostingSearchResponseDTO getPostingList(
            PostingSearchRequestDTO postingSearchRequestDTO, Pageable pageable);
    PostingDetailResponseDTO getPostingDetail(PostingDetailRequestDTO postingDetailRequestDTO);

    void likePosting(PostingExternalFeaturesRequestDTO postingExternalFeaturesRequestDTO);

    void sharePosting(PostingExternalFeaturesRequestDTO postingExternalFeaturesRequestDTO);
}
