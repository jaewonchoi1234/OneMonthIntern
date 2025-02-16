package com.intern.assignment.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intern.assignment.dto.SignUpRequestDto;
import com.intern.assignment.dto.SignUpResponseDto;
import com.intern.assignment.entity.Authority;
import com.intern.assignment.entity.User;
import com.intern.assignment.repository.AuthorityRepository;
import com.intern.assignment.repository.UserRepository;

@Service
public class UserService {
		
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthorityRepository authorityRepository;

	public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
	
	@Transactional
	public SignUpResponseDto signup(SignUpRequestDto request) {
        // 1. 중복 유저 확인
		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 유저입니다.");
        }
		// 2. 기본 권한 설정 (ROLE_USER)
        Authority userAuthority = authorityRepository.findByAuthorityName("ROLE_USER")
                .orElseGet(() -> {
                    Authority newAuthority = new Authority("ROLE_USER");
                    return authorityRepository.save(newAuthority);
                });
        
        Set<Authority> authorities = new HashSet<>();
        authorities.add(userAuthority);
        
		User user = new User(request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setAuthorities(authorities);
		return new SignUpResponseDto(userRepository.save(user));
	}

}
