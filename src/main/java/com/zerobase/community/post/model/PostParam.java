package com.zerobase.community.post.model;

import com.zerobase.community.common.model.CommonParam;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class PostParam extends CommonParam {

	Long userId;

	@Size(max = 8)
	String searchType;

	@Size(max = 100, message = "검색은 100자 이하로 가능합니다.")
	String searchValue;
}
