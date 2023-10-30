package wanted.n.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.config.JwtTokenProvider;
import wanted.n.dto.posting.request.PostingDetailRequestDTO;
import wanted.n.dto.posting.request.PostingExternalFeaturesRequestDTO;
import wanted.n.dto.posting.response.PostingDetailResponseDTO;
import wanted.n.dto.posting.request.PostingSearchRequestDTO;
import wanted.n.dto.posting.response.PostingSearchResponseDTO;
import wanted.n.enums.PostingType;
import wanted.n.enums.SearchType;
import wanted.n.service.PostingService;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@RequestMapping("/api/v1/postings")
@Api(tags="Posting API", description="포스팅과 관련된 API")
@RestController
public class PostingController {
    private final PostingService postingService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("")
    @ApiOperation(value="조건 검색", notes="태그 기반 조건 검색해서 Posting 목록을 가져옵니다.")
    public ResponseEntity<PostingSearchResponseDTO>  getPostingListSearchByCondition(
            @RequestHeader(AUTHORIZATION) String token,
            @RequestParam(value="hashtagname") String hashtagname
            , @Valid @RequestParam("type")PostingType type
            , @Valid @RequestParam("searchtype")SearchType searchType, @RequestParam("keyword") String searchKeyword
            , @PageableDefault(page=0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC)Pageable pageable
    ) {
        if(hashtagname==null){
            //!!warn!! getAccountFromToken 구현 필요
//            hashtagname = jwtTokenProvider.getAccountFromToken(token);
        }

        return new ResponseEntity<>(postingService.getPostingList(PostingSearchRequestDTO.builder()
                .hashTagName(hashtagname)
                .type(type)
                .searchType(searchType).searchKeyword(searchKeyword)
                .build(), pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="Posting 상세 가져오기", notes="요청시 viewCount가 증가하며 요청한 Posting을 상세로 가져옵니다.")
    public ResponseEntity<PostingDetailResponseDTO>  getPostingDetail(@RequestHeader(AUTHORIZATION) String token, @PathVariable("id") Long id) {
        PostingDetailRequestDTO postingDetailRequestDTO
                = PostingDetailRequestDTO.builder().postingId(id).build();

        return new ResponseEntity<>(postingService.upPostingViewCount(postingDetailRequestDTO), HttpStatus.OK);
    }

    @PatchMapping ("/like/{id}")
    @ApiOperation(value="Posting 좋아요", notes="외부 게시물 좋아요 클릭시 요청됩니다.")
    public ResponseEntity<Void>  likePosting(@RequestHeader(AUTHORIZATION) String token, @PathVariable("id") Long id) {
        postingService.likePosting(PostingExternalFeaturesRequestDTO.builder().postingId(id).build());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping ("/share/{id}")
    @ApiOperation(value="Posting 좋아요", notes="외부 게시물 좋아요 클릭시 요청됩니다.")
    public ResponseEntity<Void>  sharePosting(@RequestHeader(AUTHORIZATION) String token, @PathVariable("id") Long id) {
        postingService.sharePosting(PostingExternalFeaturesRequestDTO.builder().postingId(id).build());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
