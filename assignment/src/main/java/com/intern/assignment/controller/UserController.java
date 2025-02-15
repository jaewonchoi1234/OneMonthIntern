package com.intern.assignment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.intern.assignment.dto.SignUpRequestDto;
import com.intern.assignment.dto.SignUpResponseDto;
import com.intern.assignment.service.UserService;

@RestController
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<SignUpResponseDto> signup(@Validated @RequestBody SignUpRequestDto request){
		SignUpResponseDto response = userService.signup(request);
		return ResponseEntity.ok(response);
	}
	
}
