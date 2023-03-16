package com.zerobase.community.user.service;

import com.zerobase.community.common.model.PagingResponse;
import com.zerobase.community.user.dto.UserDto;
import com.zerobase.community.user.model.UserInput;
import com.zerobase.community.user.model.UserParam;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	/**
	 * 회원 가입
	 */
	boolean register(UserInput parameter);

	/**
	 * 회원 삭제
	 */
	boolean delete(Long userId);

	/**
	 * 회원 목록 리턴
	 */
	PagingResponse<UserDto> list(UserParam parameter);

	/**
	 * 회원 상세 정보
	 */
	UserDto getById(Long userId);

	/**
	 * 회원 페이징 처리
	 */
	String userPager(List<UserDto> list, UserParam parameter, long totalCount);
}
