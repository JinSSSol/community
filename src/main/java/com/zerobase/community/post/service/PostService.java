package com.zerobase.community.post.service;

import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.model.PostInput;
import com.zerobase.community.post.model.PostParam;
import java.util.List;

public interface PostService {

	/**
	 * 게시글 등록
	 */
	Long add(PostInput parameter);

	/**
	 * 게시글 목록 리턴
	 */
	List<PostDto> list(PostParam parameter);

	/**
	 * 전체 게시글 목록
	 */
	List<PostDto> listAll();

	/**
	 * 게시글 상세 정보
	 */
	PostDto getById(long postId);
}
