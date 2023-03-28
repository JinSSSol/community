package com.zerobase.community.user.dto;

import com.zerobase.community.user.entity.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {


	private Long userId;
	private String userEmail;
	private String userPassword;
	private String userName;
	private LocalDate userBirth;
	private LocalDate createAt;

	private boolean adminYn;

	// 페이징
	long totalCount;
	long seq;

	public static UserDto of(User user) {
		return UserDto.builder()
				.userName(user.getUserName())
				.userId(user.getUserId())
				.userPassword(user.getUserPassword())
				.userEmail(user.getUserEmail())
				.userBirth(user.getUserBirth())
				.createAt(user.getCreateAt())
				.build();
	}

}
