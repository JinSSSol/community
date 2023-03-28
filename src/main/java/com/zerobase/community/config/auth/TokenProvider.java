package com.zerobase.community.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zerobase.community.exception.CustomException;
import com.zerobase.community.exception.ErrorCode;
import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.repository.UserRepository;
import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class TokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirePeriod;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirePeriod;

	@Value("${jwt.access.header}")
	private String accessHeader;

	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
	public static final String TOKEN_PREFIX = "Bearer ";
	private static final String KEY_ROLE = "role";

	private final UserRepository userRepository;

	public String generateAccessToken(String username, String role) {
		var now = new Date();
		var expireDate = new Date(now.getTime() + accessTokenExpirePeriod);

		return JWT.create()
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withClaim("subject", username)
			.withClaim(KEY_ROLE, role)
			.withIssuedAt(now)    // 토큰 생성 시간
			.withExpiresAt(expireDate)    // 토큰 만료 시간
			.sign(Algorithm.HMAC512(secretKey));
	}

	public String generateRefreshToken() {
		var now = new Date();
		var expireDate = new Date(now.getTime() + refreshTokenExpirePeriod);

		return JWT.create()
			.withSubject(REFRESH_TOKEN_SUBJECT)
			.withIssuedAt(now)
			.withExpiresAt(expireDate)
			.sign(Algorithm.HMAC512(secretKey));
	}

	public void sendAccessToken(HttpServletResponse response, String accessToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		response.setHeader(accessHeader, accessToken);
		log.info("재발급된 Access Token : {}", accessToken);
	}

	public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken,
		String refreshToken) {
		response.setStatus(HttpServletResponse.SC_OK);

		response.setHeader(accessHeader, accessToken);
		response.setHeader(refreshHeader, refreshToken);
		log.info("Access Token, Refresh Token 헤더 설정 완료");
	}

	public Optional<String> extractRefreshToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(refreshHeader))
			.filter(refreshToken -> refreshToken.startsWith(TOKEN_PREFIX))
			.map(refreshToken -> refreshToken.replace(TOKEN_PREFIX, ""));
	}

	public Optional<String> extractAccessToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(accessHeader))
			.filter(refreshToken -> refreshToken.startsWith(TOKEN_PREFIX))
			.map(refreshToken -> refreshToken.replace(TOKEN_PREFIX, ""));
	}

	public Optional<String> extractEmail(String accessToken) {
		try {
			// 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
			return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
				.build()
				.verify(accessToken)
				.getClaim("subject")
				.asString());
		} catch (Exception e) {
			log.error("액세스 토큰이 유효하지 않습니다.");
			return Optional.empty();
		}
	}

	public boolean isTokenValid(String token) {
		try {
			JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
			return true;
		} catch (Exception e) {
			log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
			return false;
		}
	}

	public void updateRefreshToken(String email, String refreshToken) {
		User user = userRepository.findByUserEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		user.updateRefreshToken(refreshToken);
	}
}
