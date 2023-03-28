package com.zerobase.community.config;

import com.zerobase.community.config.auth.TokenProvider;
import com.zerobase.community.exception.CustomException;
import com.zerobase.community.exception.ErrorCode;
import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.repository.UserRepository;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

	@Value("${jwt.access.expiration")
	private String accessTokenExpiration;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		String email = authentication.getName();
		User user = userRepository.findByUserEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


		String accessToken = tokenProvider.generateAccessToken(email, user.getRoleKey());

		tokenProvider.sendAccessToken(response, accessToken);
		log.info("로그인에 성공하였습니다. 이메일 : {}", email);
		log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
		log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
