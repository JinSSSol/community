package com.zerobase.community.post.model;

import com.zerobase.community.common.model.CommonParam;
import lombok.Data;

@Data
public class PostParam extends CommonParam {
	long postId;

	String searchType;
	String searchValue;
}
