package wanted.n.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import wanted.n.domain.HashTag;
import wanted.n.domain.Posting;
import wanted.n.dto.PostingSearchConditionDto;
import wanted.n.dto.PostingSearchRequestDto;
import wanted.n.enums.PostingType;
import wanted.n.enums.SearchType;
import wanted.n.repository.HashTagRepository;
import wanted.n.repository.PostingRepository;
import wanted.n.service.impl.PostingServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostingService 테스트")
public class PostingServiceImplTest {

    @Mock
    private PostingRepository postingRepository;
    @Mock
    private HashTagRepository hashTagRepository;


    @InjectMocks
    private PostingServiceImpl postingServiceImpl;
    @BeforeEach
    public void setup(){
        postingServiceImpl = new PostingServiceImpl();
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("GetPostingList 테스트")
    public void getPostingListTest(){
        //Given
        final PostingSearchRequestDto postingSearchRequestDto = PostingSearchRequestDto.builder()
                .hashTagName("test")
                .type(PostingType.INSTAGRAM)
                .searchType(SearchType.T)
                .searchKeyword("키워드")
                .build();
        final PostingSearchConditionDto postingSearchConditionDto = PostingSearchConditionDto.of(1L, postingSearchRequestDto);
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        List<Posting> expectPostingList = new ArrayList<>();
        expectPostingList.add(Posting.builder().id(1L).type(PostingType.INSTAGRAM).title("키워드").build());
        Page<Posting> expect = new PageImpl<>(expectPostingList);

         when(hashTagRepository.findByName(any())).thenReturn(Optional.of(HashTag.builder().id(1L).name("test").build()));
         when(postingRepository.findPostingPageByCondition(any(), any())).thenReturn(expect);

         //When
        Page<Posting> result = postingServiceImpl.getPostingList(postingSearchRequestDto, pageable);

        //Then
        assertThat(result).isEqualTo(expect);
    }
}
