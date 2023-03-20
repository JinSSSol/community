package com.zerobase.community.admin.controller;


import com.zerobase.community.admin.service.AdminService;
import com.zerobase.community.common.model.PagingResponse;
import com.zerobase.community.user.dto.UserDto;
import com.zerobase.community.user.model.UserParam;
import com.zerobase.community.user.service.UserService;
import com.zerobase.community.valid.Group;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/admin/user")
@Controller
public class AdminMemberController {

	private final AdminService adminService;
	private final UserService userService;

	@GetMapping
	public String userList(Model model, @Validated(Group.UserSearch.class) UserParam parameter) {

		PagingResponse<UserDto> users = userService.list(parameter);

		model.addAttribute("list", users.getList());
		model.addAttribute("totalCount", users.getTotalCount());
		model.addAttribute("pager", users.getPager());
		return "admin/user/list";
	}

	@GetMapping("/{userId}")
	public String userDetail(@PathVariable @NotBlank Long userId, Model model) {

		UserDto userDetail = userService.getById(userId);
		model.addAttribute("detail", userDetail);
		return "admin/user/detail";
	}

	@DeleteMapping("/{userId}")
	public String userDelete(@PathVariable @NotBlank Long userId) {
		adminService.deleteUser(userId);
		return "redirect:/admin/user";
	}


}
