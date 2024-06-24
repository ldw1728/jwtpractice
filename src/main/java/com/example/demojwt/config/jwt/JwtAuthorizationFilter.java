package com.example.demojwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demojwt.config.auth.PrincipalDetails;
import com.example.demojwt.model.User;
import com.example.demojwt.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

// BasicAuthenticationFilter - 권힌이나 인증이 필요한 특정주소를 요청했을 때 거치게되는 필터
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;


    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        //super.doFilterInternal(request, response, chain);

        System.out.println("인증이나 권한이 필요");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("header == " + jwtHeader);

        //jwt토큰 검증
        if (!StringUtils.hasText(jwtHeader) || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        //jwt token 추출
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        String username = JWT.require(Algorithm.HMAC512("ldw")).build().verify(jwtToken).getClaim("username").asString();

        if (StringUtils.hasText(username)) {
            System.out.println(username);
            User user = userRepository.findByUsername(username);
            System.out.println("findByUsername : " + user);

            PrincipalDetails principalDetails = new PrincipalDetails(user);

            // 토큰서명이 존재하고 정상이면서 유저네임에의한 유저가 존재시 인증 및 권한 패스
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            //시큐리티 세션에 강제로 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);

        }

    }
}
