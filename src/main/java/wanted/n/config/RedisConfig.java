package wanted.n.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableRedisRepositories
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Long> listTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // Key 직렬화 설정
        template.setKeySerializer(new StringRedisSerializer());

        // Value 직렬화 설정
        RedisSerializer<Long> valueSerializer = new GenericToStringSerializer<>(Long.class);
        template.setValueSerializer(valueSerializer);
        return template;
    }

    @Bean
    public RedisTemplate<String, String> listStringTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // Key 직렬화 설정
        template.setKeySerializer(new StringRedisSerializer());

        // Value 직렬화 설정
        RedisSerializer<String> valueSerializer = new GenericToStringSerializer<>(String.class);
        template.setValueSerializer(valueSerializer);
        return template;
    }
}
