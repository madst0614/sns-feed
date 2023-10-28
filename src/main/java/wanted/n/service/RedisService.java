package wanted.n.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import wanted.n.dto.LogDTO;
import wanted.n.exception.CustomException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static wanted.n.exception.ErrorCode.JSON_EXCEPTION;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final static String KEY_TAG = "tag:"; // 태그를 저장하는 키
    private final static String KEY_HOT_HASHTAG = "tags"; // 핫 해시태그 리스트를 저장하는 키
    private final static Integer TIMES = 3 * 60 * 60 * 1000;

    // 객체를 JSON 형식으로 변환시켜 sorted set 저장
    public void saveLogAsJson(LogDTO log) {
        String jsonValue;
        try {
            jsonValue = objectMapper.writeValueAsString(log);
        } catch (JsonProcessingException e) {
            throw new CustomException(JSON_EXCEPTION);
        }
        long currentTimeMillis = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(KEY_TAG + log.getTag(), jsonValue, currentTimeMillis);

        // TTL 3시간으로 설정
        redisTemplate.expire(KEY_TAG + log.getTag(), TIMES, TimeUnit.SECONDS);
    }

    // key로 데이터를 조회
    public Set<String> findDataWithKey(String key) {
        return redisTemplate.keys(key);
    }

    // key에 따라 데이터 개수 반환
    public long countDataWithTime(String key) {
        Long count = redisTemplate.opsForZSet().zCard(key);
        return Objects.requireNonNullElse(count, 0L);
    }

    // 태그 리스트를 저장
    public void saveSortedTags(List<String> sortedTags, String key) {
        redisTemplate.delete(key);
        long currentTimeMillis = System.currentTimeMillis();

        List<String> tagNames = sortedTags.stream()
                .map(tag -> tag.substring(KEY_TAG.length()))
                .collect(Collectors.toList());
        for (String name : tagNames) {
            redisTemplate.opsForZSet().add(KEY_HOT_HASHTAG, name, currentTimeMillis);
        }
    }

    // 많이 사용된 순으로 태그 n개 조회
    public Set<Object> findHotTags(int n) {
        return redisTemplate.opsForZSet().range(KEY_HOT_HASHTAG, 0, n - 1);
    }

}
