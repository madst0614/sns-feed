package wanted.n.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import wanted.n.config.QuerydslConfig;
import wanted.n.domain.HashTagAllCountSum;
import wanted.n.repository.custom.impl.HashTagStatisticsCustomImpl;

import java.util.List;
import java.util.Optional;

@Import({QuerydslConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("HashTagStatisticsCustm 테스트")
@Slf4j
public class HashTagStatisticsCustomTest {
    @Autowired
    private HashTagStatisticsRepository hashTagStatisticsRepository;

    @Test
    @DisplayName("findAllCountSumByAllHashTags 테스트")

    public void findAllCountSumByAllHashTags(){
        Optional<List<HashTagAllCountSum>> list = hashTagStatisticsRepository.findAllCountSumByAllHashTags();

        if(list.isEmpty()){
            log.info("빔!");
            return;
        }


        for(HashTagAllCountSum hs : list.get()){
            log.info("id: "+hs.getHashTagId().toString()+" 게시물 수 "+hs.getCountSum()+"  뷰 "+hs.getViewCountSum().toString()+" 좋아요 "+hs.getLikeCountSum()+" 공유 "+hs.getShareCountSum());
        }
    }
}
