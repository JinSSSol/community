package com.zerobase.community.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class PostSearchTypeValidator implements ConstraintValidator<PostSearchTypeValid, String> {
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		// 검색 조건이 없는경우 -> valid
		if (ObjectUtils.isEmpty(value)) {
			return true;
		} else if (value.equals("title") || value.equals("contents") || value.equals("userName") || value.equals("all")) {
			return true;
		}
		return false;
	}
}
