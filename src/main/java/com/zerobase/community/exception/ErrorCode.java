package com.zerobase.community.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	SUCCESS(HttpStatus.OK, "OK"),

	USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 사용자를 찾을 수 없습니다."),
	POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 게시글을 찾을 수 없습니다."),
	POST_NOT_FOUND_BY_USER(HttpStatus.BAD_REQUEST, "해당 사용자의 게시글을 찾을 수 없습니다."),
	FILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 파일을 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String message;

}
