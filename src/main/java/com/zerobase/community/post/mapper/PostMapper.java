package com.zerobase.community.post.mapper;

import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.model.PostParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
	long selectListCount(PostParam parameter);
	List<PostDto> selectList(PostParam parameter);
}
