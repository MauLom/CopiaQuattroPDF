package com.copsis.models.axa;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.axa.salud.AxaSaludFaModel;
import com.copsis.models.axa.salud.AxaSaludModel;
import com.copsis.models.axa.salud.AxaSaludV2Model;
import com.copsis.models.axa.salud.AxaSaludtresModel;

public class AxaModel {
	// Clases
	private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	// Variables
	private PDFTextStripper stripper;
	private PDDocument doc;
	private String contenido;
	private boolean div0;
	// Constructor
	public AxaModel(PDFTextStripper pdfStripper, PDDocument pdDoc, String contenido) {
		this.stripper = pdfStripper;
		this.doc = pdDoc;
		this.contenido = contenido;

	}

	public EstructuraJsonModel procesa() {
		try {


			if ((contenido.contains("Datos del vehículo")) && !contenido.contains(" Vehicle description") || contenido.contains("DATOS DEL VEHÍCULO")) { // AUTOS
				modelo  = new AxaAutosModel(fn.caratula(1, 2, stripper, doc),fn.textoBusqueda(stripper, doc, "RECIBO PROVISIONAL DE", false)).procesar();
			
			}else if( contenido.contains("Datos del Vehículo") && contenido.contains("AUTOMÓVILES/AUTO  COLECTIVA")  && !contenido.contains(" Vehicle description")) {
				modelo = new AxaAutos3Model(fn.caratula(1, 4, stripper, doc)).procesar();
			
			}
			else if (contenido.indexOf("Recibo provisional para pago de primas") > -1) {
				modelo = new AxaSaludModel(fn.caratula(1, 3, stripper, doc)).procesar();
		
			}
			else if ( !contenido.contains("Gastos Médicos Mayores Individual") && (contenido.contains("familiar") && contenido.contains("póliza"))) {

				modelo = new AxaSaludFaModel().procesar(fn.caratula(1, 3, stripper, doc));
		
			}else if ( fn.caratula(1, 3, stripper, doc).contains("Gastos Médicos Mayores")
			 && fn.caratula(1, 3, stripper, doc).contains("Familiar")) {

				modelo = new AxaSaludModel(fn.caratula(1, 3, stripper, doc)).procesar();
				if(modelo.getVigenciaA().isEmpty() && modelo.getVigenciaDe().isEmpty() && modelo.getMoneda() ==0 && modelo.getFormaPago() ==0){
					modelo = new AxaSaludtresModel().procesar(fn.caratula(1, 3, stripper, doc));
				}
		
			}
			else if(fn.caratula(1, 3, stripper, doc).contains("Gastos Médicos Mayores Individual")){
			AxaSaludV2Model datosAxa2Salud = new AxaSaludV2Model(fn.caratula(1, 3, stripper, doc));
								modelo = datosAxa2Salud.procesar();
			}


			else {

				String[] tipos = { "PAQUETE DE SEGURO EMPRESARIAL", "GASTOS M", "TRADICIONALES DE VIDA",
				ConstantsValue.VIDA_PROTGT ,ConstantsValue.VIDA_INDIVIDUAL,
					ConstantsValue.VIDA_ACADEMICO,ConstantsValue.ALIADOS_KIDS,
						"HOGAR INTEGRAL", "VEHICLE DESCRIPTION", "PROTECCIÓN A BIENES EMPRESARIALES",
						ConstantsValue.PLANPROTEGE_COMERCIO,
						"RESPONSABILIDAD CIVIL, COMERCIO",ConstantsValue.PLANPROTEGE_COMERCIO,"DAÑOS","PLANPROTEGE / CONSTRUCTORES", "RESPONSABILIDAD CIVIL, ERRORES"};
				contenido = contenido.toUpperCase();

				for (String tipo : tipos) {	
					
					if (contenido.contains(tipo)) {	
								
						switch (tipo) {
							
						case "TRADICIONALES DE VIDA": case ConstantsValue.VIDA_PROTGT: case ConstantsValue.VIDA_INDIVIDUAL: case ConstantsValue.VIDA_ACADEMICO: case ConstantsValue.ALIADOS_KIDS: // VIDA
							if(tipo.equals(ConstantsValue.VIDA_PROTGT) || tipo.equals(ConstantsValue.VIDA_INDIVIDUAL) || tipo.equals(ConstantsValue.VIDA_ACADEMICO) || tipo.equals(ConstantsValue.ALIADOS_KIDS)) {
								
								AxaVida2Model datosAxaVida = new AxaVida2Model(fn.caratula(1, 4, stripper, doc));
								modelo = datosAxaVida.procesar();
							}else {
								
								AxaVidaModel datosAxaVida = new AxaVidaModel(fn.caratula(1, 3, stripper, doc));
								modelo = datosAxaVida.procesar();	
							}											
							break;
						case "VEHICLE DESCRIPTION":
							
							AxaAutos2Model datosAxaAutos = new AxaAutos2Model(fn.caratula(1, 4, stripper, doc));
							modelo = datosAxaAutos.procesar();
							break;
						case "GASTOS M": // GASTOS MEDICOS --/(Se usara identificar la version 2 del pdf)Datos del
											// contratante
							if (contenido.contains("DATOS DEL CONTRATANTE")) {
						
								AxaSaludV2Model datosAxa2Salud = new AxaSaludV2Model(fn.caratula(1, 3, stripper, doc));
								modelo = datosAxa2Salud.procesar();
							} else {
				
								AxaSaludModel datosAxaSalud = new AxaSaludModel(fn.caratula(1, 3, stripper, doc));
								modelo = datosAxaSalud.procesar();
							}
							break;
						case "HOGAR INTEGRAL":
						case "PLANPROTEGE / CONSTRUCTORES":
						case ConstantsValue.PLANPROTEGE_COMERCIO:
				
						// HOGAR
							 int pagFinal = doc.getNumberOfPages() > 5 ? doc.getNumberOfPages() :4;
							 if(fn.caratula(1, pagFinal, stripper, doc).contains("Insured's data")){
								modelo = new AxaDiversos3Model().procesar(fn.caratula(1, pagFinal, stripper, doc));
								div0 = true;
							 }else{														
								AxaDiversosModel datosAxaDiversos = new AxaDiversosModel(fn.caratula(1, pagFinal, stripper, doc));
								modelo = datosAxaDiversos.procesar();
								div0 = true;
							 }
							break;
						   case "RESPONSABILIDAD CIVIL, COMERCIO":// HOGAR
						   case "PROTECCIÓN A BIENES EMPRESARIALES":
						   case "RESPONSABILIDAD CIVIL, ERRORES":
						   case "DAÑOS":
						   int pagFin = doc.getNumberOfPages() > 3 ? 6 :4;
						 
						   
							if(fn.caratula(1, pagFin, stripper, doc).contains("Equipo de Contratistas y  Maquinaria")){
								modelo  = new AxaDiversos4Model().procesar(fn.caratula(2, pagFin, stripper, doc));
								

							}else if(!fn.caratula(1, pagFin, stripper, doc).contains("Vehicle description") && (!div0)) {									
							 AxaDiversos2Model datosAxaDive = new AxaDiversos2Model(fn.caratula(1, pagFin, stripper, doc));
								modelo = datosAxaDive.procesar();
							   
							}
								break;
							default: 
								break;
						}
					
					}
				}
			}

			return modelo;
		} catch (Exception ex) {
			modelo.setError(
					AxaModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
			return modelo;
		}

	}

}
