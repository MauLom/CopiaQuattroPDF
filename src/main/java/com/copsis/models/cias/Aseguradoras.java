package com.copsis.models.cias;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Aseguradoras {
    
    @JsonProperty("cias")
	public ArrayList<Aseguradora> aseguradoras;

    @Getter
	@Setter
	@ToString
	public static class Aseguradora {
		@JsonProperty("id")
		private int id;
		@JsonProperty("abrevia")
		private String abrevia;		
	}
	
}
