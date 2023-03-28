package com.zerobase.community.exception;

public abstract class AbstractException extends RuntimeException {

	abstract public int getStatusCode();
	abstract public String getMessage();
}
