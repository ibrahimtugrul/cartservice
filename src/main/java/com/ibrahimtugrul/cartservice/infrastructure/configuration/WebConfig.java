package com.ibrahimtugrul.cartservice.infrastructure.configuration;

import com.ibrahimtugrul.cartservice.infrastructure.configuration.filter.XSSFilter;
import com.ibrahimtugrul.cartservice.infrastructure.interceptor.LogExecutionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.Filter;


/**
 *
 *
 */
@Configuration
@EnableWebMvc
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final LocaleChangeInterceptor localeChangeInterceptor;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and().authorizeRequests().anyRequest().permitAll();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowCredentials(true).allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedOrigins("*");
    }

    @Bean
    public FilterRegistrationBean<Filter> xssPreventFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<Filter>();

        registrationBean.setFilter(new XSSFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor).order(0);
        registry.addInterceptor(new LogExecutionInterceptor()).order(1);
    }
}

