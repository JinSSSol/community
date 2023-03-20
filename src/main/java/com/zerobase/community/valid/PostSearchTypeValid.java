package com.zerobase.community.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PostSearchTypeValidator.class)
public @interface PostSearchTypeValid {

	String message() default "게시글 검색 조건에 해당하지 않습니다.";

	Class<?>[] groups() default  {};
	Class<? extends Payload>[] payload() default {};

}
