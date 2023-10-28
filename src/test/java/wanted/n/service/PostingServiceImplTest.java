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
import wanted.n.dto.posting.PostingSearchConditionDTO;
import wanted.n.dto.posting.request.PostingDetailRequestDTO;
import wanted.n.dto.posting.request.PostingExternalFeaturesRequestDTO;
import wanted.n.dto.posting.request.PostingSearchRequestDTO;
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
import static org.mockito.Mockito.*;

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
        final PostingSearchRequestDTO postingSearchRequestDto = PostingSearchRequestDTO.builder()
                .hashTagName("test")
                .type(PostingType.INSTAGRAM)
                .searchType(SearchType.T)
                .searchKeyword("키워드")
                .build();
        final PostingSearchConditionDTO postingSearchConditionDto = PostingSearchConditionDTO.of(1L, postingSearchRequestDto);
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        List<Posting> expect = new ArrayList<>();
        expect.add(Posting.builder().id(1L).type(PostingType.INSTAGRAM).title("키워드").build());

         when(hashTagRepository.findByName(any())).thenReturn(Optional.of(HashTag.builder().id(1L).name("test").build()));
         when(postingRepository.findPostingPageByCondition(any(), any())).thenReturn(new PageImpl<>(expect));

         //When
        List<Posting> result = postingServiceImpl.getPostingList(postingSearchRequestDto, pageable).getPostingList();

        //Then
        assertThat(result).isEqualTo(expect);
    }

    @Test
    @DisplayName("GetPostingDetail 테스트")
    public void getPostingDetailTest(){
        //Given
        final PostingDetailRequestDTO postingDetailRequestDTO = PostingDetailRequestDTO.builder()
                .postingId(1L)
                .build();

        Posting expect = Posting.builder().id(1L).title("테스트").type(PostingType.INSTAGRAM).viewCount(0L).build();

        when(postingRepository.findById(1L)).thenReturn( Optional.of(Posting.builder().id(1L).title("테스트").type(PostingType.INSTAGRAM).viewCount(1L).build()));
        when(postingRepository.save(any())).thenReturn(Posting.builder().id(1L).title("테스트").type(PostingType.INSTAGRAM).viewCount(1L).build());


        //When
        Posting result = postingServiceImpl.getPostingDetail(postingDetailRequestDTO).getPosting();

        //Then
        assertThat(result.getId()).isEqualTo(expect.getId());
        assertThat(result.getTitle()).isEqualTo(expect.getTitle());
        assertThat(result.getContent()).isEqualTo(expect.getContent());
        assertThat(result.getType()).isEqualTo(expect.getType());
        assertThat(result.getPostingHashTagList()).isEqualTo(expect.getPostingHashTagList());
        assertThat(result.getViewCount()).isEqualTo(expect.getViewCount()+1L);
        assertThat(result.getLikeCount()).isEqualTo(expect.getLikeCount());
        assertThat(result.getShareCount()).isEqualTo(expect.getShareCount());
    }

    @Test
    @DisplayName("likePosting 테스트")
    public void likePostingTest(){
        //Given
        final PostingExternalFeaturesRequestDTO postingExternalFeaturesRequestDTO =
                PostingExternalFeaturesRequestDTO.builder().postingId(1L).build();

        when(postingRepository.findById(1L)).thenReturn( Optional.of(Posting.builder().id(1L).title("테스트").type(PostingType.INSTAGRAM).likeCount(1L).build()));

        //When
        postingServiceImpl.likePosting(postingExternalFeaturesRequestDTO);

        //Then
        verify(postingRepository, times(1))
                .save(argThat(posting ->
                        posting.getId().equals(postingExternalFeaturesRequestDTO.getPostingId())
                ));
    }


    @Test
    @DisplayName("sharePosting 테스트")
    public void sharePostingTest(){
        //Given
        final PostingExternalFeaturesRequestDTO postingExternalFeaturesRequestDTO =
                PostingExternalFeaturesRequestDTO.builder().postingId(1L).build();

        when(postingRepository.findById(1L)).thenReturn( Optional.of(Posting.builder().id(1L).title("테스트").type(PostingType.INSTAGRAM).shareCount(1L).build()));

        //When
        postingServiceImpl.sharePosting(postingExternalFeaturesRequestDTO);

        //Then
        verify(postingRepository, times(1))
                .save(argThat(posting ->
                        posting.getId().equals(postingExternalFeaturesRequestDTO.getPostingId())
                ));
    }
}
