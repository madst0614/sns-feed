package wanted.n.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import wanted.n.dto.LogDTO;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private final static String KEY_TAG = "tag:";
    private final static Integer TIMES = 3 * 60 * 60 * 1000;

    /**
     * 객체를 JSON 형식으로 변환시켜 3시간 동안 redis의 sorted set에 저장합니다.
     *
     * @throws JsonProcessingException JSON 변환 예외 발생 처리
     */
    public void saveObjectAsJson(LogDTO log) throws JsonProcessingException {
        String jsonValue = objectMapper.writeValueAsString(log);
        long currentTimeMillis = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(KEY_TAG + log.getTag(), jsonValue, currentTimeMillis);

        // TTL 3시간으로 설정
        redisTemplate.expire(KEY_TAG + log.getTag(), TIMES, TimeUnit.SECONDS);
    }

}
