package wanted.n.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import wanted.n.config.QuerydslConfig;
import wanted.n.domain.HashTag;
import wanted.n.domain.Posting;
import wanted.n.domain.PostingHashTag;
import wanted.n.dto.PostingSearchConditionDto;
import wanted.n.enums.PostingType;
import wanted.n.enums.SearchType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Import({QuerydslConfig.class})
@EnableJpaAuditing
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("PostingRepository 테스트")
public class PostingRepositoryCustomTest {

    @Autowired
    private PostingRepository postingRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private PostingHashTagRepository postingHashTagRepository;

    // createdAt,updatedAt 테스트는 기존 만들어진 데이터로 진행
    @Test
    @DisplayName("조건 검색(1)-Order By createdAt")
    public void searchByConditionTest1(){

        //Given(1) - init data(createdAt/DESC로 id 5 4 3 2 1)

        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L에는 기존 테스트용 데이터
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        long[] expect = {5, 4, 3, 2, 1};
        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getId).isEqualTo(expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(2)-Order By updatedAt")
    public void searchByConditionTest2(){

        //Given(1) - init data(createdAt/DESC로 id 1 2 3 4 5)

        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L에는 기존 테스트용 데이터
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "updatedAt");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        long[] expect = {1, 2, 3, 4, 5};
        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getId).isEqualTo(expect[i]);
        }
    }
    @Test
    @DisplayName("조건 검색(3)-Order By likeCount")
    public void searchByConditionTest3(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(hashTagList.get(0).getId()).build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "likeCount");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getTitle).isEqualTo("공부하자"+expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(4)-Order By shareCount")
    public void searchByConditionTest4(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(hashTagList.get(1).getId()).build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "shareCount");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getTitle).isEqualTo("놀러가자"+expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(5)-Order By viewCount")
    public void searchByConditionTest5(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(hashTagList.get(2).getId()).build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "viewCount");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getTitle).isEqualTo("취업하자"+expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(6)-type 조회")
    public void searchByConditionTest6(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).type(PostingType.INSTAGRAM).build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getTitle).isEqualTo("취미하자"+expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(7)-검색 제목 조회")
    public void searchByConditionTest7(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).searchType(SearchType.T).searchKeyword("취미").build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getTitle).isEqualTo("취미하자"+expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(8)-검색 내용 조회")
    public void searchByConditionTest8(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).searchType(SearchType.C).searchKeyword("쉴땐").build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getContent).isEqualTo("쉴땐쉬자"+expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(9)-검색 제목 내용 조회(1)")
    public void searchByConditionTest9(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).searchType(SearchType.TC).searchKeyword("쉴땐").build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getContent).isEqualTo("쉴땐쉬자"+expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(10)-검색 제목 내용 조회(2)")
    public void searchByConditionTest10(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).searchType(SearchType.TC).searchKeyword("테스트").build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then
        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getContent).isEqualTo("update");
        }
    }

    @Test
    @DisplayName("조건 검색(11)-page=0 size =5")
    public void searchByConditionTest11(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).build();
        final Pageable pageable = PageRequest.of(0, 5, Sort.Direction.ASC, "id");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then

        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getContent).isEqualTo("update");
        }
    }

    @Test
    @DisplayName("조건 검색(12)-page=1 size =5")
    public void searchByConditionTest12(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).build();
        final Pageable pageable = PageRequest.of(1, 5, Sort.Direction.ASC, "id");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then

        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getContent).isEqualTo("쉴땐쉬자"+expect[i]);
        }
    }

    @Test
    @DisplayName("조건 검색(13)-page=0 size =10")
    public void searchByConditionTest13(){

        //Given(1) - init data(likeCount/ASC로 id 1 2 3 4 5)
        List<HashTag> hashTagList = initData();
        //Given(2) - init search condition(hashTagId=1L, page=0, size=5, order by=createdAt/DESC)
        // hashTagId 1L=기존 테스트 데이터 인덱스0=공부 인덱스1=놀자 인덱스2=취업 인덱스3=취미
        final PostingSearchConditionDto dto = PostingSearchConditionDto.builder().hashTagId(1L).build();
        final Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        //When
        final Page<Posting> result = postingRepository.searchByCondition(dto,pageable);
        final List<Posting> resultList = result.getContent();

        //Then

        int[] expect = {1, 2, 3, 4, 5};

        for(int i=0; i<5; i++){
            assertThat(resultList.get(i)).extracting(Posting::getContent).isEqualTo("update");
        }

        for(int i=5; i<resultList.size(); i++){
            assertThat(resultList.get(i)).extracting(Posting::getContent).isEqualTo("쉴땐쉬자"+expect[i-5]);
        }
    }

    //init data 생성
    public List<HashTag> initData(){
        List<Posting> postingList = new ArrayList<Posting>();

        //Posting - FACEBOOK(id 1~5)
        for(int i=1; i<=5; i++){
            postingList.add(Posting.builder()
                    .title("공부하자"+i)
                    .content("돈벌어야행복"+i)
                    .type(PostingType.FACEBOOK)
                    .viewCount(0L)
                    .likeCount((long) i)
                    .shareCount(0L)
                    .build());
        }

        //Posting - INSTAGRAM(id 6~10)
        for(int i=1; i<=5; i++){
            postingList.add(Posting
                    .builder()
                    .title("놀러가자"+i)
                    .content("인생행복"+i)
                    .type(PostingType.INSTAGRAM)
                    .viewCount(0L)
                    .likeCount(0L)
                    .shareCount((long) i)
                    .build());
        }


        //Posting - THREADS(id 11~15)
        for(int i=1; i<=5; i++){
            postingList.add(Posting.builder()
                    .title("취업하자"+i)
                    .content("할수있다"+i)
                    .type(PostingType.THREADS)
                    .viewCount((long) i)
                    .likeCount(0L)
                    .shareCount(0L)
                    .build());
        }

        //Posting - TWITTER(id 16~20)
        for(int i=1; i<=5; i++){
            postingList.add(Posting.builder()
                    .title("취미하자"+i)
                    .content("쉴땐쉬자"+i)
                    .type(PostingType.TWITTER)
                    .viewCount(0L)
                    .likeCount(0L)
                    .shareCount(0L)
                    .build());


        }

        postingList = postingRepository.saveAll(postingList);

        //HashTag
        List<HashTag> hashTagList = new ArrayList<HashTag>();

        hashTagList.add(HashTag.builder().name("공부").build());
        hashTagList.add(HashTag.builder().name("놀자").build());
        hashTagList.add(HashTag.builder().name("취업").build());
        hashTagList.add(HashTag.builder().name("취미").build());

        hashTagList = hashTagRepository.saveAll(hashTagList);

        //Posting-HashTag 관계 매핑
        Posting posting;
        HashTag hashTag;
        for(int i=0; i<20; i++){
                posting = postingRepository.findById(postingList.get(i).getId()).get();
                hashTag = hashTagRepository.findById(hashTagList.get(i/5).getId()).get();

                if(i>=15){
                    hashTag = hashTagRepository.findById(1L).get();
                }
                postingHashTagRepository
                        .save(PostingHashTag.builder()
                                .posting(posting)
                                .hashTag(hashTag)
                                .build()
                        );


        }

        return hashTagList;
    }
}
