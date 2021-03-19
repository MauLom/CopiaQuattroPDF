package com.copsis.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CopsisResponse {
	
	private Boolean ok;
	private HttpStatus status;
	private Object result;
	private String message;
	
	public CopsisResponse(Builder builder) {
		this.ok = builder.ok;
		this.status = builder.status;
		this.result = builder.result;
		this.message = builder.message;
	}
	
	public static class Builder {
		
		private Boolean ok;
		private HttpStatus status;
		private Object result; 
		private String message;
		
		public Builder() {}
		
		public Builder(Boolean ok) {
			this.ok = ok;
		}
		
		public Builder ok(Boolean ok) {
			this.ok = ok;
			return this;
		}

		public Builder status(HttpStatus status) {
			this.status = status;
			return this;
		}

		public Builder result(Object result) {
			this.result = result;
			return this;
		}
		
		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public ResponseEntity<CopsisResponse> build() {
			
			
			return new ResponseEntity<CopsisResponse> (new CopsisResponse(this), this.status);
		}
	}
	
}
