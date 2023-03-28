package com.zerobase.community.user.mapper;

import com.zerobase.community.user.dto.UserDto;
import com.zerobase.community.user.model.UserParam;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

	long selectListCount(UserParam parameter);
	List<UserDto> selectList(UserParam parameter);


}
