package com.zerobase.community.post.controller;


import com.zerobase.community.common.model.PagingResponse;
import com.zerobase.community.file.dto.FileDto;
import com.zerobase.community.file.model.FileInput;
import com.zerobase.community.file.service.FileService;
import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.model.PostInput;
import com.zerobase.community.post.model.PostParam;
import com.zerobase.community.post.service.PostService;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class PostController {

	private final PostService postService;
	private final FileService fileService;

	@GetMapping("/post/add")
	public String add() {
		return "post/add";
	}

	@PostMapping("/post/add")
	public String addSubmit(@Valid PostInput parameter, Principal principal)
		throws IOException {

		parameter.setUserEmail(principal.getName());
		boolean result = postService.add(parameter);

		return "redirect:/post/list";
	}

	@GetMapping("/post/list")
	public String list(Model model, @Valid PostParam parameter) {
		PagingResponse<PostDto> posts = postService.list(parameter);

		model.addAttribute("list", posts.getList());
		model.addAttribute("totalCount", posts.getTotalCount());
		model.addAttribute("pager", posts.getPager());

		return "post/list";
	}

	@GetMapping("/post/detail/{postId}")
	public String postDetail(Model model, @PathVariable("postId") @NotBlank Long postId) {
		PostDto detail = postService.getById(postId);
		model.addAttribute("detail", detail);

		return "post/detail";
	}

	//   이미지 출력
	@GetMapping("/images/{fileId}")
	@ResponseBody
	public Resource loadImage(@PathVariable("fileId") @NotBlank Long fileId) throws IOException {
		FileDto file = fileService.getById(fileId);
		return new UrlResource("file:" + file.getSavedPath());
	}

	@PostMapping("/post/delete")
	public String delete(HttpServletRequest request, PostInput parameter) {
		boolean result = postService.deleteAll(parameter.getIds());
		return "redirect:" + request.getHeader("Referer");
	}

	@PostMapping("/file/delete")
	public String deleteFile(HttpServletRequest request, FileInput parameter) {

		boolean result = fileService.delete(parameter.getFileId());

		return "redirect:" + request.getHeader("Referer");
	}


}
