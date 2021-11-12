package com.copsis.exceptions;

import lombok.Getter;

@Getter
public abstract class CopsisException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String errorMessage;
	private final String exMessage;
	private Object objectBody;

	protected CopsisException(String errorMessage, String exMessage, Object objectBody) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.exMessage = exMessage;
		this.objectBody = objectBody;
	}
	
	protected CopsisException(String errorMessage, String exMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.exMessage = exMessage;
	}
	
	protected CopsisException(String errorMessage, Object objectBody) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.exMessage = errorMessage;
		this.objectBody = objectBody;
	}
	
	protected CopsisException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.exMessage = errorMessage;
	}

}
