package com.example.demojwt.config;

import com.example.demojwt.config.auth.PrincipalDetailsService;
import com.example.demojwt.config.jwt.JwtAuthenticationFilter;
import com.example.demojwt.config.jwt.JwtAuthorizationFilter;
import com.example.demojwt.filter.MyFilter1;
import com.example.demojwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final PrincipalDetailsService principalDetailsService;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.userDetailsService(principalDetailsService);
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .sessionManagement(sessionConf -> sessionConf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilter(corsFilter)
                .formLogin(formLogin->formLogin.disable())
                .httpBasic(basic->basic.disable()) //bearer 방식을 사용하기위해
                .addFilter(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration)))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration), userRepository))
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/user/**").access(new WebExpressionAuthorizationManager("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')"))
                .requestMatchers("/api/v1/manager/**").access(new WebExpressionAuthorizationManager("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')"))
                .requestMatchers("/api/v1/admin/**").access(new WebExpressionAuthorizationManager("hasRole('ROLE_ADMIN')"))
                .anyRequest().permitAll();
        return http.build();
    }
}
