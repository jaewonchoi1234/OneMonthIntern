package com.intern.assignment.dto;

import java.util.List;
import java.util.Set;


import com.intern.assignment.entity.Authority;
import com.intern.assignment.entity.User;

import lombok.Getter;

@Getter
public class SignUpResponseDto {
	private String username;
	private String nickname;
	private Set<Authority> authorities;
	
	
	
	public SignUpResponseDto(User user) {
		username = user.getUsername();
		nickname = user.getNickname();
		authorities = user.getAuthorities();
	}




}
