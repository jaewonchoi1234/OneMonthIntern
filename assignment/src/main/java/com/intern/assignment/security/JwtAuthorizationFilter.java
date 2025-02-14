package com.intern.assignment.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
    
	private final JwtUtil jwtUtil; 
    
    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            
            if (jwtUtil.validateToken(token)) {
            	
            	if(jwtUtil.isRefreshToken(token)) {
            		// refreshToken이면 accessToken 발급
            		String accessToken = jwtUtil.createAccessToken(username);
            		// AccessToken을 응답 헤더 또는 JSON 바디에 담아 반환
            		response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    Map<String, String> tokenMap = new HashMap<>();
                    tokenMap.put("accessToken", accessToken);
            		response.getWriter().write(new ObjectMapper().writeValueAsString(tokenMap));
            	}
            	
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
