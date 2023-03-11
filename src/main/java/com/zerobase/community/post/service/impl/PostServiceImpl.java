package com.zerobase.community.post.service.impl;

import com.zerobase.community.common.model.PagingResponse;
import com.zerobase.community.file.service.FileService;
import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.entity.Post;
import com.zerobase.community.post.mapper.PostMapper;
import com.zerobase.community.post.mapper.UserPostMapper;
import com.zerobase.community.post.model.PostInput;
import com.zerobase.community.post.model.PostParam;
import com.zerobase.community.post.repository.PostRepository;
import com.zerobase.community.post.service.PostService;
import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.repository.UserRepository;
import com.zerobase.community.util.PageUtil;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final PostMapper postMapper;
	private final UserPostMapper userPostMapper;
	private final UserRepository userRepository;
	private final FileService fileService;

	@Override
	public boolean add(PostInput parameter) throws IOException {
		User user = userRepository.findByUserEmail(parameter.getUserEmail())
			.orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

		Post post = Post.builder()
			.postId(parameter.getPostId())
			.title(parameter.getTitle())
			.contents(parameter.getContents())
			.userId(user.getUserId())
			.userName(user.getUserName())
			.createAt(LocalDate.now())
			.build();
		postRepository.save(post);

		// 파일 저장
		for (MultipartFile file : parameter.getFiles()) {
			fileService.saveFile(file, post.getPostId());
		}
		return true;
	}

	@Override
	public PagingResponse<PostDto> list(PostParam parameter) {
		long totalCount = postMapper.selectListCount(parameter);
		List<PostDto> list = postMapper.selectList(parameter);

		String pager = postPager(list, parameter, totalCount);

		return new PagingResponse<>(list, pager, totalCount);
	}

	@Override
	public PostDto getById(long postId) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("Post doesn't exist"));

		PostDto postDto = PostDto.of(post);
		postDto.setFiles(fileService.getByPostId(postId));

		return postDto;
	}

	@Override
	public PagingResponse<PostDto> myPost(String userEmail) {
		User user = userRepository.findByUserEmail(userEmail)
			.orElseThrow(() -> new IllegalArgumentException("User doesn't exist"));

		PostParam parameter = new PostParam();
		parameter.setUserId(user.getUserId());

		parameter.init();
		long totalCount = userPostMapper.selectMyPostListCount(parameter);
		List<PostDto> list = userPostMapper.selectMyPostList(parameter);

		String pager = postPager(list, parameter, totalCount);

		return new PagingResponse<>(list, pager, totalCount);
	}


	@Override
	public String postPager(List<PostDto> list, PostParam parameter, long totalCount) {

		PageUtil pageUtil = new PageUtil(totalCount, parameter.getPageIndex(),
			parameter.getQueryString());

		if (!CollectionUtils.isEmpty(list)) {
			int i = 0;
			for (PostDto x : list) {
				x.setTotalCount(totalCount);
				x.setSeq(totalCount - parameter.getPageStart() - i);
				i++;
			}
		}
		return pageUtil.pager();
	}

	@Override
	public boolean delete(String idList) {
		if (idList != null && idList.length() > 0) {
			String[] ids = idList.split(",");
			for (String x : ids) {
				long postId = 0L;
				postId = Long.parseLong(x);

				if (postId > 0) {
					postRepository.deleteById(postId);
				}
			}
		}
		return true;
	}

	@Override
	public boolean update(PostInput parameter) throws IOException {

		Post post = postRepository.findById(parameter.getPostId())
			.orElseThrow(() -> new IllegalArgumentException("Post doesn't exist"));

		post.setTitle(parameter.getTitle());
		post.setContents(parameter.getContents());
		post.setCreateAt(LocalDate.now());
		postRepository.save(post);

		// 파일 update
		for (MultipartFile file : parameter.getFiles()) {
			fileService.saveFile(file, post.getPostId());
		}

		return true;
	}

}
