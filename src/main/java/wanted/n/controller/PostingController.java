package wanted.n.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wanted.n.config.JwtTokenProvider;
import wanted.n.domain.Posting;
import wanted.n.dto.PostingSearchRequestDto;
import wanted.n.enums.PostingType;
import wanted.n.enums.SearchType;
import wanted.n.service.PostingService;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/posting")
@Api(tags="Posting API", description="포스팅과 관련된 API")
@RestController
public class PostingController {
    private final PostingService postingService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("")
    @ApiOperation(value="조건 검색", notes="태그 기반 조건 검색해서 Posting 목록을 가져옵니다.")
    public ResponseEntity<Page<Posting>>  getPostingList(
            @RequestHeader(AUTHORIZATION) String token,
            @RequestParam(value="hashtagname") String hashtagname
            , @Valid @RequestParam("type")PostingType type
            , @Valid @RequestParam("searchtype")SearchType searchType, @RequestParam("keyword") String searchKeyword
            , @PageableDefault(page=0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC)Pageable pageable
    ) {
        if(hashtagname==null){
            //!!warn!! getAccountFromToken 구현 필요
            hashtagname = jwtTokenProvider.getAccountFromToken(token);
        }

        PostingSearchRequestDto postingSearchRequestDto = PostingSearchRequestDto.builder()
                .hashTagName(hashtagname)
                .type(type).searchType(searchType).searchKeyword(searchKeyword)
                .build();

        return new ResponseEntity<>(postingService.getPostingList(postingSearchRequestDto, pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value="Posting 상세 가져오기", notes="요청한 Posting을 상세로 가져옵니다.")
    public ResponseEntity<Posting>  getPostingDetail(@RequestHeader(AUTHORIZATION) String token, @PathVariable("id") Long id) {

        return new ResponseEntity<>(postingService.getPostingDetail(id), HttpStatus.OK);
    }

    @PutMapping ("/like/{id}")
    @ApiOperation(value="Posting 좋아요", notes="외부 게시물 좋아요 클릭시 요청됩니다.")
    public ResponseEntity<Void>  likePosting(@RequestHeader(AUTHORIZATION) String token, @PathVariable("id") Long id) {
        postingService.likePosting(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/share/{id}")
    @ApiOperation(value="Posting 좋아요", notes="외부 게시물 좋아요 클릭시 요청됩니다.")
    public ResponseEntity<Void>  sharePosting(@RequestHeader(AUTHORIZATION) String token, @PathVariable("id") Long id) {
        postingService.sharePosting(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
