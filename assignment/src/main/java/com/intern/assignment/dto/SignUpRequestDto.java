package com.intern.assignment.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	private String nickname;

}
