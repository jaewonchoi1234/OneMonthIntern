package com.intern.assignment.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.intern.assignment.entity.User;


public class UserDetailsImpl implements UserDetails {
	
	private final User user;
	
	public UserDetailsImpl(User user) {
		this.user = user;
	}
	
    @Override
    public String getPassword() {
        return user.getPassword(); // 사용자 비밀번호
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // 사용자 아이디
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금되지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화됨
    }
}
