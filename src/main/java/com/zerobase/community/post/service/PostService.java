package com.zerobase.community.post.service;

import com.zerobase.community.common.model.PagingResponse;
import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.model.PostInput;
import com.zerobase.community.post.model.PostParam;
import java.io.IOException;
import java.util.List;

public interface PostService {

	/**
	 * 게시글 등록
	 */
	boolean add(PostInput parameter) throws IOException;

	/**
	 * 게시글 목록 리턴
	 */
	PagingResponse<PostDto> list(PostParam parameter);

	/**
	 * 게시글 상세 정보
	 */
	PostDto getById(long postId);

	/**
	 * 내 게시글 목록
	 */
	PagingResponse<PostDto> myPost(Long userId);

	/**
	 * 내 게시글 목록 By Email
	 */
	PagingResponse<PostDto> myPostByEmail(String userEmail);

	/**
	 * 게시글 페이징 처리
	 */
	String postPager(List<PostDto> list, PostParam parameter, long totalCount);

	/**
	 * 게시글 삭제
	 */
	boolean delete(Long postId);

	/**
	 * 선택된 게시글 삭제
	 */
	boolean deleteAll(List<Long> postIds);

	/**
	 * 게시글 수정
	 */
	boolean update(PostInput parameter) throws IOException;

	/**
	 * 해당 회원의 모든 게시글 삭제
	 */
	boolean deleteByUserId(Long userId);

}
