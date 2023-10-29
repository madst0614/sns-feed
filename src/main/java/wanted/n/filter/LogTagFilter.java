package wanted.n.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wanted.n.dto.LogDTO;
import wanted.n.dto.LogPostingDTO;
import wanted.n.service.RedisService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class LogTagFilter implements Filter {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (request.getRequestURI().contains("/api/v1/logs/postings") && request.getMethod().equals("POST")) {
            String requestBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // LogPostingDTO 객체로 변환해서 sorted set에 저장
            for (Long tag : objectMapper.readValue(requestBody, LogPostingDTO.class).getTag()) {
                redisService.saveLogAsJson(new LogDTO(tag.toString(), System.currentTimeMillis()));
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
