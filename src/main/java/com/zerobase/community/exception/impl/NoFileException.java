package com.zerobase.community.exception.impl;

import com.zerobase.community.exception.AbstractException;
import com.zerobase.community.exception.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NoFileException extends AbstractException {

//	public NoFileException(Long fileId) {
//		super(message + "fileId: " + fileId);
//	}

	@Override
	public int getStatusCode() {
		return 0;
	}

	@Override
	public String getMessage() {
		return null;
	}
}
