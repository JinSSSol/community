package com.zerobase.community.file.service.impl;

import com.zerobase.community.file.dto.FileDto;
import com.zerobase.community.file.entity.File;
import com.zerobase.community.file.repository.FileRepository;
import com.zerobase.community.file.service.FileService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

	@Value("${file.dir}")
	private String fileDir;

	private final FileRepository fileRepository;

	@Override
	public Long saveFile(MultipartFile file, Long postId) throws IOException {
		if (file.isEmpty()) {
			return null;
		}

		String originName = file.getOriginalFilename();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String extension = originName.substring(originName.lastIndexOf("."));
		String savedName = uuid + extension;

		// 파일을 불러올 때 사용할 파일 경로
		String savedPath = fileDir + savedName;

		File fileEntity = File.builder()
			.originName(originName)
			.savedName(savedName)
			.savedPath(savedPath)
			.postId(postId)
			.build();

		// 실제로 로컬에 uuid를 파일명으로 저장
		java.io.File localFile = new java.io.File(savedPath);
		file.transferTo(localFile);

		File savedFile = fileRepository.save(fileEntity);

		return savedFile.getFileId();
	}

	@Override
	public boolean delete(Long fileId) {

		FileDto fileDto = this.getById(fileId);
		boolean fileDeleted = new java.io.File(fileDto.getSavedPath()).delete();
		if (!fileDeleted) {
			return false;
		}

		fileRepository.deleteById(fileId);
		return true;

	}

	@Override
	public boolean deleteByPostId(Long postId) {
		List<FileDto> files = this.getByPostId(postId);

		for (FileDto file : files) {
			this.delete(file.getFileId());
		}

		return true;
	}

	@Override
	public FileDto getById(Long fileId) {
		File file = fileRepository.findById(fileId)
			.orElseThrow(() -> new IllegalArgumentException("File doesn't exist"));
		return FileDto.of(file);
	}

	@Override
	public List<FileDto> getByPostId(Long postId) {
		List<File> files = fileRepository.findAllByPostId(postId)
			.orElse(Collections.emptyList());
		return FileDto.of(files);
	}


}
