package com.copsis.models.banorte;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class BanorteAutosBModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        int inicio = 0;
        int fin = 0;
        StringBuilder newcontenido = new StringBuilder();
        StringBuilder domicilio = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        

        try {
            modelo.setTipo(1);
            modelo.setCia(35);
          
            inicio = contenido.indexOf("NÚMERO DE SOLICITUD");
		       	fin = contenido.indexOf("DATOS DEL ASEGURADO");
            newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));
            	for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                            
                if(newcontenido.toString().split("\n")[i].contains("No. de Solicitud#")){
                  modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[0]);
                }

              }



            inicio = contenido.indexOf("DATOS DEL ASEGURADO");
		       	fin = contenido.indexOf("@DATOS DEL VEHÍCULO");
          
			
			newcontenido.append( fn.extracted(inicio, fin, contenido).replace("día-mes-año: ", ""));
			
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                
                if(newcontenido.toString().split("\n")[i].contains("Nombre del Contratante:")
                 && newcontenido.toString().split("\n")[i].contains(ConstantsValue.RFC)){
                   modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Contratante:")[1].split(ConstantsValue.RFC)[0].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("domicilio del Asegurado:")
                && newcontenido.toString().split("\n")[i].contains(ConstantsValue.RFC)){
                 
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split(ConstantsValue.RFC)[1].replace("###", "").trim());
               }

               if(newcontenido.toString().split("\n")[i].contains("Calle y Número:")){
                domicilio.append( newcontenido.toString().split("\n")[i].split(ConstantsValue.NUMEROPM)[1].replace("###", "").trim() );
              }
              if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.CALLE_NO)){
                domicilio.append( newcontenido.toString().split("\n")[i].split(ConstantsValue.CALLE_NO)[1].replace("###", "").trim() );
              }
              if(newcontenido.toString().split("\n")[i].contains("Colonia:") &&newcontenido.toString().split("\n")[i].contains("Tel:")){
                domicilio.append( newcontenido.toString().split("\n")[i].split("Colonia:")[1].split("Tel:")[0].replace("###", " ").trim() );
              }
              if(newcontenido.toString().split("\n")[i].contains("Población-Municipio:")){
               domicilio.append(" " +newcontenido.toString().split("\n")[i].split("Municipio:")[1].replace("###", " ").trim() );
              }
              if(newcontenido.toString().split("\n")[i].contains("No. Póliza")){
                modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[0]);
              }
              if(newcontenido.toString().split("\n")[i].contains("C.P:")){
            
                modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].replace("###", "").trim().substring(0, 5));
              }
              
              if(newcontenido.toString().split("\n")[i].contains("Fecha de emisión:") && newcontenido.toString().split("\n")[i].contains("Prima neta:")){              
                List<String> valores = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]);
                modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
                List<String> x = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(x.get(0))));
              
              }
                if(newcontenido.toString().split("\n")[i].contains("Fecha de emisión:") && newcontenido.toString().split("\n")[i].contains("Prima Neta:")){              
                List<String> valores = fn.obtenVigeCpl(newcontenido.toString().split("\n")[i]);
                modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));
                List<String> x = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(x.get(0))));
              
              }
              if(newcontenido.toString().split("\n")[i].contains("Inicio de vigencia:") && newcontenido.toString().split("\n")[i].contains("Reducción")){
                List<String> valores = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
              }
               if(newcontenido.toString().split("\n")[i].contains("Inicio Vigencia:") && newcontenido.toString().split("\n")[i].contains("Reducción")){
                List<String> valores = fn.obtenVigeCpl(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
              }
              if(newcontenido.toString().split("\n")[i].contains("Fin de vigencia:") && newcontenido.toString().split("\n")[i].contains("Recargo")){
                List<String> valores = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)));
                 List<String> x = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setRecargo(fn.castBigDecimal(fn.castDouble(x.get(0))));
              }
               if(newcontenido.toString().split("\n")[i].contains("Fin Vigencia:") && newcontenido.toString().split("\n")[i].contains("Recargo")){
                List<String> valores = fn.obtenVigeCpl(newcontenido.toString().split("\n")[i]);
                modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)));
                 List<String> x = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setRecargo(fn.castBigDecimal(fn.castDouble(x.get(0))));
              }

              if(newcontenido.toString().split("\n")[i].contains("Moneda") && newcontenido.toString().split("\n")[i].contains("Derecho")){
                modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
              }
           
              if(newcontenido.toString().split("\n")[i].contains("Forma de pago:") && newcontenido.toString().split("\n")[i].contains("Impuesto")){             
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
               
              }
              if(newcontenido.toString().split("\n")[i].contains("Forma de pago:") && newcontenido.toString().split("\n")[i].contains("I.V.A:")){             
                modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
               
              }
              if(newcontenido.toString().split("\n")[i].contains("Prima total:") || newcontenido.toString().split("\n")[i].contains("Prima Total:")){
                List<String> valores = fn.obtenerListNumeros(newcontenido.toString().split("\n")[i]);
                modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));             
              }                            
            }

            modelo.setCteDireccion(domicilio.toString().replace("Estado:", ""));

            inicio = contenido.indexOf("DATOS DEL VEHÍCULO");
			fin = contenido.indexOf("Referencia:");
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
			boolean descripcion=true;
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {            
                if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.DESCRIPCION) && descripcion){               
                   modelo.setDescripcion(newcontenido.toString().split("\n")[i].split(ConstantsValue.DESCRIPCION)[1].replace("###", "").replace(":", "").trim());              
                   descripcion=false;
                  }
                if(newcontenido.toString().split("\n")[i].contains("Marca:") && newcontenido.toString().split("\n")[i].contains("Capacidad:")
                 && newcontenido.toString().split("\n")[i].contains("Modelo:") && newcontenido.toString().split("\n")[i].contains("Transmisión:")){
                    modelo.setMarca(newcontenido.toString().split("\n")[i].split("Marca:")[1].split("Capacidad:")[0].replace("###", ""));     
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("Modelo:")[1]);                   
                    modelo.setModelo(fn.castInteger(valores.get(0)));                    
                 }
                 if(newcontenido.toString().split("\n")[i].contains("Placas") && newcontenido.toString().split("\n")[i].contains(ConstantsValue.SERIE)){                    
                    modelo.setPlacas(newcontenido.toString().split("\n")[i].split("Placas:")[1].split(ConstantsValue.SERIE)[0].replace("###", "").trim());                         
                    modelo.setSerie(newcontenido.toString().split("\n")[i].split(ConstantsValue.SERIE)[1].replace("###", "").trim());                    
                 }
                 if(newcontenido.toString().split("\n")[i].contains("Motor:")){
                    modelo.setMotor(newcontenido.toString().split("\n")[i].split("Motor:")[1].replace("###", "").trim());
                 }
            }
         
      inicio = contenido.indexOf("DETALLE DE COBERTURAS");
			fin = contenido.indexOf("La Compañía podrá");
      fin = fin==-1 ? contenido.indexOf("La Unidad de Medida"):fin;

           
			newcontenido = new StringBuilder();
			newcontenido.append( fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
			for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(newcontenido.toString().split("\n")[i].contains(ConstantsValue.PAQUETE)){
                 modelo.setPlan(newcontenido.toString().split("\n")[i].split(ConstantsValue.PAQUETE)[1].replace("###", "").replace(":", "").trim());
                }
                if(!newcontenido.toString().split("\n")[i].contains("PAQUETE:")
                && !newcontenido.toString().split("\n")[i].contains("COBERTURAS")
                && !newcontenido.toString().split("\n")[i].contains("Coberturas")
                && !newcontenido.toString().split("\n")[i].contains("Deducible")
                && newcontenido.toString().split("\n")[i].split("###").length > 3){                   
                    cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                    cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                    cobertura.setDeducible(newcontenido.toString().split("\n")[i].split("###")[2]);
                    coberturas.add( cobertura);                                                                
                }

            }
            modelo.setCoberturas(coberturas);

            return modelo;
        } catch (Exception ex) {        
            modelo.setError(
                    BanorteAutosBModel.this.getClass().getTypeName() + " | " + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }

    }

}
