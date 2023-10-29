package wanted.n.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.domain.HashTag;
import wanted.n.domain.Posting;
import wanted.n.dto.posting.request.PostingDetailRequestDTO;
import wanted.n.dto.posting.request.PostingExternalFeaturesRequestDTO;
import wanted.n.dto.posting.response.PostingDetailResponseDTO;
import wanted.n.dto.posting.PostingSearchConditionDTO;
import wanted.n.dto.posting.request.PostingSearchRequestDTO;
import wanted.n.dto.posting.response.PostingSearchResponseDTO;
import wanted.n.exception.CustomException;
import wanted.n.exception.ErrorCode;
import wanted.n.repository.HashTagRepository;
import wanted.n.repository.PostingRepository;
import wanted.n.service.PostingService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostingServiceImpl implements PostingService {

    private final PostingRepository postingRepository;
    private final HashTagRepository hashTagRepository;

    @Override
    @Transactional(readOnly = true)
    public PostingSearchResponseDTO getPostingList(PostingSearchRequestDTO postingSearchRequestDTO, Pageable pageable) {
        // !WARN! hashTagId look-up 작업 필요 !WARN!
        Optional<HashTag> hashTag = hashTagRepository.findByName(postingSearchRequestDTO.getHashTagName());

        if(hashTag.isEmpty()){
            hashTag = Optional.of(
                    hashTagRepository.save(
                    HashTag.builder()
                    .name(postingSearchRequestDTO.getHashTagName())
                    .build()));
        }

        PostingSearchConditionDTO postingSearchConditionDto
                = PostingSearchConditionDTO.builder()
                .hashTagId(hashTag.get().getId())
                .type(postingSearchRequestDTO.getType())
                .searchType(postingSearchRequestDTO.getSearchType())
                .searchKeyword(postingSearchRequestDTO.getSearchKeyword())
                .build();

        PageValidCheck(pageable);

        return PostingSearchResponseDTO.builder()
                .postingList(postingRepository
                        .findPostingPageByCondition(postingSearchConditionDto, pageable)
                        .getContent())
                .build();
    }

    @Override
    @Transactional
    public PostingDetailResponseDTO upPostingViewCount(PostingDetailRequestDTO postingDetailRequestDTO) {
        Posting posting = postingRepository
                .findById(postingDetailRequestDTO.getPostingId())
                .orElseThrow(()->new CustomException(ErrorCode.POSTING_NOT_FOUND));

        return PostingDetailResponseDTO.builder()
                .posting(postingRepository.save(Posting.builder()
                                .id(posting.getId())
                                .title(posting.getTitle())
                                .content(posting.getContent())
                                .type(posting.getType())
                                .viewCount(posting.getViewCount()+1L)
                                .likeCount(posting.getLikeCount())
                                .shareCount(posting.getShareCount())
                                .build())
                        )
                .build();
    }

    @Override
    @Transactional
    public void likePosting(PostingExternalFeaturesRequestDTO postingExternalFeaturesRequestDTO) {
        // !WARN! 좋아요 시도 구현 필요
        try{

        }catch(CustomException e){
        }

        Posting posting = postingRepository.findById(postingExternalFeaturesRequestDTO.getPostingId())
                .orElseThrow(()->new CustomException(ErrorCode.POSTING_NOT_FOUND));

        postingRepository.save(Posting.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .content(posting.getContent())
                .type(posting.getType())
                .viewCount(posting.getViewCount())
                .likeCount(posting.getLikeCount()+1L)
                .shareCount(posting.getShareCount())
                .build());
    }

    @Override
    @Transactional
    public void sharePosting(PostingExternalFeaturesRequestDTO postingExternalFeaturesRequestDTO) {
        // !WARN! 공유 시도 구현 필요
        try{

        }catch(CustomException e){
        }

        Posting posting = postingRepository.findById(postingExternalFeaturesRequestDTO.getPostingId())
                .orElseThrow(()->new CustomException(ErrorCode.POSTING_NOT_FOUND));

        postingRepository.save(Posting.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .content(posting.getContent())
                .type(posting.getType())
                .viewCount(posting.getViewCount())
                .likeCount(posting.getLikeCount())
                .shareCount(posting.getShareCount()+1L)
                .build());
    }

    static void PageValidCheck(Pageable pageable){
        if(pageable.getOffset() < 0)
            throw new CustomException(ErrorCode.INVALID_PAGINATION_OFFSET);
        if(pageable.getPageSize() < 0)
            throw new CustomException(ErrorCode.INVALID_PAGINATION_SIZE);
    }
}
