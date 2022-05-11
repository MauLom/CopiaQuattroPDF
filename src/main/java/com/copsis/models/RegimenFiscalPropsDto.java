/**
 * 
 */
package com.copsis.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Israel_work
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class RegimenFiscalPropsDto {
	private String clave;
	private String descripcion;
	private String tipo;
}
