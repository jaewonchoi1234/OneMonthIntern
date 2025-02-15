package com.intern.assignment;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import com.intern.assignment.security.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

public class JwtUtilTest {
	
	private String secretKey = "YnNJL9KBcUSQKgQgRGn6BiKkx9FIIRC5"; // 테스트 시크릿 키
	private JwtUtil jwtUtil = new JwtUtil();

	@BeforeEach
	public void init() throws Exception {
	    // secretKey 필드 값 설정
	    Field secretKeyField = JwtUtil.class.getDeclaredField("secretKey");
	    secretKeyField.setAccessible(true); // private 필드 접근 허용
	    secretKeyField.set(jwtUtil, secretKey); // 값 설정

	    // init() 메서드 호출하여 key 초기화
	    jwtUtil.init();
	}
	
	@Test
	public void createAccessTokenTest() {
		//given
		String testUsername = "test";
		//when
		String accessToken = jwtUtil.createAccessToken(testUsername);
		//then
		assertNotNull(accessToken);
	}
	@Test
	public void createRefreshTokenTest() {
		//given
		String testUsername = "test";
		//when
		String refreshToken = jwtUtil.createRefreshToken(testUsername);
		//then
		assertNotNull(refreshToken);
	}
	@Test
	public void validateTokenTest() {
		//given
		String testUsername = "test";
		String token = jwtUtil.createAccessToken(testUsername);
		//when 
		boolean validation = jwtUtil.validateToken(token);
		//then
		assertTrue(validation);
	}
	
	@Test
	public void getUsernameTest() {
		//given
		String testUsername = "test";
		String token = jwtUtil.createAccessToken(testUsername);
 		//when 
		String username = jwtUtil.getUsername(token);
		//then
		assertEquals(username, testUsername);
	}
	
	@Test
	public void isRefreshTokenTest() {
		//given
		String testUsername = "test";
		String refreshToken = jwtUtil.createRefreshToken(testUsername);
		String accessToken = jwtUtil.createAccessToken(testUsername);
		//when
		boolean isRefresh = jwtUtil.isRefreshToken(refreshToken);
		boolean isAccess = jwtUtil.isRefreshToken(accessToken);
		//then
		assertTrue(isRefresh);
		assertFalse(isAccess);
	}
	
	
}
