package com.zerobase.community.config;

import com.zerobase.community.config.auth.CustomOAuth2UserService;
import com.zerobase.community.config.auth.JwtAuthenticationFilter;
import com.zerobase.community.config.auth.OAuthAuthenticationSuccessHandler;
import com.zerobase.community.user.model.constrains.Role;
import com.zerobase.community.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserService userService;
	private final UserAuthenticationSuccessHandler userAuthenticationSuccessHandler;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserAuthenticationFailureHandler getFailureHandler() {
		return new UserAuthenticationFailureHandler();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/favicon.ico", "/files/**");

		super.configure(web);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		http.headers().frameOptions().sameOrigin();

		http.authorizeRequests()
			.antMatchers(
				"/"
				, "/user/register"
				, "/user/register_admin"
				, "/post/list"
				, "/post/detail/*"
				, "/images/*"
			)
			.permitAll();

		http.authorizeRequests()
			.antMatchers("/admin/**")
			.hasAuthority(Role.ADMIN.name());

		http.formLogin()
			.loginPage("/user/login")
			.usernameParameter("userEmail")
			.failureHandler(getFailureHandler())
			.successHandler(userAuthenticationSuccessHandler)
			.permitAll();

		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
			.logoutSuccessUrl("/")
			.invalidateHttpSession(true);

		http.exceptionHandling()
			.accessDeniedPage("/error/denied");

		http.oauth2Login()
			//.defaultSuccessUrl("/")
			.successHandler(oAuthAuthenticationSuccessHandler)
			.userInfoEndpoint().userService(customOAuth2UserService);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		super.configure(http);

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
			.passwordEncoder(getPasswordEncoder());

		super.configure(auth);
	}



}
