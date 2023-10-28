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

import static wanted.n.exception.ErrorCode.JSON_EXCEPTION;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static wanted.n.exception.ErrorCode.INVALID_OTP;
import static wanted.n.exception.ErrorCode.OTP_EXPIRED;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedisTemplate<String, Object> sortedSetTemplate;
    private final RedisTemplate<String, Long> listTemplate;
    private final ObjectMapper objectMapper;

    private final static String KEY_TAG = "tag:"; // 태그를 저장하는 키
    private final static String KEY_HOT_HASHTAG = "tags"; // 핫 해시태그 리스트를 저장하는 키
    private final static String KEY_POSTING = "posting:"; // 리스트를 저장하는 키
    private final static String KEY_OTP = "otp: ";
    private final static String KEY_TOKEN = "token: ";
  
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
     * Redis에 문자열 형식의 값을 저장하는 메서드.
     *
     * @param key        Redis에 저장할 키
     * @param value      Redis에 저장할 값
     * @param expireTime 키와 값의 만료 시간(분 단위)
     */
    private void saveKeyAndValue(String key, String value, int expireTime) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value);
        stringRedisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
    }
 
    /**
     * OTP(One-Time Password) 값을 받아와 Redis에 저장하는 메서드
     * OTP는 10분 동안 유효하며, 10분이 지나면 자동으로 삭제
     *
     * @param account 사용자의 계정
     * @param otp   CompletableFuture로 비동기적으로 얻은 OTP 값
     * @throws RuntimeException CompletableFuture 결과 가져오기 실패 시 발생하는 예외
     */
    @Async
    public void saveOtp(String account, CompletableFuture<String> otp) {
        try {
            String valueFuture = otp.get();
            saveKeyAndValue(KEY_OTP + account, valueFuture, 10);
            log.info("OTP 저장 완료! OTP 생성자 : " + account);

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("OTP 결과 가져오기 및 임시저장 실패", e);
        }
    }

    /**
     * Redis에 저장된 OTP 값과 사용자가 입력한 OTP 값을 비교하는 메서드
     * 일치할 경우에는 OTP 를 삭제
     *
     * @param account
     * @param otp
     */
    @Transactional(readOnly = true)
    public void otpVerification(String account, String otp) {
        String key = KEY_OTP + account;

        // Redis에 해당 이메일을 키로 한 OTP 정보가 존재하지 않으면 OTP가 만료되었음을 의미
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            throw new CustomException(OTP_EXPIRED);
        }

        String storedOtp = stringRedisTemplate.opsForValue().get(key);

        // 입력한 OTP가 저장된 OTP와 일치하지 않을 경우 예외 발생
        if (!otp.equals(storedOtp)) {
            throw new CustomException(INVALID_OTP);
        }
    }

    /**
     * 사용자 이메일과 리프레시 토큰을 저장하는 메서드입니다.
     *
     * @param account       사용자 계정
     * @param refreshToken 리프레시 토큰
     */
    @Transactional
    public void saveRefreshToken(String account, String refreshToken) {
        // 이메일을 기반으로 한 식별키를 생성합니다.
        String key = KEY_TOKEN + account;

        // 생성된 식별키와 리프레시 토큰을 저장하며, 토큰의 유효 기간은 1440분(24시간)으로 설정합니다.
        saveKeyAndValue(key, refreshToken, 1440);
    }

    /**
     * 로그아웃 시 사용자 리프레시토큰을 삭제하는 메서드입니다.
     *
     * @param account       사용자 계정
     */
    @Transactional
    public void deleteRefreshToken(String account) {
        String key = KEY_TOKEN + account;
        stringRedisTemplate.delete(key);
    }

    /**
     * Redis에 저장된 OTP 값과 사용자가 입력한 OTP 값을 비교하는 메서드
     * 일치할 경우에는 OTP 를 삭제
     *
     * @param email
     * @param otp
     */
    @Transactional(readOnly = true)
    public void otpVerification(String email, String otp) {
        String key = KEY_OTP + email;

        // Redis에 해당 이메일을 키로 한 OTP 정보가 존재하지 않으면 OTP가 만료되었음을 의미
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(key))) {
            throw new CustomException(OTP_EXPIRED);
        }

        String storedOtp = stringRedisTemplate.opsForValue().get(key);

        // 입력한 OTP가 저장된 OTP와 일치하지 않을 경우 예외 발생
        if (!otp.equals(storedOtp)) {
            throw new CustomException(INVALID_OTP);
        }
    }

    @Transactional
    public void saveRefreshToken(String email, String refreshToken) {
        String key = KEY_TOKEN + email;
        saveKeyAndValue(key, refreshToken, 1440);
    }
}
