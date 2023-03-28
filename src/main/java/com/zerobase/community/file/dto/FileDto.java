package com.zerobase.community.file.dto;

import com.zerobase.community.file.entity.File;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FileDto {

	private Long fileId;
	private String originName;
	private String savedName;
	private String savedPath;
	private Long postId;

	public static FileDto of(File file) {
		return FileDto.builder()
			.fileId(file.getFileId())
			.originName(file.getOriginName())
			.savedName(file.getSavedName())
			.savedPath(file.getSavedPath())
			.postId(file.getPostId())
			.build();
	}

	public static List<FileDto> of(List<File> files) {

		if (files == null) {
			return null;
		}

		List<FileDto> fileList = new ArrayList<>();
		for (File x : files) {
			fileList.add(FileDto.of(x));
		}
		return fileList;
	}

}
