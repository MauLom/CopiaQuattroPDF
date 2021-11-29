package com.copsis.exceptions;

public class NotFoundException extends CopsisException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
