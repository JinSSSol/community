package com.zerobase.community.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class UserSearchTypeValidator implements ConstraintValidator<UserSearchTypeValid, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		// 검색 조건이 없는경우 -> valid
		if (ObjectUtils.isEmpty(value)) {
			return true;
		} else if ( value.equals("userName") || value.equals("userEmail") || value.equals("all")) {
			return true;
		}
		return false;
	}
}
