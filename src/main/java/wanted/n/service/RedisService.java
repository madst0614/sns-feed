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
import wanted.n.dto.LogDTO;
import wanted.n.exception.CustomException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static wanted.n.exception.ErrorCode.JSON_EXCEPTION;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final static String KEY_TAG = "tag:"; // 태그를 저장하는 키
    private final static String KEY_HOT_HASHTAG = "tags"; // 핫 해시태그 리스트를 저장하는 키
    private final static Integer TIMES = 3 * 60 * 60 * 1000;
    private final static String KEY_OTP = "otp: ";

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

    // key에 따라 특정 시간 동안 데이터 개수 반환
    public long countDataWithTime(String key, double startTime, double lastTime) {
        return Objects.requireNonNullElse(redisTemplate.opsForZSet()
                .count(key, startTime, lastTime), 0).longValue();
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
     * Redis에 문자열 형식의 값을 저장하는 메서드.
     *
     * @param key        Redis에 저장할 키
     * @param value      Redis에 저장할 값
     * @param expireTime 키와 값의 만료 시간(분 단위)
     */
    private void saveKeyAndValue(String key, String value, int expireTime) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
    }
}
