package com.zerobase.community.post.service.impl;

import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.entity.Post;
import com.zerobase.community.post.mapper.PostMapper;
import com.zerobase.community.post.model.PostInput;
import com.zerobase.community.post.model.PostParam;
import com.zerobase.community.post.repository.PostRepository;
import com.zerobase.community.post.service.PostService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final PostMapper postMapper;

	@Override
	public Long add(PostInput parameter) {
		Post post = Post.builder()
			.postId(parameter.getPostId())
			.title(parameter.getTitle())
			.contents(parameter.getContents())
			.userId(parameter.getUserId())
			.userName(parameter.getUserName())
			.createAt(LocalDate.now())
			.build();
		postRepository.save(post);
		return post.getPostId();
	}

	@Override
	public List<PostDto> list(PostParam parameter) {
		long totalCount = postMapper.selectListCount(parameter);

		List<PostDto> list = postMapper.selectList(parameter);

		if (!CollectionUtils.isEmpty(list)) {
			int i = 0;
			for (PostDto x : list) {
				x.setTotalCount(totalCount);
				x.setSeq(totalCount - parameter.getPageStart() - i);
				i++;
			}
		}

		return list;
	}

	@Override
	public List<PostDto> listAll() {
		List<Post> postList = postRepository.findAll();
		return PostDto.of(postList);
	}

	@Override
	public PostDto getById(long postId) {
		Optional<Post> post = postRepository.findById(postId);
		if (!post.isPresent()) {
			return null;
		}
		return PostDto.of(post.get());
	}

}
