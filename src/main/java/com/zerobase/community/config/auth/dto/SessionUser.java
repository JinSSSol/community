package com.zerobase.community.config.auth.dto;

import com.zerobase.community.user.entity.User;
import java.io.Serializable;

public class SessionUser implements Serializable {
	private String name;
	private String email;
	private String picture;

	public SessionUser(User user) {
		this.name = user.getUserName();
		this.email = user.getUserEmail();
		this.picture = user.getPicture();

	}

}
