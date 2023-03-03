package com.zerobase.community.user.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserInput {
	private String userEmail;
	private String userPassword;
	private String userName;
	private String userBirth;

	private String adminAuthStatus;
}
