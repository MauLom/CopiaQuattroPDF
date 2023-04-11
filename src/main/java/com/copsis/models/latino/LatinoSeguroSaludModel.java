package com.copsis.models.latino;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class LatinoSeguroSaludModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        String cbo = contenido;
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales())
                .replace("día-mes-año:", "");
        try {
            modelo.setTipo(3);
            modelo.setCia(21);

            inicio = contenido.indexOf("DATOS DE LA PÓLIZA");
            fin = contenido.indexOf("DATOS DEL ASEGURADO");

            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                if (newcontenido.toString().split("\n")[i].contains("Póliza:")
                        && newcontenido.toString().split("\n")[i].contains("Operación:")
                        && newcontenido.toString().split("\n")[i].contains("Plan de seguro:")) {
                    modelo.setPoliza(newcontenido.toString().split("\n")[i].split("Póliza:")[1].split("Operación")[0]
                            .replace("###", "").trim());
                    modelo.setPlan(
                            newcontenido.toString().split("\n")[i].split("seguro:")[1].replace("###", "").trim());
                }
                if (newcontenido.toString().split("\n")[i].contains("Desde")
                        && newcontenido.toString().split("\n")[i].contains("Hasta")) {
                    modelo.setVigenciaDe(fn
                            .formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
                    modelo.setVigenciaA(fn
                            .formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
                    modelo.setFechaEmision(modelo.getVigenciaDe());
                }
                if (newcontenido.toString().split("\n")[i].contains("CONTRATANTE")
                        && newcontenido.toString().split("\n")[i + 1].contains("Nombre:")) {
                    modelo.setCteNombre(
                            newcontenido.toString().split("\n")[i + 1].split("Nombre:")[1].replace("###", "").trim());
                }

                if (newcontenido.toString().split("\n")[i].contains("Domicilio:")) {
                    modelo.setCteDireccion(
                            newcontenido.toString().split("\n")[i].replace("###", "").replace("Domicilio:", "").trim()
                                    + " " + newcontenido.toString().split("\n")[i + 1].replace("###", "").trim() + " "
                                    + newcontenido.toString().split("\n")[i + 2].replace("###", "").trim());
                }
                if (newcontenido.toString().split("\n")[i].contains("C.P.")) {
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P.")[1].trim().substring(0, 5));
                }
                if (newcontenido.toString().split("\n")[i].contains("R.F.C:")
                        && newcontenido.toString().split("\n")[i].contains("Número")) {
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Número")[0]
                            .replace("###", "").replace("-", "").trim());
                }

            }
            inicio = contenido.indexOf("DATOS DEL ASEGURADO");
            fin = contenido.indexOf("COBERTURAS");
            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", "")
                    .replace("1.0", "###").replace("1.1", "").replace("1.2", "").replace("1.3", "").replace("1.4", "")
                    .replace("1.5", ""));

            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                if (newcontenido.toString().split("\n")[i].split("-").length > 3) {
                    asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[1].trim());
                    if (newcontenido.toString().split("\n")[i].split("###")[2].length() > 3) {
                        asegurado.setSexo(
                                fn.sexo(newcontenido.toString().split("\n")[i].split("###")[3].trim()) ? 1 : 0);
                        asegurado.setParentesco(
                                fn.findParentesco(newcontenido.toString().split("\n")[i].split("###")[2].trim()));
                        asegurado.setFechaAlta(fn.formatDateMonthCadena(
                                fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
                        asegurado.setAntiguedad(fn.formatDateMonthCadena(
                                fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
                    } else {
                        asegurado.setSexo(
                                fn.sexo(newcontenido.toString().split("\n")[i].split("###")[2].trim()) ? 1 : 0);
                        asegurado.setParentesco(1);
                        asegurado.setFechaAlta(fn.formatDateMonthCadena(
                                fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(0)));
                        asegurado.setAntiguedad(fn.formatDateMonthCadena(
                                fn.obtenVigePoliza(newcontenido.toString().split("\n")[i]).get(1)));
                    }
                    asegurados.add(asegurado);
                }
            }
            modelo.setAsegurados(asegurados);

            inicio = cbo.indexOf("COBERTURAS");
            fin = cbo.indexOf("Eje Central Lázaro");

            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            String[] arrContenido = fn.extracted(inicio, fin, cbo).split("\n");

            for (String x : arrContenido) {
                if (!x.contains("BENEFICIOS") && !x.contains("Básica") && !x.contains("Concepto")
                        && !x.contains("ZONA") && !x.contains("Adicionales")) {
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    int sp = x.split("###").length;
                    switch (sp) {
                        case 1:
                            cobertura.setNombre(x.split("###")[0]);
                            coberturas.add(cobertura);
                            break;
                        case 2:
                            cobertura.setNombre(x.split("###")[0]);
                            cobertura.setSa(x.split("###")[1].replace("\r", ""));
                            coberturas.add(cobertura);
                            break;
                    }
                }

            }
            modelo.setCoberturas(coberturas);


       
            	
			inicio = contenido.indexOf("PRIMA DEL SEGURO");
			fin = contenido.indexOf("Se acompaña a la");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
        
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {					
              
				if(newcontenido.toString().split("\n")[i].contains("Prima neta") && newcontenido.toString().split("\n")[i].contains("I.V.A:")) {
					 List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                    
					  modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));	       		  
		       		  modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(1))));   
				}	
                if(newcontenido.toString().split("\n")[i].contains("Recargo") 
                && newcontenido.toString().split("\n")[i].contains("I.V.A")) {
                    List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);                   
                        modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                     
               }	               
               if(newcontenido.toString().split("\n")[i].contains("expedición") 
               && newcontenido.toString().split("\n")[i].contains("Total")) {
                   List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);                                         
                       modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));                   		  
                       modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));   
              }
              if(newcontenido.toString().split("\n")[i].contains("Periodo de pago:") ){
               modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
              }
              if(newcontenido.toString().split("\n")[i].contains("Moneda:") ){
                modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
              }
			}


            inicio = contenido.indexOf("Agente:");
			fin = contenido.indexOf("Clave Promotor:");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
        	for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {		               
                if( newcontenido.toString().split("\n")[i].contains("Agente:")){
                    modelo.setAgente( newcontenido.toString().split("\n")[i].split("Agente:")[1].split("Clave:")[0].trim());
                    modelo.setCveAgente(fn.obtenerListNumeros2( newcontenido.toString().split("\n")[i]).get(0));

                }
            }

            return modelo;
        } catch (Exception e) {

            return modelo;
        }
    }

 

}
