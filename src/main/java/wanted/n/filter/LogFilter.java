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
public class LogFilter implements Filter {

    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestBody = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // LogPostingDTO 객체로 변환
        for (String t : objectMapper.readValue(requestBody, LogPostingDTO.class).getTag()) {
            redisService.saveObjectAsJson(new LogDTO(t, System.currentTimeMillis()));
        }

        filterChain.doFilter(request, servletResponse);
    }
}
