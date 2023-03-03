package com.zerobase.community.user.service.impl;

import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.model.UserInput;
import com.zerobase.community.user.repository.UserRepository;
import com.zerobase.community.user.service.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

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

		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);

		if (!optionalUser.isPresent()) {
			throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
		}

		User user = optionalUser.get();

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

		if (user.isAdminYn()) {
			grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return new org.springframework.security.core.userdetails.User(user.getUserEmail(),
			user.getUserPassword(), grantedAuthorities);
	}
}
