package com.zerobase.community.user.service;

import com.zerobase.community.user.model.UserInput;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	/**
	 * 회원 가입
	 */
	boolean register(UserInput parameter);
}
