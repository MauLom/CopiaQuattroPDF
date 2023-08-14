package com.copsis.models.atlas;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class AtlasVida2Model {

    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    public EstructuraJsonModel procesar(String contenido) {
        try {
            int inicio = 0;
			int fin = 0;
            StringBuilder newcontenido = new StringBuilder();			
			contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());

            modelo.setTipo(5);			
			modelo.setCia(33);
            inicio = contenido.indexOf("Nombre y domicilio");
            fin =contenido.indexOf("Coberturas y Primas");
            
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                  
                if(newcontenido.toString().split("\n")[i].contains("Nombre") && newcontenido.toString().split("\n")[i].contains("Póliza-Endoso")){
                   modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);
                   modelo.setPoliza(newcontenido.toString().split("\n")[i+1].split("###")[2]);

                }
                if(newcontenido.toString().split("\n")[i].contains("Fecha Expedición")){
                    String x  =  newcontenido.toString().split("\n")[i+1];
                    if(x.contains("De:") && x.contains("A:")){
                        x =x.split("De:")[1].split("A:")[0].replace(" ", "");
                        List<String> valores = fn.obtenVigePoliza(x);
                         if(!valores.isEmpty()){
                            modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                            modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
                            modelo.setFechaEmision(modelo.getVigenciaDe());
                         }
                    }
                }

                if(newcontenido.toString().split("\n")[i].contains("Información Genera")){
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Información")[0]);
                }
                if(newcontenido.toString().split("\n")[i].contains("Moneda:") && newcontenido.toString().split("\n")[i].contains("Forma Pago:")){
                    modelo.setFormaPago(fn.formaPagoSring(newcontenido.toString().split("\n")[i]));
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                }
                 if(newcontenido.toString().split("\n")[i].contains("CP")
                  && newcontenido.toString().split("\n")[i].contains("RFC")
                  && newcontenido.toString().split("\n")[i].contains("Trámite:")){
                  List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("RFC")[0]);
                    if(!valores.isEmpty()){
                        modelo.setCp(valores.get(0));
                    }
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("RFC")[1].split("Trámite")[0].replace("###", ""));						
                 }

                  if(newcontenido.toString().split("\n")[i].contains("Agente:")){
                     List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].split("Agente")[1]);
                     System.out.println(valores);
                    if(!valores.isEmpty()){
                      modelo.setAgente(valores.get(0));  
                    }
                  }
              
            }

            newcontenido = new StringBuilder();
            inicio = contenido.indexOf("Asegurado");
            fin =contenido.indexOf("Coberturas y Primas");
            List<EstructuraAseguradosModel> asegurados = new ArrayList<>();            
            newcontenido.append(fn.extracted(inicio, fin, contenido).replace("Nombre###", "").replace("F.Nac:###", "").replace("Edad", ""));

             for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
                EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                if(newcontenido.toString().split("\n")[i].split("-").length > 2){
                    asegurado.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                    asegurado.setNacimiento(fn.formatDateMonthCadena(newcontenido.toString().split("\n")[i].split("###")[1]));
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i]);
                    
                    asegurado.setEdad(fn.castInteger(valores.get(0)));
                    asegurados.add(asegurado);

                }
                  
            }
            modelo.setAsegurados(asegurados);

            
            newcontenido = new StringBuilder();
            
            inicio = contenido.indexOf("Coberturas y Primas");
            fin =contenido.indexOf("Beneficiarios");
          
             newcontenido.append(fn.extracted(inicio, fin, contenido));
             List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                
                 EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(!newcontenido.toString().split("\n")[i].contains("Coberturas")
                 && !newcontenido.toString().split("\n")[i].contains("Plan:")
                 && !newcontenido.toString().split("\n")[i].contains("Cobertura")
                 && !newcontenido.toString().split("\n")[i].contains("Pago adicional")){
                    
                    switch(newcontenido.toString().split("\n")[i].split("###").length){
                        case 4:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                        break;
                    }                   
                }            
            }
            modelo.setCoberturas(coberturas);
            //


        
            inicio = contenido.indexOf("Beneficiarios");
            fin =contenido.indexOf("Pago inmediato");
            
            newcontenido = new StringBuilder();          
            newcontenido.append(fn.extracted(inicio, fin, contenido).replace("____EN PARTES IGUALES. A", "")
            .replace("HIJAS", "###HIJAS##")
            .replace("ESPOSA___", "###ESPOSA###"));  
            
            List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();          
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
                EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
              
                 if(!newcontenido.toString().split("\n")[i].contains("FALLECIMIENTO")
                 && !newcontenido.toString().split("\n")[i].contains("Beneficiarios")){
                  
                    switch(newcontenido.toString().split("\n")[i].split("###").length){
                        case 1:
                        beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);                        
                        beneficiarios.add(beneficiario);
                        break;
                        case 2:
                        beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        beneficiario.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i]));                        
                        beneficiarios.add(beneficiario);
                        break;
                        case 3:
                        beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        beneficiario.setParentesco(fn.buscaParentesco(newcontenido.toString().split("\n")[i]));
                        beneficiario.setPorcentaje(fn.castInteger(newcontenido.toString().split("\n")[i].split("###")[2].replace("%", "") ) );
                        beneficiarios.add(beneficiario);
                        break;
                    }
            
                }

            }
            modelo.setBeneficiarios(beneficiarios);



            inicio = contenido.indexOf("Prima Neta:");
            fin =contenido.indexOf("#Recargo Pago");            
            newcontenido = new StringBuilder();          
            newcontenido.append(fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
                if( newcontenido.toString().split("\n")[i].contains("Prima Neta")){
                    List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                   
						if(!valores.isEmpty()){ 
                            modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(0))));
                            modelo.setPrimaTotal(modelo.getPrimaneta());
                               
                        }

                }
              
            }


            
            
            return modelo;
        } catch (Exception ex) {		modelo.setError(
            AtlasVida2Model.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | " + ex.getCause());
            return modelo;
        }
    }

}
