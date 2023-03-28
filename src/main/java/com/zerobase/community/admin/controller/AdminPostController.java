package com.zerobase.community.admin.controller;


import com.zerobase.community.common.model.PagingResponse;
import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.model.PostParam;
import com.zerobase.community.post.service.PostService;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/admin/post")
@Controller
public class AdminPostController {

	private final PostService postService;

	@GetMapping("")
	public String postList(Model model) {

		PagingResponse<PostDto> posts = postService.list(new PostParam());

		model.addAttribute("list", posts.getList());
		model.addAttribute("totalCount", posts.getTotalCount());
		model.addAttribute("pager", posts.getPager());

		return "admin/post/list";
	}

	@GetMapping("/{userId}")
	public String userPostList(@PathVariable @NotBlank Long userId, Model model) {

		PagingResponse<PostDto> userPosts = postService.myPost(userId);

		model.addAttribute("list", userPosts.getList());
		model.addAttribute("totalCount", userPosts.getTotalCount());
		model.addAttribute("pager", userPosts.getPager());

		return "user/post/list";
	}


}
