package wanted.n.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wanted.n.filter.LogFilter;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    private final LogFilter logFilter;

    @Bean
    public FilterRegistrationBean<LogFilter> customFilter() {
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(logFilter);
        registrationBean.addUrlPatterns("/log/*"); // 임시 log 처리 패턴으로 지정
        return registrationBean;
    }
}