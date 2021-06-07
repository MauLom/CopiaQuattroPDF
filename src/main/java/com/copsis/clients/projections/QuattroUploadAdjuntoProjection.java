package com.copsis.clients.projections;

import org.springframework.http.HttpStatus;

import com.copsis.dto.AdjuntoDTO;

import lombok.Data;
@Data
public class QuattroUploadAdjuntoProjection {
	private Boolean ok;
	private HttpStatus status;
	private AdjuntoDTO result;
	private String message;
	private String cause;
}
