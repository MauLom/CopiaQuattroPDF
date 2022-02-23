package com.copsis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SURACoberturaDTO {
	private Long id;
	private String nombre;
	private double prima;
	private double sa;
	private int orden;
}