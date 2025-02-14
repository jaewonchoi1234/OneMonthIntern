package com.intern.assignment.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.intern.assignment.repository.UserRepository;

import com.intern.assignment.entity.User;

public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findById(username).orElseThrow(()-> new IllegalArgumentException("해당 username이 없습니다."));
		return new UserDetailsImpl(user);
		
	}
    
    
}
