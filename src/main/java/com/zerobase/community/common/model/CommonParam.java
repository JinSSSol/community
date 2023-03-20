package com.zerobase.community.common.model;

import com.zerobase.community.valid.Group;
import com.zerobase.community.valid.PostSearchTypeValid;
import com.zerobase.community.valid.UserSearchTypeValid;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class CommonParam {

	long pageIndex;
	long pageSize;

	@PostSearchTypeValid(groups = Group.PostSearch.class)
	@UserSearchTypeValid(groups = Group.UserSearch.class)
	String searchType;

	@Size(max = 100, message = "검색은 100자 이하로 가능합니다.")
	String searchValue;


	public long getPageStart() {
		init();
		return (pageIndex - 1) * pageSize;
	}

	public long getPageEnd() {
		init();
		return pageSize;
	}

	public void init() {
		if (pageIndex < 1) {
			pageIndex = 1;
		}

		if (pageSize < 10) {
			pageSize = 10;
		}
	}


	public String getQueryString() {
		init();

		StringBuilder sb = new StringBuilder();

		if (searchType != null && searchType.length() > 0) {
			sb.append(String.format("searchType=%s", searchType));
		}

		if (searchValue != null && searchValue.length() > 0) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("searchValue=%s", searchValue));
		}

		return sb.toString();
	}
}
