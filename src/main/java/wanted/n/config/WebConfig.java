package wanted.n.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wanted.n.filter.LogTagFilter;

import javax.servlet.DispatcherType;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final LogTagFilter logTagFilter;

    @Bean
    public FilterRegistrationBean<LogTagFilter> customTagFilter() {
        FilterRegistrationBean<LogTagFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(logTagFilter);
        registrationBean.addUrlPatterns("/api/v1/logs/postings/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST); // GET 메소드
        return registrationBean;
    }
}