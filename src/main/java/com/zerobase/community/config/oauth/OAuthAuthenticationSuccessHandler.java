package com.zerobase.community.config.oauth;

import com.zerobase.community.config.jwt.TokenProvider;
import com.zerobase.community.config.oauth.dto.CustomOAuth2User;
import com.zerobase.community.exception.CustomException;
import com.zerobase.community.exception.ErrorCode;
import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.model.constrains.Role;
import com.zerobase.community.user.repository.UserRepository;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthAuthenticationSuccessHandler implements
	AuthenticationSuccessHandler {

	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		log.info("OAuth2 로그인에 성공하였습니다.");
		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

		if (oAuth2User.getRole() == Role.GUEST) {
			User user = userRepository.findByUserEmail(oAuth2User.getEmail())
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
			user.authorizeUser();

		}

		loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User)
		throws IOException {
		String accessToken = tokenProvider.generateAccessToken(oAuth2User.getEmail(),
			oAuth2User.getRole().getKey());
		String refreshToken = tokenProvider.generateRefreshToken();
		response.addHeader(tokenProvider.getAccessHeader(), "Bearer " + accessToken);
		response.addHeader(tokenProvider.getRefreshHeader(), "Bearer " + refreshToken);

		tokenProvider.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
		tokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);

	}


}
