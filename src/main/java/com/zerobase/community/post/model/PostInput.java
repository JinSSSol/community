package com.zerobase.community.post.model;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@ToString
@Data
public class PostInput {

	private Long postId;

	@NotBlank(message = "제목은 필수 값입니다.")
	private String title;

	@Size(max = 1000, message = "내용을 1000자 이내 등록할 수 있습니다.")
	private String contents;
	private Long userId;

	private String userName;
	private String userEmail;

	// 파일
	List<MultipartFile> files;

	// 삭제
	List<Long> ids;

}
