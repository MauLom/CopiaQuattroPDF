package com.copsis.exceptions;

public class ValidationServiceException extends CopsisException {
	private static final long serialVersionUID = 1L;

	public ValidationServiceException(String errorMessage, Object objectBody) {
		super(errorMessage, objectBody);
	}
	
	public ValidationServiceException(String errorMessage) {
		super(errorMessage);
	}
}
