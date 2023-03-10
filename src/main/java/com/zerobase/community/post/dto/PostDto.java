package com.zerobase.community.post.dto;

import com.zerobase.community.file.dto.FileDto;
import com.zerobase.community.post.entity.Post;
import java.time.LocalDate;
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
public class PostDto {

	private Long postId;
	private String title;
	private String contents;
	private Long userId;

	private String userName;
	private LocalDate createAt;

	// 페이징
	long totalCount;
	long seq;

	// 파일
	List<FileDto> files;

	public static PostDto of(Post post) {
		return PostDto.builder()
			.postId(post.getPostId())
			.title(post.getTitle())
			.contents(post.getContents())
			.userId(post.getUserId())
			.userName(post.getUserName())
			.createAt(post.getCreateAt())
			.build();
	}

	public static List<PostDto> of(List<Post> posts) {
		if (posts == null) {
			return null;
		}
		List<PostDto> postList = new ArrayList<>();
		for (Post x : posts) {
			postList.add(PostDto.of(x));
		}
		return postList;
	}
}
