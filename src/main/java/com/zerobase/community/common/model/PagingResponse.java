package com.zerobase.community.common.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PagingResponse<T> {
	private List<T> list = new ArrayList<>();
	private String pager;

	private long totalCount;

}
