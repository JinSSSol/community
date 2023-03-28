package com.zerobase.community.config.auth;

import static com.zerobase.community.user.model.constrains.SocialType.KAKAO;
import static com.zerobase.community.user.model.constrains.SocialType.NAVER;

import com.zerobase.community.config.auth.dto.CustomOAuth2User;
import com.zerobase.community.config.auth.dto.OAuthAttributes;
import com.zerobase.community.config.auth.dto.SessionUser;
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
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
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

		User user = saveOrUpdate(attributes);

		httpSession.setAttribute("user", new SessionUser(user));

		return new CustomOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
			attributes.getAttributes(),
			attributes.getNameAttributeKey(),
			user.getUserEmail(),
			user.getRole());
	}

	private SocialType getSocialType(String registrationId) {
		if(NAVER.equals(registrationId)) {
			return NAVER;
		}
		if(KAKAO.equals(registrationId)) {
			return KAKAO;
		}
		return SocialType.GOOGLE;
	}

	private User saveOrUpdate(OAuthAttributes attributes) {
		User user = userRepository.findByUserEmail(attributes.getEmail())
			.map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
			.orElse(attributes.toEntity());

		return userRepository.save(user);
	}

//	private User getUser(OAuthAttributes attributes, SocialType socialType) {
//		User findUser = userRepository.findBySocialTypeAndSocialId(socialType,
//			attributes.getOauth2UserInfo().getId()).orElse(null);
//
//		if(findUser == null) {
//			return saveUser(attributes, socialType);
//		}
//		return findUser;
//	}
//
//	/**
//	 * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
//	 * 생성된 User 객체를 DB에 저장 : socialType, socialId, email, role 값만 있는 상태
//	 */
//	private User saveUser(OAuthAttributes attributes, SocialType socialType) {
//		User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
//		return userRepository.save(createdUser);
//	}

}
