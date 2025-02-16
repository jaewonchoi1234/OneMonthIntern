package com.intern.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intern.assignment.controller.UserController;
import com.intern.assignment.dto.SignUpRequestDto;
import com.intern.assignment.dto.SignUpResponseDto;
import com.intern.assignment.entity.Authority;
import com.intern.assignment.entity.User;
import com.intern.assignment.service.UserService;
import static org.mockito.BDDMockito.given;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class) 
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;
   
	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void signupTest() throws Exception{
		//given
		String username = "TEST";
		String password = "test1234!";
		String nickname = "TEST";
		SignUpRequestDto request = new SignUpRequestDto(username,password,nickname);

		Set<Authority> authorities = new HashSet<>();
		authorities.add(new Authority("ROLE_USER"));
		User user = new User(1L,username,password,nickname,authorities);
		SignUpResponseDto response = new SignUpResponseDto(user);

		given(userService.signup(any(SignUpRequestDto.class))).willReturn(response);
		
		//when
		ResultActions resultActions = mockMvc.perform(post("/signup")
	               .contentType(MediaType.APPLICATION_JSON)
	               .content(objectMapper.writeValueAsString(request)));
		
		//then
		resultActions.andExpect(status().isOk())
				   	 .andExpect(jsonPath("$.username").value("TEST"))
				   	 .andExpect(jsonPath("$.nickname").value("TEST"))
				     .andExpect(jsonPath("$.authorities[0].authorityName").value("ROLE_USER"));
	}
	
}
