package com.intern.assignment.security;
import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil; 

    // ObjectMapper를 싱글턴으로 사용하도록 수정
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        super.setFilterProcessesUrl("/sign"); // 로그인 URL 변경
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청에서 username, password 추출
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, String>>() {});
            String username = credentials.get("username");
            String password = credentials.get("password");
            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authenticationToken); // 인증 시도
        } catch (IOException e) {
            // IOException 처리 (예: 요청 본문 읽기 실패)
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String accessToken = jwtUtil.createAccessToken(authResult.getName()); // JWT 생성
        String refreshToken = jwtUtil.createRefreshToken(authResult.getName()); // JWT 생성

        // 인증된 사용자 정보를 SecurityContextHolder에 설정
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // JWT를 JSON 형식으로 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // JWT를 담을 DTO 객체 생성
        JwtTokens tokens = new JwtTokens(accessToken, refreshToken);
        response.getWriter().write(objectMapper.writeValueAsString(tokens));
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 실패 시 적절한 응답 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 인증 실패 시 401 상태 코드
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 에러 메시지 반환
        response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "Invalid username or password")));
    }
    
    // JWT 응답을 담을 DTO 클래스
    public static class JwtTokens {
        private String accessToken;
        private String refreshToken;

        public JwtTokens(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
}
