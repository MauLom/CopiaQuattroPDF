package com.copsis.exceptions;

import lombok.Getter;

@Getter
public abstract class CopsisException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String errorCode;
	private final String errorMessage;

	protected CopsisException(String errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

}
