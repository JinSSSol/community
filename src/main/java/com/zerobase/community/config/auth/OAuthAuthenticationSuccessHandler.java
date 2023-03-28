package com.zerobase.community.config.auth;

import com.zerobase.community.config.auth.dto.CustomOAuth2User;
import com.zerobase.community.exception.CustomException;
import com.zerobase.community.exception.ErrorCode;
import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.model.constrains.Role;
import com.zerobase.community.user.repository.UserRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthAuthenticationSuccessHandler implements
	AuthenticationSuccessHandler {

	private static final String REDIRECT_URI = "/";
	private final TokenProvider tokenProvide;
	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {

		log.info("OAuth2 로그인에 성공하였습니다.");
		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

		if(oAuth2User.getRole() == Role.GUEST) {
			String accessToken = tokenProvide.generateAccessToken(oAuth2User.getEmail(), oAuth2User.getRole().getKey());
			response.addHeader(tokenProvide.getAccessHeader(), "Bearer " + accessToken);
			response.sendRedirect("index");

			tokenProvide.sendAccessAndRefreshToken(response, accessToken, null);

			User user = userRepository.findByUserEmail(oAuth2User.getEmail())
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
			user.authorizeUser();

		} else {
			loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
		}
//		response.sendRedirect(UriComponentsBuilder.fromUriString(REDIRECT_URI)
//			.queryParam("accessToken", "accessToken")
//			.queryParam("refreshToken", "refreshToken")
//			.build()
//			.encode(StandardCharsets.UTF_8)
//			.toUriString()
//		);
	}

	private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
		String accessToken = tokenProvide.generateAccessToken(oAuth2User.getEmail(), oAuth2User.getRole().getKey());
		String refreshToken = tokenProvide.generateRefreshToken();
		response.addHeader(tokenProvide.getAccessHeader(), "Bearer " + accessToken);
		response.addHeader(tokenProvide.getRefreshHeader(), "Bearer " + refreshToken);

		tokenProvide.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		tokenProvide.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
	}


}
