package com.zerobase.community.user.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserInput {

	@NotBlank(message = "이메일은 필수 값입니다.")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 아닙니다.")
	private String userEmail;
	@NotBlank(message = "비밀번호는 필수 값입니다.")
	private String userPassword;

	@NotBlank(message = "이름은 필수 값입니다.")
	private String userName;
	private String userBirth;

	private String adminAuthStatus;
}
