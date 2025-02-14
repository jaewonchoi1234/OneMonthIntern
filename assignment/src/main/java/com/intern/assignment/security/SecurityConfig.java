package com.intern.assignment.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private JwtUtil jwtUtil;
	
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // JWT는 CSRF 필요 없음
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/login", "/swagger-ui/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class) // 로그인 필터
	        .addFilterBefore(new JwtAuthorizationFilter(jwtUtil), JwtAuthenticationFilter.class); // JWT 인증 필터

	    return http.build();
	}
	
	
}
