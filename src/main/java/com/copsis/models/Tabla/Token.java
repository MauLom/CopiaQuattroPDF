package com.copsis.models.Tabla;

import lombok.Data;

@Data
public class Token {
	
	private final TokenType type;
	private final String data;
	
	public Token(TokenType type, String data) {
		this.type = type;
		this.data = data;
	}
	
	  
}
