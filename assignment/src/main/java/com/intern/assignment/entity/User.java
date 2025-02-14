package com.intern.assignment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class User {
	@Id
	private String username;
	@Column(nullable = false)
	private String password;
	
}
