package com.intern.assignment.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil; 
    
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청에서 username, password 추출
            Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            String username = credentials.get("username");
            String password = credentials.get("password");

            // 스프링 시큐리티 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authenticationToken); // 인증 시도
        } catch (IOException e) {
            throw new RuntimeException("인증 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = jwtUtil.createAccessToken(authResult.getName()); // JWT 생성
        String refreshToken = jwtUtil.createRefreshToken(authResult.getName()); // JWT 생성
       
        // 인증된 사용자 정보를 SecurityContextHolder에 설정
        SecurityContextHolder.getContext().setAuthentication(authResult);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JWT를 응답 헤더 또는 JSON 바디에 담아 반환
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokenMap));
    }
}

