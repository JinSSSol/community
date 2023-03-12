package com.zerobase.community.post.mapper;

import com.zerobase.community.post.dto.PostDto;
import com.zerobase.community.post.model.PostParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPostMapper {

	long selectMyPostListCount(PostParam parameter);

	List<PostDto> selectMyPostList(PostParam parameter);
}
