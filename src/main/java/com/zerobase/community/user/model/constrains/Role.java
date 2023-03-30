package com.zerobase.community.user.model.constrains;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	ADMIN("ROLE_ADMIN", "관리자"),
	USER("ROLE_USER", "사용자"),
	GUEST("ROLE_QUEST", "게스트");
	private final String key;
	private final String title;
}
