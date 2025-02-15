package com.intern.assignment.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	private String nickname;

}
