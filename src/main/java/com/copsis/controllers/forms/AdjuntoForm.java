package com.copsis.controllers.forms;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjuntoForm {
	private MultipartFile file;
	private String folder;
	private String bucket;
	private int adjuntoID = 0;
	private int clienteID = 0;
	private int polizaID = 0;
	private int endosoID = 0;
	private int reciboID = 0;
	private int pagoID = 0;
	private int edoctaVendedorID = 0;
	private int aseguradoID = 0;
	private int polizaAseguradoID = 0;
	private int vehiculoID = 0;
	private int polizaVehiculoID = 0;
	private int crmID = 0;
	private int cotizacionID = 0;
	private int prospectID = 0;
	private int emailID = 0;
	private int vendedorID = 0;
	private int aseguradoraID = 0;
	private int comisionID = 0;
	private int conciliacionID = 0;
	private int siniestroID = 0;
	private int entidadID = 0;
	private int entidadTipo = 0;
	private int tipo = 0;
	private String concepto	= "General";
	private int privado = 0;
	private String webpath = "";
	private String nombreOriginal = "";
	private String d;
	private String b64;
}
