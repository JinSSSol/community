package com.zerobase.community.post.controller;


import com.zerobase.community.file.dto.FileDto;
import com.zerobase.community.file.entity.File;
import com.zerobase.community.file.repository.FileRepository;
import com.zerobase.community.file.service.FileService;
import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.model.PostInput;
import com.zerobase.community.post.model.PostParam;
import com.zerobase.community.post.service.PostService;
import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.repository.UserRepository;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class PostController extends BaseController {

	private final PostService postService;
	private final FileService fileService;
	private final UserRepository userRepository;
	private final FileRepository fileRepository;

	@GetMapping("/post/add")
	public String add(Model model, Principal principal) {
		return "post/add";
	}

	@PostMapping("/post/add")
	public String addSubmit(Model model, PostInput parameter, Principal principal)
		throws IOException {
		String userEmail = principal.getName();
		Optional<User> user = userRepository.findByUserEmail(userEmail);
		parameter.setUserId(user.get().getUserId());
		parameter.setUserName(user.get().getUserName());

		Long postId = postService.add(parameter);

		// 파일 저장
		for (MultipartFile file : parameter.getFiles()) {
			fileService.saveFile(file, postId);
		}

		return "redirect:/post/list";
	}

	@GetMapping("/post/list")
	public String list(Model model, PostParam parameter) {
		parameter.init();
		List<PostDto> posts = postService.list(parameter);

		long totalCount = 0;
		if (posts != null && posts.size() > 0) {
			totalCount = posts.get(0).getTotalCount();
		}
		String queryString = parameter.getQueryString();
		String pagerHtml = getPaperHtml(totalCount, parameter.getPageSize(),
			parameter.getPageIndex(), queryString);

		model.addAttribute("list", posts);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("pager", pagerHtml);

		return "post/list";
	}

	@GetMapping("/post/detail/{postId}")
	public String postDetail(Model model, PostParam parameter) {
		PostDto detail = postService.getById(parameter.getPostId());
		model.addAttribute("detail", detail);

		Optional<List<File>> optionalFiles = fileRepository.findAllByPostId(parameter.getPostId());
		if (optionalFiles.isPresent()) {
			model.addAttribute("files", FileDto.of(optionalFiles.get()));
		}

		return "post/detail";
	}

	//   이미지 출력
	@GetMapping("/images/{fileId}")
	@ResponseBody
	public Resource loadImage(@PathVariable("fileId") Long id, Model model) throws IOException {

		File file = fileRepository.findById(id).orElse(null);
		return new UrlResource("file:" + file.getSavedPath());
	}


}
