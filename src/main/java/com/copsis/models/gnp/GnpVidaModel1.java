package com.copsis.models.gnp;

import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraBeneficiariosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class GnpVidaModel1 {
    private DataToolsModel fn = new DataToolsModel();
	private EstructuraJsonModel modelo = new EstructuraJsonModel();
	
    public  EstructuraJsonModel procesar (String contenido){
        int inicio = 0;
		int fin = 0;
		StringBuilder newcontenido = new StringBuilder();
		StringBuilder newdire = new StringBuilder();
		contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(5);
			modelo.setCia(18);
            

			inicio = contenido.indexOf("Domicilio del Asegurado");
			fin = contenido.indexOf("Beneficios");					
			newcontenido.append( fn.extracted(inicio, fin, contenido));

            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {                              
                if(newcontenido.toString().split("\n")[i].contains("Domicilio")){
                   modelo.setCteNombre(newcontenido.toString().split("\n")[i+1].split("###")[0]);     
                }
                if(newcontenido.toString().split("\n")[i].contains("R.F.C")){
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C.")[1].trim());
                    modelo.setPoliza(newcontenido.toString().split("\n")[i-1]);

                     List<String> cp = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i-2]);
                     if(!cp.isEmpty()){
                       modelo.setCp(cp.get(0));
                     }
                }
                if(newcontenido.toString().split("\n")[i].contains("Número de")){
                 modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Número")[0].replace("###", ""));
                }

                if(newcontenido.toString().split("\n")[i].contains("Nacimiento")
                && newcontenido.toString().split("\n")[i].contains("Plan")
                && newcontenido.toString().split("\n")[i].contains("Moneda")){
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i+1]));
                    modelo.setPlan(newcontenido.toString().split("\n")[i+1].split("###")[1]);
                }

            }

      
            inicio = contenido.indexOf("Beneficios");
			fin = contenido.indexOf("Fecha de Inicio");					
			newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {   
                EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                if(!newcontenido.toString().split("\n")[i].contains("Beneficios")
                 && !newcontenido.toString().split("\n")[i].contains("Supervivencia")
                 && !newcontenido.toString().split("\n")[i].contains("Fallecimiento")){
                 
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
            modelo.setFormaPago(1);

           
            inicio = contenido.indexOf("Fecha de Inicio");
			fin = contenido.indexOf("Beneficiarios");					
			newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
                if(newcontenido.toString().split("\n")[i].split("-").length > 3){
                    List<String> valores = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[i]);
                     modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));
                     modelo.setVigenciaA(fn.calcvigenciaA(modelo.getVigenciaDe(), 12));
                     modelo.setFechaEmision(modelo.getVigenciaDe());
                     List<String> prima = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i].replace(",", ""));
                     if(!prima.isEmpty() && prima.size()== 5){
                     modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(prima.get(4))));
                     modelo.setPrimaneta(modelo.getPrimaTotal());
                     }
                }
            }

            
            inicio = contenido.indexOf("Beneficiarios");
			fin = contenido.indexOf("Grupo Nacional");					
			newcontenido = new StringBuilder();
            newcontenido.append( fn.extracted(inicio, fin, contenido));
            List<EstructuraBeneficiariosModel> beneficiarios = new ArrayList<>();
              for (int i = 0; i < newcontenido.toString().split("\n").length; i++) { 
                EstructuraBeneficiariosModel beneficiario = new EstructuraBeneficiariosModel();
                if( newcontenido.toString().split("\n")[i].contains("%")){
                    beneficiario.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                    beneficiario.setParentesco(fn.parentesco(newcontenido.toString().split("\n")[i].split("###")[1]));
                    beneficiario.setPorcentaje(fn.castInteger(fn.obtenerListNumeros2(newcontenido.toString().split("\n")[i] ).get(0)));
                    beneficiarios.add(beneficiario);

                }              
            }
            modelo.setBeneficiarios(beneficiarios);

            return modelo;
        } catch (Exception e) {
            modelo.setError(GnpVidaModel1.this.getClass().getTypeName()+ " | "+e.getMessage()+" | "+e.getCause());
            return modelo; 
        }
    
    }
}
