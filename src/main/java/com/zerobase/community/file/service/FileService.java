package com.zerobase.community.file.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	/**
	 * 파일 저장
	 */
	public Long saveFile(MultipartFile files, Long postId) throws IOException;

}
