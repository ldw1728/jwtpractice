package com.example.demojwt.config;

import com.example.demojwt.filter.MyFilter1;
import jakarta.servlet.FilterRegistration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> filter = new FilterRegistrationBean<>(new MyFilter1());
        filter.addUrlPatterns("/*");
        filter.setOrder(0);
        return filter;
    }
}
