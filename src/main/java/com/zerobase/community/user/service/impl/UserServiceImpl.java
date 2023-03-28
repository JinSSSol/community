package com.zerobase.community.user.service.impl;

import com.zerobase.community.common.model.PagingResponse;
import com.zerobase.community.exception.CustomException;
import com.zerobase.community.exception.ErrorCode;
import com.zerobase.community.user.dto.UserDto;
import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.mapper.UserMapper;
import com.zerobase.community.user.model.UserInput;
import com.zerobase.community.user.model.UserParam;
import com.zerobase.community.user.model.constrains.Role;
import com.zerobase.community.user.repository.UserRepository;
import com.zerobase.community.user.service.UserService;
import com.zerobase.community.util.PageUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	@Override
	public boolean register(UserInput parameter) {
		Optional<User> optionalUser = userRepository.findByUserEmail(parameter.getUserEmail());
		if (optionalUser.isPresent()) {
			return false;
		}

		String encPassword = BCrypt.hashpw(parameter.getUserPassword(), BCrypt.gensalt());
		String uuid = UUID.randomUUID().toString();

		String birtStr = parameter.getUserBirth();
		LocalDate birth = LocalDate.parse(birtStr);

		boolean adminYn = false;
		if (parameter.getAdminAuthStatus().equals("success")) {
			adminYn = true;
		}

		User user = User.builder()
			.userEmail(parameter.getUserEmail())
			.userPassword(encPassword)
			.userName(parameter.getUserName())
			.userBirth(birth)
			.createAt(LocalDate.now())
			.adminYn(adminYn)
			.build();

		userRepository.save(user);
		log.info("User registration complete! -> " + user.getUserId());

		return true;
	}

	@Override
	public boolean delete(Long userId) {
		userRepository.deleteById(userId);
		log.info("User deletion complete! -> " + userId);
		return true;
	}

	@Override
	public PagingResponse<UserDto> list(UserParam parameter) {
		parameter.init();
		long totalCount = userMapper.selectListCount(parameter);
		List<UserDto> list = userMapper.selectList(parameter);

		String pager = userPager(list, parameter, totalCount);

		return new PagingResponse<>(list, pager, totalCount);
	}

	@Override
	public UserDto getById(Long userId) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		return UserDto.of(user);
	}

	@Override
	public String userPager(List<UserDto> list, UserParam parameter, long totalCount) {

		PageUtil pageUtil = new PageUtil(totalCount, parameter.getPageIndex(),
			parameter.getQueryString());

		if (!CollectionUtils.isEmpty(list)) {
			int i = 0;
			for (UserDto x : list) {
				x.setTotalCount(totalCount);
				x.setSeq(totalCount - parameter.getPageStart() - i);
				i++;
			}
		}
		return pageUtil.pager();
	}

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

		User user = userRepository.findByUserEmail(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException("회원 정보가 존재하지 않습니다."));

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(Role.USER.getKey()));

		if (user.isAdminYn()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(Role.ADMIN.getKey()));
		}

		return new org.springframework.security.core.userdetails.User(user.getUserEmail(),
			user.getUserPassword(), grantedAuthorities);
	}
}
