package com.zerobase.community.file.service;

import com.zerobase.community.file.dto.FileDto;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	/**
	 * 파일 저장
	 */
	Long saveFile(MultipartFile files, Long postId) throws IOException;

	/**
	 * 파일 삭제
	 */
	boolean delete(Long fileId);

	/**
	 * 해당 게시글의 모든 파일 삭제
	 */
	boolean deleteByPostId(Long postId);

	/**
	 * 파일 상세 정보
	 */
	FileDto getById(Long fileId);

	/**
	 * 해당 게시글 파일 정보 가져오기
	 */
	List<FileDto> getByPostId(Long postId);

}
