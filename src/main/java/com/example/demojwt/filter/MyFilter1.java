package com.example.demojwt.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("filter1111");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

//        if (req.getMethod().equals("POST")) {
//            String headerAuth = req.getHeader("Authorization");
//            System.out.println(headerAuth);
//
//            if("123".equals(headerAuth)){
//                chain.doFilter(req, res);
//            }
//            else{
//                PrintWriter out = res.getWriter();
//                out.println("인증안됨");
//            }
//
//        }

    }
}
