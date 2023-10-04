package com.copsis.models.hdi;
import java.util.ArrayList;
import java.util.List;

import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraAseguradosModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;

public class HdiSaludModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();

    public EstructuraJsonModel procesar(String contenido) {
        StringBuilder  newcontenido  = new StringBuilder();;
        int inicio = 0;
        int fin = 0;

        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        try {
            modelo.setTipo(3);
            modelo.setCia(14);

            inicio = contenido.indexOf("Contratante:");
			fin  = contenido.indexOf("Nombre y fecha");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();

            for(int x =0; x < newcontenido.toString().split("\n").length; x++){               
                if(newcontenido.toString().split("\n")[x].contains("Contratante:")){
                   modelo.setCteNombre(newcontenido.toString().split("\n")[x].split("Contratante:")[1].replace("###", ""));
                }
                 if(newcontenido.toString().split("\n")[x].contains("R.F.C:")){
                    modelo.setRfc(newcontenido.toString().split("\n")[x].split("R.F.C:")[1].replace("###", ""));
                 }

                 if(newcontenido.toString().split("\n")[x].contains("Dirección:")){
                      modelo.setCteDireccion(newcontenido.toString().split("\n")[x].split("Dirección:")[1].replace("###", "")
                      +" " +newcontenido.toString().split("\n")[x+1].replace("###", ""));
                 }

                 if(newcontenido.toString().split("\n")[x].contains("Número de Póliza:")){
                     modelo.setPoliza(newcontenido.toString().split("\n")[x].split("Póliza")[1].split("###")[1].replace("###", ""));
                 }

                 if(newcontenido.toString().split("\n")[x].contains("Vigencia:")){
                    List<String> valores = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[x]);
                    modelo.setVigenciaDe(fn.formatDateMonthCadena(valores.get(0)));                   
                 }

                  if(newcontenido.toString().split("\n")[x].contains("Hasta:")){
                    List<String> valores = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[x]);
                    modelo.setVigenciaA(fn.formatDateMonthCadena(valores.get(0)));                   
                 }
                 if(newcontenido.toString().split("\n")[x].contains(" Emisión:")){
                    List<String> valores = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[x]);
                    modelo.setFechaEmision(fn.formatDateMonthCadena(valores.get(0)));                   
                 }

                if(newcontenido.toString().split("\n")[x].contains("Coaseguro:") && newcontenido.toString().split("\n")[x].contains("Tope de Coaseguro:")){
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    cobertura.setCoaseguro(newcontenido.toString().split("\n")[x].split("Coaseguro:")[1].split("Tope")[0].replace("###", ""));
                    cobertura.setCoaseguroTope(newcontenido.toString().split("\n")[x].split("Tope de Coaseguro:")[1].replace("###", ""));
                    coberturas.add(cobertura);
                }

               

                 

            } 
            modelo.setCoberturas(coberturas);


              if(modelo.getCteDireccion().contains("C.P.")){
            List<String> valores = fn.obtenerListNumeros2(modelo.getCteDireccion().split("C.P.")[1]);
						if(!valores.isEmpty()){
							modelo.setCp(valores.get(0));
						}
                    }
            

          
            inicio = contenido.indexOf("Nombre y fecha");
			fin  = contenido.indexOf("Deducible contratado:");	
			newcontenido = new StringBuilder();
			newcontenido.append(fn.extracted(inicio, fin, contenido));
List<EstructuraAseguradosModel> asegurados = new ArrayList<>();
               for(int x =0; x < newcontenido.toString().split("\n").length; x++){                                            
                if (!newcontenido.toString().split("\n")[x].contains("Mes") && newcontenido.toString().split("\n")[x].contains("-")) {
                    EstructuraAseguradosModel asegurado = new EstructuraAseguradosModel();
                      List<String> fecha = fn.obtenVigePoliza2(newcontenido.toString().split("\n")[x]);
                    asegurado.setNombre(newcontenido.toString().split("\n")[x].split("###")[0]);
                    if(!fecha.isEmpty()){
                       asegurado.setNacimiento(fn.formatDateMonthCadena(fecha.get(0)));
                    }
                    asegurados.add(asegurado);
                    

						List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[x].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setPrimaneta(fn.castBigDecimal(fn.castDouble(valores.get(2))));
						}						
					}
					if (newcontenido.toString().split("\n")[x].contains("Derecho de Póliza")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[x].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setDerecho(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}						
					}

                    if (newcontenido.toString().split("\n")[x].contains("Recargo Pago")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[x]);
						if(!valores.isEmpty()){
							modelo.setRecargo(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}						
					}

                     if (newcontenido.toString().split("\n")[x].contains("IVA")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[x].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setIva(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}						
					}
              
                     if (newcontenido.toString().split("\n")[x].contains("Total")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[x].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setPrimaTotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}						
					}


                     if (newcontenido.toString().split("\n")[x].contains("Primer Recibo")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[x].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setPrimerPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}						
					}


                     if (newcontenido.toString().split("\n")[x].contains("Pagos Subsecuente")) {
						List<String> valores = fn.obtenerListNumeros2(newcontenido.toString().split("\n")[x].replace(",", ""));
						if(!valores.isEmpty()){
							modelo.setSubPrimatotal(fn.castBigDecimal(fn.castDouble(valores.get(0))));
						}						
					}
              
            }
            modelo.setAsegurados(asegurados);

            modelo.setMoneda(1);
            modelo.setFormaPago(1);

            return modelo;
        } catch (Exception ex) {
            modelo.setError(HdiSaludModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }

    }
}
