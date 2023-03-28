package com.zerobase.community.config.auth;

import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.repository.UserRepository;
import com.zerobase.community.util.PasswordUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jdk.nashorn.internal.parser.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public final TokenProvider tokenProvider;
	public final UserRepository userRepository;
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String refreshToken = tokenProvider.extractRefreshToken(request)
			.filter(tokenProvider::isTokenValid)
			.orElse(null);

		// refreshToken -> accessToken 재발급, 인증 처리 x
		if (StringUtils.hasText(refreshToken)) {
			checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
			return;
		}

		checkAccessTokenAndAuthentication(request, response, filterChain);
	}

	public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
		userRepository.findByRefreshToken(refreshToken)
			.ifPresent(user -> {
				String reIssuedRefreshToken = reIssueRefreshToken(user);
				tokenProvider.sendAccessAndRefreshToken(response, tokenProvider.generateAccessToken(user.getUserEmail(), user.getRoleKey()), reIssuedRefreshToken);
			});
	}

	private String reIssueRefreshToken(User user) {
		String reIssuedRefreshToken = tokenProvider.generateRefreshToken();
		user.updateRefreshToken(reIssuedRefreshToken);
		userRepository.saveAndFlush(user);
		return reIssuedRefreshToken;
	}

	public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		log.info("checkAccessTokenAndAuthentication() 호출");
		tokenProvider.extractAccessToken(request)
			.filter(tokenProvider::isTokenValid)
			.ifPresent(accessToken -> tokenProvider.extractEmail(accessToken)
				.ifPresent(email -> userRepository.findByUserEmail(email)
					.ifPresent(this::saveAuthentication)));

		//System.out.println((tokenProvider.isTokenValid(tokenProvider.extractAccessToken(request).get())));
		filterChain.doFilter(request, response);
	}

	public void saveAuthentication(User user) {
		String password = user.getUserPassword();
		if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
			password = PasswordUtil.generateRandomPassword();
		}

		UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
			.username(user.getUserEmail())
			.password(password)
			.roles(user.getRoleKey())
			.build();

		Authentication authentication =
			new UsernamePasswordAuthenticationToken(userDetails, null,
				authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
