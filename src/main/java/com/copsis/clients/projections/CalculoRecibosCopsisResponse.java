package com.copsis.clients.projections;
import java.util.List;
import org.springframework.http.HttpStatus;
import lombok.Data;

@Data
public class CalculoRecibosCopsisResponse {
	private Boolean ok;
	private HttpStatus status;
	private List<CalculaRecibosProjection> result;
	private String message;
	private String cause;
}