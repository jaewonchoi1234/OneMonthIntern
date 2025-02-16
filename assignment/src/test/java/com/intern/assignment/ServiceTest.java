package com.intern.assignment;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.intern.assignment.dto.SignUpRequestDto;
import com.intern.assignment.dto.SignUpResponseDto;
import com.intern.assignment.entity.Authority;
import com.intern.assignment.entity.User;
import com.intern.assignment.repository.AuthorityRepository;
import com.intern.assignment.repository.UserRepository;
import com.intern.assignment.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(UserService.class)
public class ServiceTest {
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private AuthorityRepository authorityRepository;
	
    @MockBean
    private PasswordEncoder passwordEncoder;
    
	@Autowired
	private UserService userService;
	
	@Test
	public void signupTest() {
		//given
		String username = "TEST";
		String password = "test1234!";
		String nickname = "TEST";
		SignUpRequestDto request = new SignUpRequestDto(username,password,nickname);
		
		Set<Authority> authorities = new HashSet<>();
		authorities.add(new Authority(1L,"ROLE_USER"));
		User user = new User(1L,username,password,nickname,authorities);
		
		given(userRepository.findByUsername(any(String.class))).willReturn(Optional.ofNullable(null));
		given(authorityRepository.findByAuthorityName(any(String.class))).willReturn(Optional.ofNullable(null));
		given(authorityRepository.save(any(Authority.class))).willReturn(new Authority(1L,"USER_ROLE"));
		given(userRepository.save(any(User.class))).willReturn(user);
		
		//when
		SignUpResponseDto response =userService.signup(request);
		
		//then
		assertEquals(response.getUsername(), username);
		assertEquals(response.getNickname(), nickname);
		assertEquals(response.getAuthorities(), authorities);

		
		

		
	}
}
