package com.copsis.exceptions;

public class GeneralServiceException  extends CopsisException {
	private static final long serialVersionUID = 1L;

	public GeneralServiceException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}
}
