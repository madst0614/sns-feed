package wanted.n.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import wanted.n.dto.HashtagsResponse;
import wanted.n.exception.ErrorCode;

import java.util.*;
import java.util.stream.Collectors;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class LogService {
    private final RedisService redisService;

    private final static String KEY_TAG = "tag:"; // 태그를 저장하는 키
    private final static String KEY_HOT_HASHTAG = "tags"; // 핫 해시태그 리스트를 저장하는 키
    private final static Integer TIMES = 3 * 60 * 60 * 1000;
    private final static Integer MAX_TAG = 5;

    // 최근 3시간 많이 사용된 태그 순으로 리스트 저장
    public void getCountByTagForLast3Hours() {
        long threeHoursAgoTime = System.currentTimeMillis() - TIMES;
        Set<String> tags = redisService.findDataWithKey("*" + KEY_TAG + "*");

        List<String> sortedTags = countTags(tags);
        redisService.saveSortedTags(sortedTags, KEY_HOT_HASHTAG);
    }

    // 태그마다 시간 범위 내의 개수를 세어서 내림차순으로 정렬된 결과 반환
    private  List<String> countTags(Set<String> tags) {
        Map<String, Long> tagCounts = new HashMap<>();

        for (String tag : tags) {
            long count = redisService.countDataWithTime(tag);
            tagCounts.put(tag, count);
        }
        return sortTagsByCount(tagCounts);
    }

    // 내림차순으로 태그 Map 정렬
    private List<String> sortTagsByCount(Map<String, Long> tagCounts) {
        return tagCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 많이 사용된 해시태그 리스트를 조회
    public HashtagsResponse getSortedTags() {
        List<Object> hashtags = new ArrayList<>(redisService.findHotTags(MAX_TAG));
        return new HashtagsResponse(ErrorCode.HOT_HASHTAG_OK, hashtags);
    }

}
