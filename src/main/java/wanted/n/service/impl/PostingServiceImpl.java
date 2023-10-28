package wanted.n.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wanted.n.domain.HashTag;
import wanted.n.domain.Posting;
import wanted.n.dto.PostingSearchConditionDto;
import wanted.n.dto.PostingSearchRequestDto;
import wanted.n.exception.CustomException;
import wanted.n.exception.ErrorCode;
import wanted.n.repository.HashTagRepository;
import wanted.n.repository.PostingRepository;
import wanted.n.service.PostingService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostingServiceImpl implements PostingService {

    PostingRepository postingRepository;
    HashTagRepository hashTagRepository;

    @Override
    public Page<Posting> getPostingList(PostingSearchRequestDto dto, Pageable pageable) {
        // !WARN! hashTagId look-up 작업 필요 !WARN!
        Optional<HashTag> hashTag = hashTagRepository.findByName(dto.getHashTagName());
        if(hashTag.isEmpty()){
            hashTag = Optional.of(hashTagRepository.save(HashTag.builder().name(dto.getHashTagName()).build()));
        }

        PostingSearchConditionDto postingSearchConditionDto
                = PostingSearchConditionDto.builder()
                .hashTagId(hashTag.get().getId())
                .type(dto.getType())
                .searchType(dto.getSearchType())
                .searchKeyword(dto.getSearchKeyword())
                .build();

        PageValidCheck(pageable);

        return postingRepository.findPostingPageByCondition(postingSearchConditionDto, pageable);
    }

    @Override
    public Posting getPostingDetail(Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(()->new CustomException(ErrorCode.POSTING_NOT_FOUND));

        return postingRepository.save(Posting.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .content(posting.getContent())
                .type(posting.getType())
                .viewCount(posting.getViewCount()+1)
                .likeCount(posting.getLikeCount())
                .shareCount(posting.getShareCount())
                .build());
    }

    @Override
    public void likePosting(Long postingId) {
        // !WARN! 좋아요 시도 구현 필요
        try{

        }catch(CustomException e){
        }

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(()->new CustomException(ErrorCode.POSTING_NOT_FOUND));

        postingRepository.save(Posting.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .content(posting.getContent())
                .type(posting.getType())
                .viewCount(posting.getViewCount())
                .likeCount(posting.getLikeCount()+1)
                .shareCount(posting.getShareCount())
                .build());
    }

    @Override
    public void sharePosting(Long postingId) {
        // !WARN! 공유 시도 구현 필요
        try{

        }catch(CustomException e){
        }

        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(()->new CustomException(ErrorCode.POSTING_NOT_FOUND));

        postingRepository.save(Posting.builder()
                .id(posting.getId())
                .title(posting.getTitle())
                .content(posting.getContent())
                .type(posting.getType())
                .viewCount(posting.getViewCount())
                .likeCount(posting.getLikeCount())
                .shareCount(posting.getShareCount()+1)
                .build());
    }

    static void PageValidCheck(Pageable pageable){
        if(pageable.getOffset() < 0)
            throw new CustomException(ErrorCode.INVALID_PAGINATION_OFFSET);
        if(pageable.getPageSize() < 0)
            throw new CustomException(ErrorCode.INVALID_PAGINATION_SIZE);
    }
}
