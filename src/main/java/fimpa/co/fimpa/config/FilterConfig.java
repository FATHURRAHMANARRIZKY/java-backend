package fimpa.co.fimpa.config;

import fimpa.co.fimpa.filter.JwtAuthenticationFilter;
import fimpa.co.fimpa.filter.LogoutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private LogoutFilter logoutFilter;

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LogoutFilter> loggingFilter() {
        FilterRegistrationBean<LogoutFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(logoutFilter);
        registrationBean.addUrlPatterns("/logout");
        return registrationBean;
    }
}