package com.example.demojwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demojwt.config.auth.PrincipalDetails;
import com.example.demojwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // login요청 시 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // username, password 받아와서 authenticationManager로 로그인 시도(principalDetailsService의 loadUserByUsername함수를 호출)
        // 권한 관리를 위해 principalDetails 객체는 세션에 저장.
        // jwt토큰 생하여 응답

        try{

            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 로그인이 성공하면 authentication이 생성된다.
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());

            // return 하면 session영역에 저장됨
            // jwt 사용시 굳이 세션이 필요없지만 권한관리를 security에서 사용하기위해
            return authentication;
        }catch (IOException e){
            e.printStackTrace();
        }

       System.out.println("로그인 시도중");
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("인증이 완료됨");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // jwt 토큰 생성
        String jwt = JWT.create().withSubject(principalDetails.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (60000*10))) // 만료시간 대략 10분에서 30분사이가 적당할듯
                        .withClaim("id", principalDetails.getUser().getId()) //jwt의 payload부분
                        .withClaim("username", principalDetails.getUser().getUsername())//jwt의 payload부분
                        .sign(Algorithm.HMAC512("ldw")); // 시크릿값은 임의로 정해준다.

        response.addHeader("Authorization", "Bearer " + jwt);

    }
}
