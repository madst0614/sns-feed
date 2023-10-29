package wanted.n.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wanted.n.dto.LogDTO;
import wanted.n.exception.CustomException;


import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static wanted.n.exception.ErrorCode.INVALID_OTP;
import static wanted.n.exception.ErrorCode.OTP_EXPIRED;
import static wanted.n.exception.ErrorCode.JSON_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> sortedSetTemplate;
    private final RedisTemplate<String, Long> listTemplate;
    private final ObjectMapper objectMapper;

    private final static String KEY_TAG = "tag:"; // 태그를 저장하는 키
    private final static String KEY_HOT_HASHTAG = "tags"; // 핫 해시태그 리스트를 저장하는 키
    private final static String KEY_OTP = "otp: ";
    private final static String KEY_POSTING = "posting:"; // 리스트를 저장하는 키
    private final static Integer HOT_TIMES = 3 * 60 * 60 * 1000; // TTL 3시간으로 설정
    private final static Integer ON_FIRE_TIMES = 12 * 60 * 60 * 1000; // TTL 12시간으로 설정

    // hot hashtag 관련

    // 객체를 JSON 형식으로 변환시켜 sorted set 저장
    public void saveLogAsJson(LogDTO log) {
        String jsonValue;
        try {
            jsonValue = objectMapper.writeValueAsString(log);
        } catch (JsonProcessingException e) {
            throw new CustomException(JSON_EXCEPTION);
        }

        String key = KEY_TAG + log.getTag();
        long currentTimeMillis = System.currentTimeMillis();

        sortedSetTemplate.opsForZSet().add(key, jsonValue, currentTimeMillis);
        sortedSetTemplate.expire(key, HOT_TIMES, TimeUnit.SECONDS);
    }

    // key로 set 데이터를 조회
    public Set<String> findSetDataWithKey(String key) {
        return sortedSetTemplate.keys(key);
    }

    // key에 따라 데이터 개수 반환
    public long countDataWithTime(String key) {
        Long count = sortedSetTemplate.opsForZSet().zCard(key);
        return Objects.requireNonNullElse(count, 0L);
    }

    // 태그 리스트를 저장
    public void saveSortedTags(List<String> sortedTags, String key) {
        sortedSetTemplate.delete(key);
        long currentTimeMillis = System.currentTimeMillis();

        List<String> tagNames = sortedTags.stream()
                .map(tag -> tag.substring(KEY_TAG.length()))
                .collect(Collectors.toList());
        for (String name : tagNames) {
            sortedSetTemplate.opsForZSet().add(KEY_HOT_HASHTAG, name, currentTimeMillis);
        }
    }

    // 많이 사용된 순으로 태그 n개 조회
    public Set<Object> findHotTags(int n) {
        return sortedSetTemplate.opsForZSet().range(KEY_HOT_HASHTAG, 0, n - 1);
    }


    // on fire 관련

    // List에 postingId log 추가
    public void saveIdToList(long postingId) {
        String key = KEY_POSTING + postingId;
        // id별 시간 저장
        listTemplate.opsForList().leftPush(key, System.currentTimeMillis());
        listTemplate.expire(key, ON_FIRE_TIMES, TimeUnit.SECONDS);
    }

    // pattern에 해당하는 key 조회
    public Set<String> findKeyWithPattern(String keyPattern) {
        return listTemplate.keys(keyPattern);
    }

    // key별 개수 반환
    public long getListSize(String key) {
        Long size = listTemplate.opsForList().size(key);
        return (size != null) ? size.intValue() : 0;
    }

    /**
     * OTP(One-Time Password) 값을 받아와 Redis에 저장하는 메서드
     * OTP는 10분 동안 유효하며, 10분이 지나면 자동으로 삭제
     *
     * @param email 사용자의 이메일 주소
     * @param otp   CompletableFuture로 비동기적으로 얻은 OTP 값
     * @throws RuntimeException CompletableFuture 결과 가져오기 실패 시 발생하는 예외
     */
    @Async
    public void saveOtp(String email, CompletableFuture<String> otp) {
        try {
            String valueFuture = otp.get();
            saveKeyAndValue(KEY_OTP + email, valueFuture, 10);
            log.info("OTP 저장 완료! OTP 생성자 : " + email);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("OTP 결과 가져오기 및 임시저장 실패", e);
        }
    }

    /**
     * 객체를 JSON 형식으로 변환시켜 redis의 sorted set에 저장합니다.
     *
     * @throws JsonProcessingException JSON 변환 예외 발생 처리
     */
    public void saveObjectAsJson(LogDTO log) throws JsonProcessingException {
        String jsonValue = objectMapper.writeValueAsString(log);
        redisTemplate.opsForZSet().add(KEY_TAG + log.getTag(), jsonValue, log.getCreatedAt());
    }

}
