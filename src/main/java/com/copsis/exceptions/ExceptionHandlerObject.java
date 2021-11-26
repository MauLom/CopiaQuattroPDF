package com.copsis.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionHandlerObject {

	private String message;
	private String cause;
}
