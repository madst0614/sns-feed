package wanted.n.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wanted.n.filter.LogPostingFilter;
import wanted.n.filter.LogTagFilter;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final LogTagFilter logTagFilter;
    private final LogPostingFilter logPostingFilter;

    @Bean
    public FilterRegistrationBean<LogTagFilter> customTagFilter() {
        FilterRegistrationBean<LogTagFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(logTagFilter);
        registrationBean.addUrlPatterns("/api/v1/log/*"); // 임시 log 처리 패턴으로 지정!
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LogPostingFilter> customPostingFilter() {
        FilterRegistrationBean<LogPostingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(logPostingFilter);
        registrationBean.addUrlPatterns("/api/v1/log/*"); // 임시 log 처리 패턴으로 지정!
        return registrationBean;
    }
}