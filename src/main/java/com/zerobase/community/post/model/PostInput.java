package com.zerobase.community.post.model;

import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Data
public class PostInput {

	private Long postId;
	private String title;
	private String contents;
	private Long userId;

	private String userName;

	// 파일
	List<MultipartFile> files;

}
