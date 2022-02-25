package com.copsis.clients.projections;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImpresionReclamacionProjection {
	@JsonProperty("c3")
	public List<C3> tramites;
	@JsonProperty("c4")
	public List<C4> primas;
	@JsonProperty("c5")
	public C5 c5;
	@JsonProperty("c6")
	public List<C6> c6;
	@JsonProperty("c7")
	public int c7;
	@JsonProperty("c1")
	public Contratante contratantes;
	@JsonProperty("c2")
    public List<C2> datos;
	@JsonProperty("modelo")
	public int modelo=0;
	@JsonProperty("imagenSocio")
	public String imagenSocio="";
	@JsonProperty("imagenes")
	public List<Imagenes> imagenes;
	@Getter
	@Setter
	@ToString
	public static class C3 {
		
		@JsonProperty("c1")
		private String c1;
		@JsonProperty("c2")
		private String c2;		
		@JsonProperty("c3")
		private String c3;
		@JsonProperty("c4")
		private int c4;
		@JsonProperty("c5")
		private int c5;
		@JsonProperty("c6")
		private String c6;
		@JsonProperty("c7")
		private String c7;
		@JsonProperty("c8")
		private List<C8> pruebas;
		@JsonProperty("c9")
		private String c9;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class C8 {		
		private String c1;
	}
	
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class C4 {
		@JsonProperty("c3")
		public String c3;
		@JsonProperty("c4")
		public String c4;
		@JsonProperty("c5")
		public boolean c5;
		@JsonProperty("c6")
		public boolean c6;
		@JsonProperty("c10")
		public int c10;
		@JsonProperty("c7")
		public String c7;
		@JsonProperty("c8")
		public int c8;
		@JsonProperty("c9")
		public String c9;
		@JsonProperty("c1")
		public String c1;
		@JsonProperty("c2")
		public String c2;
	}
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class C5 {
		@JsonProperty("c1")
		private List<C1> documentos;
		@JsonProperty("c2")
		public documentolist2 c2;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class C1 {
		@JsonProperty("c3")
		public String c3;
		@JsonProperty("c4")
		public String c4;
		@JsonProperty("c5")
		public String c5;
		@JsonProperty("c10")
		public int c10;
		@JsonProperty("c7")
		public String c7;
		@JsonProperty("c13")
		public String c13;
		@JsonProperty("c8")
		public String c8;
		@JsonProperty("c12")
		public String c12;
		@JsonProperty("c9")
		public String c9;
		@JsonProperty("c14")
		public boolean c14;
		@JsonProperty("c1")
		public documentolistC1 dl;
		@JsonProperty("c2")
		public documentolistC2 dl2;
		
	}

	@Getter
	@Setter
	@ToString
	public static class documentolistC1 {
		@JsonProperty("c1")
		public String c1;
	}
	@Getter
	@Setter
	@ToString
	public static class documentolistC2 {
		@JsonProperty("c1")
		public String c1;
	}
	@Getter
	@Setter
	@ToString
	public static class documentolist2 {
		private String c3;
		private String c1;
		private String c2;
	}
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class C6 {
		public String c3;
		public String c4;
		public String c5;
		public String c11;
		public int c10;
		public String c7;
		public String c12;
		public String c1;
		public String c2;
	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public  static class Contratante {
		@JsonProperty("c3")
		private DatosGenerales datosGenerale;
		@JsonProperty("c4")
		public List<Monedas> moneda;
		@JsonProperty("c1")
		public List<Servicios> Servicio;
		@JsonProperty("c2")
		private Asegurados asegurados;
	}
 
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class DatosGenerales {
		public String c11;
		public String c10;
		public String c13;
		public String c12;
		public boolean siniestroRAM;
		public String c15;
		public int c14;
		public String c17;
		public int c16;
		public String c19;
		public String c18;
		public String c20;
		public String c22;
		public String c21;
		public String c24;
		public String c23;
		public String c26;
		public boolean c25;
		public int c28;
		public String c27;
		public String c1;
		public String c2;
		public int c3;
		public int c4;
		public String c5;
		public int c6;
		public String c7;
		public String c8;
		public String c9;
		public String tipoPago;

	}

	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Monedas {
		public String c1;
		public String c2;
	}
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Servicios {
		  public String c3;
		  public int c4;
		  public String c1;
		  public String c2;
	}
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Asegurados {
		  public String c13;
		    public int c15;
		    public String c14;
		    public String c17;
		    public String c16;
		    public String c19;
		    public String c18;
		    public String c1;
		    public String c2;
		    public String c3;
		    public String c4;
		    public int c5;
		    public String c6;
		    public boolean c7;
		    public int c8;
	}
	
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public  static class C2{
	    private boolean c3;
	    private String c4;
	    private boolean c5;
	    private boolean c6;
	    private String c7;
	    private String c1;
	    private boolean c2;
	}
	@Getter
	@Setter
	@ToString
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Imagenes {
		 public String nombre;
		 public String path;
	}

}
