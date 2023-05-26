package com.copsis.models.prevem;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class PrevemSaludModel {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
	public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();	
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
   try {
            modelo.setTipo(3);
			modelo.setCia(86);
           
	
			inicio = contenido.indexOf("Tipo Documento#");
			fin = contenido.indexOf("Prima Neta");			
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));	
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
             
              if(newcontenido.toString().split("\n")[i].contains("Póliza No.")){
              modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza No.")[1].replace("###", ""));
              }
              if(newcontenido.toString().split("\n")[i].contains("Contratante:") && newcontenido.toString().split("\n")[i].contains("R.F.C.")){
               modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Contratante:")[1].split("R.F.C.")[0].replace("###", ""));
               modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].replace("###", ""));
              }
              if(newcontenido.toString().split("\n")[i].contains("Domicilio:") ){
                modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Domicilio:")[1].replace("###", " ").trim());
              
                modelo.setCp(fn.obtenerCPRegex2(modelo.getCteDireccion()));
              }

              if(newcontenido.toString().split("\n")[i].contains("Vigencia: Desde")  && 
                newcontenido.toString().split("\n")[i].contains("Agente") &&
                newcontenido.toString().split("\n")[i+1].contains("Hasta")   ){
                 modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i]).get(0)));
                 modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza( newcontenido.toString().split("\n")[i+1]).get(0)));
                  modelo.setFechaEmision(modelo.getVigenciaDe());
                    if(newcontenido.toString().split("\n")[i+1].split("###").length == 5){
                        modelo.setCveAgente(newcontenido.toString().split("\n")[i+1].split("###")[2]);
                        modelo.setAgente(newcontenido.toString().split("\n")[i+1].split("###")[3]);
                    }
                }
                if(newcontenido.toString().split("\n")[i].contains("Forma de Pago")){
                  modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                  modelo.setMoneda(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                }

            }
          
            inicio = contenido.indexOf("Prima Neta");
			fin = contenido.indexOf("Prevem Seguros S.A. de C.V");	
            fin = fin == -1 ? contenido.indexOf("Prevem Seguros S.A.") : fin;

            newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));	
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {           
                if(newcontenido.toString().split("\n")[i].contains("Prima Neta")){
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i+1]);
                    modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                    modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(2))));
                    modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(3))));
                    modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(4))));
                }
            
            }
            
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
         
            inicio = contenido.indexOf("LISTADO DE ASEGURADOS");
			fin = contenido.indexOf("COBERTURAS###SUMA ASEGURADA");	
      
            newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));	
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
           
            EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
              if(newcontenido.toString().split("\n")[i].split("-").length > 3){
              
                asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                asegurado.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[1]));
                asegurado.setSexo(fn.sexo(newcontenido.toString().split("\n")[i].split("###")[2]).booleanValue() ? 1 : 0);
               
               asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[3]));
               asegurado.setEdad(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[4]));
               asegurado.setAntiguedad(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[5]));
                asegurados.add(asegurado);
              }
            }
            modelo.setAsegurados(asegurados);

            inicio = contenido.indexOf("COBERTURAS ADICIONALES");
			fin = contenido.indexOf("Días de espera 3");	
       
      
            newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido));	

            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();             
                if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS")){
                    switch(newcontenido.toString().split("\n")[i].split("###").length){
                        case 2:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                        break;
                        case 3:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                        break;

                    }

                }
            }
            modelo.setCoberturas(coberturas);


            if(modelo.getCoberturas().isEmpty()){
                inicio = contenido.indexOf("COBERTURAS###ESCALA###SUMA ASEGURADA");
                fin = contenido.indexOf("ENDOSOS EN ESTA PÓLIZA");	
                newcontenido = new StringBuilder();		
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("###B### ", ""));	

                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();             
                    if(!newcontenido.toString().split("\n")[i].contains("COBERTURAS")){
                        
                        switch(newcontenido.toString().split("\n")[i].split("###").length){
                            case 3:
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
                            cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
                            coberturas.add(cobertura);
                            break;
                            case 4:
                            cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[1]);
                            cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[2]);
                            cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[3]);
                            coberturas.add(cobertura);
                            break;

                        }

                    }
                }
                modelo.setCoberturas(coberturas);
            }

    return modelo;   
   } catch (Exception ex) {
    modelo.setError(PrevemSaludModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
    return modelo;
   }
    }
}
