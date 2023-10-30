package wanted.n.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wanted.n.service.RedisService;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (request.getRequestURI().equals("/api/v1/postings")) { // tag 검색
            String name = request.getParameter("hashtagname");
            if (name != null) {
                redisService.saveTag(name);
            }
        } else if (request.getRequestURI().matches("/api/v1/postings/\\d+")) { // 상세 조회
            String requestURI = request.getRequestURI();
            String[] pathSegments = requestURI.split("/");

            if (pathSegments.length >= 5) {
                redisService.savePostingToList(Long.parseLong(pathSegments[5]));
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
