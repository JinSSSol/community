package com.zerobase.community.exception.impl;

public class NoPostException extends IllegalArgumentException {

	private static final String message = "존재하지 않는 파일 입니다.";

	public NoPostException(Long fileId) {
		super(message + "fileId: " + fileId);
	}
}
