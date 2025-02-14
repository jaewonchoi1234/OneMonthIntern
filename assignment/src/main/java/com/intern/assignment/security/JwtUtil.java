package com.intern.assignment.security;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
	
	private final int REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000; // 2주
	private final int ACCESS_TOKEN_TIME = 60 * 60 * 1000; // 60분
	
	@Value("${jwt.secret}")
	private String secretKey;

	private Key key;

	@PostConstruct
	public void init() {
		key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
	}
	//AcessToken 생성
	public String createAccessToken(String username) {
		return Jwts.builder()
                .setSubject(username) // 사용자 정보 
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME)) // 만료 시간
                .signWith(key) // 서명(Signature)
                .compact(); // 최종적으로 JWT 문자열 반환
	}
	
	//RefreshToken 생성
	public String createRefreshToken(String username) {
		return Jwts.builder()
                .setSubject(username) // 사용자 정보
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME)) // 만료 시간
                .signWith(key) // 서명(Signature)
                .compact(); // 최종적으로 JWT 문자열 반환
	}
	
	//Token 검증
    public boolean validateToken(String token) {
        try {
        	
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key) // 서명 검증을 위한 키 설정
                    .build()
                    .parseClaimsJws(token); // 토큰 파싱

            return !claims.getBody().getExpiration().before(new Date()); // 만료 시간 확인
        } catch (Exception e) {
            return false; // 유효하지 않은 토큰
        }
    }
	
	//토큰에서 username 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); 
    }
    

    // 토큰이 RefreshToken인지 AccessToken인지 확인
    public boolean isRefreshToken(String token) {
    	Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date issuedAt = claims.getIssuedAt();  // 발급 시간
        Date expiration = claims.getExpiration();  // 만료 시간
        long duration = expiration.getTime() - issuedAt.getTime();
        return duration > ACCESS_TOKEN_TIME;
    }
    
    
    
}
