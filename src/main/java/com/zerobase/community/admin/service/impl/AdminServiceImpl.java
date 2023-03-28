package com.zerobase.community.admin.service.impl;

import com.zerobase.community.admin.service.AdminService;
import com.zerobase.community.post.service.PostService;
import com.zerobase.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserService userService;
	private final PostService postService;
	@Override
	public boolean deleteUser(Long userId) {
		userService.delete(userId);
		postService.deleteByUserId(userId);
		return true;
	}
}
