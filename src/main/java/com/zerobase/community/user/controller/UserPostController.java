package com.zerobase.community.user.controller;

import com.zerobase.community.common.model.PagingResponse;
import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.model.PostInput;
import com.zerobase.community.post.service.PostService;
import java.io.IOException;
import java.security.Principal;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserPostController {

	private final PostService postService;

	@GetMapping("/user/main")
	public String userMain() {
		return "user/main";
	}

	@GetMapping("/user/post/list")
	public String userManagePost(Model model, Principal principal) {

		String userEmail = principal.getName();
		PagingResponse<PostDto> myPosts = postService.myPostByEmail(userEmail);

		model.addAttribute("list", myPosts.getList());
		model.addAttribute("totalCount", myPosts.getTotalCount());
		model.addAttribute("pager", myPosts.getPager());
		return "user/post/list";
	}

	@GetMapping("/user/post/update/{postId}")
	public String update(Model model, @PathVariable @NotBlank Long postId) {

		PostDto detail = postService.getById(postId);
		model.addAttribute("detail", detail);

		return "user/post/update";
	}

	@PostMapping("/user/post/update/{postId}")
	public String updateSubmit(@PathVariable @NotBlank Long postId, @Valid PostInput parameter) throws IOException {

		parameter.setPostId(postId);
		boolean result = postService.update(parameter);

		return "redirect:/user/post/list";

	}
}
