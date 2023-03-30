package com.zerobase.community.config.oauth;

import static com.zerobase.community.user.model.constrains.SocialType.KAKAO;
import static com.zerobase.community.user.model.constrains.SocialType.NAVER;

import com.zerobase.community.config.oauth.dto.CustomOAuth2User;
import com.zerobase.community.config.oauth.dto.OAuthAttributes;
import com.zerobase.community.config.oauth.dto.SessionUser;
import com.zerobase.community.user.entity.User;
import com.zerobase.community.user.model.constrains.SocialType;
import com.zerobase.community.user.repository.UserRepository;
import java.util.Collections;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	private final HttpSession httpSession;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		SocialType socialType = getSocialType(registrationId);
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
			.getUserInfoEndpoint().getUserNameAttributeName();

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
			oAuth2User.getAttributes());

		User user = saveOrUpdate(attributes, socialType);

		httpSession.setAttribute("user", new SessionUser(user));

		return new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
			attributes.getAttributes(),
			attributes.getNameAttributeKey(),
			user.getUserEmail(),
			user.getRole());
	}

	private SocialType getSocialType(String registrationId) {
		if (NAVER.equals(registrationId)) {
			return NAVER;
		}
		if (KAKAO.equals(registrationId)) {
			return KAKAO;
		}
		return SocialType.GOOGLE;
	}

	private User saveOrUpdate(OAuthAttributes attributes, SocialType socialType) {
		User user = userRepository.findByUserEmail(attributes.getEmail())
			.map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
			.orElse(attributes.toEntity(socialType));

		return userRepository.save(user);
	}

}
