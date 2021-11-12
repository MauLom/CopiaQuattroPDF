package com.copsis.exceptions;

public class GeneralServiceException  extends CopsisException {
	private static final long serialVersionUID = 1L;

	public GeneralServiceException(String errorMessage, String exMessage, Object objectBody) {
		super(errorMessage, exMessage, objectBody);
	}

	public GeneralServiceException(String errorMessage, String exMessage) {
		super(errorMessage, exMessage);
	}
}
