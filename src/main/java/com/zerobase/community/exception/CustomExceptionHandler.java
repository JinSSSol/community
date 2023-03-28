package com.zerobase.community.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

		ErrorResponse errorResponse = ErrorResponse.builder()
			.status(e.getErrorCode().getStatus())
			.code(e.getErrorCode().name())
			.message(e.getErrorCode().getMessage())
			.build();

		return ResponseEntity
			.status(e.getErrorCode().getStatus())
			.body(errorResponse);
	}
}
