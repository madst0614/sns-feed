package wanted.n.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;
import wanted.n.service.RedisService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class LogPostingFilter implements Filter {

    private final RedisService redisService;
    private final UrlPathHelper urlPathHelper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (request.getRequestURI().contains("api/v1/log/postings") && request.getMethod().equals("GET")) {
            String requestUri = urlPathHelper.getRequestUri(request);
            String pathWithinApplication = urlPathHelper.getPathWithinApplication(request);
            String idString = pathWithinApplication.substring(pathWithinApplication.lastIndexOf("/") + 1);
            long id = Long.parseLong(idString);

            // List에 조회된 posting id 저장
            redisService.saveIdToList(id);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
