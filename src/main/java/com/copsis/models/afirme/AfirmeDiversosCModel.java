package com.copsis.models.afirme;

import java.util.ArrayList;
import java.util.List;

import com.copsis.constants.ConstantsValue;
import com.copsis.models.DataToolsModel;
import com.copsis.models.EstructuraCoberturasModel;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.EstructuraRecibosModel;

public class AfirmeDiversosCModel {
    private DataToolsModel fn = new DataToolsModel();
    private EstructuraJsonModel modelo = new EstructuraJsonModel();
    
    
    public EstructuraJsonModel procesar(String contenido) {        
        StringBuilder newcontenido = new StringBuilder();
        contenido = fn.remplazarMultiple(contenido, fn.remplazosGenerales());
        int inicio = 0;
        int fin = 0;
        try {
            
            modelo.setTipo(7);            
            modelo.setCia(31);

            inicio = contenido.indexOf("PÓLIZA");
            fin =contenido.indexOf("ABREVIATURAS Y-O DEFINICIONES");
  
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            
            for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {
        

                if(newcontenido.toString().split("\n")[i].contains("PÓLIZA:")) {
                    modelo.setPoliza(newcontenido.toString().split("\n")[i].split("PÓLIZA:")[1].replace("###", "").trim());
                }

                if(newcontenido.toString().split("\n")[i].contains("MONEDA:")) {
                    modelo.setMoneda(fn.buscaMonedaEnTexto(newcontenido.toString().split("\n")[i]));
                    modelo.setPlan(newcontenido.toString().split("\n")[i+1].replace("###", ""));
                }
                if(newcontenido.toString().split("\n")[i].contains("Desde") && newcontenido.toString().split("\n")[i].contains("Hasta")){
                                                         
                  modelo.setVigenciaDe(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(0)));
                  modelo.setVigenciaA(fn.formatDateMonthCadena(fn.obtenVigePoliza(newcontenido.toString().split("\n")[i+1]).get(1)));      
                  modelo.setFechaEmision(modelo.getVigenciaDe());
                 }
                
                if(newcontenido.toString().split("\n")[i].contains("Contratante:")) {
                    modelo.setCteNombre(newcontenido.toString().split("\n")[i].split("Contratante:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("Domicilio") && newcontenido.toString().split("\n")[i].contains("Calle:")) {
                    modelo.setCteDireccion(newcontenido.toString().split("\n")[i].split("Calle:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("C.P:")) {
                    modelo.setCp(newcontenido.toString().split("\n")[i].split("C.P:")[1].replace("###", "").trim());
                }
                if(newcontenido.toString().split("\n")[i].contains("R.F.C:") && newcontenido.toString().split("\n")[i].contains("Correo")) {
                    modelo.setRfc(newcontenido.toString().split("\n")[i].split("R.F.C:")[1].split("Correo")[0].replace("###", "").trim());
                }
                if (newcontenido.toString().split("\n")[i].contains(ConstantsValue.PRIMA_NETA)) {
                    modelo.setPrimaneta( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[1])));
                    modelo.setRecargo( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[3])));
                    modelo.setDerecho( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[4])));
                    modelo.setIva( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[6])));                     
                    modelo.setPrimaTotal( fn.castBigDecimal(fn.castDouble(newcontenido.toString().split("\n")[i+1].split("###")[7])));
                    
                }
            }
            
            
         
            inicio = contenido.indexOf("Coberturas###Suma Asegurada");
            fin =contenido.indexOf("En testimonio de lo cual");

            newcontenido = new StringBuilder();
            newcontenido.append(fn.extracted(inicio, fin, contenido));
            List<EstructuraCoberturasModel> coberturas = new ArrayList<>();
            if( newcontenido.toString().split("\n").length> 5) {                
                for (int i = 0; i < newcontenido.toString().split("\n").length; i++) {  
                   
                    EstructuraCoberturasModel cobertura = new EstructuraCoberturasModel();
                    if(!newcontenido.toString().split("\n")[i].contains("Suma Asegurada") 
                            &&  !newcontenido.toString().split("\n")[i].contains("CONTENIDOS")
                            &&  !newcontenido.toString().split("\n")[i].contains("Cobertura Básica")
                            &&  !newcontenido.toString().split("\n")[i].contains("OTROS RIESGOS")
                            &&  !newcontenido.toString().split("\n")[i].contains("Seguros")
                            &&  !newcontenido.toString().split("\n")[i].contains("Hidalgo")
                            &&  !newcontenido.toString().split("\n")[i].contains("N.L.")
                            &&  !newcontenido.toString().split("\n")[i].contains("Dinero")
                           
                            ) {
                        
                        int sp  = newcontenido.toString().split("\n")[i].split("###").length;
                       switch (sp) {
                    case 1:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        coberturas.add(cobertura);
                        break;
                    case 3:
                        cobertura.setNombre(newcontenido.toString().split("\n")[i].split("###")[0]);
                        cobertura.setSa(newcontenido.toString().split("\n")[i].split("###")[1]);
                        coberturas.add(cobertura);
                        break;
                    default:
                        break;
                    }
                    }
                }
                modelo.setCoberturas(coberturas);
            }
            modelo.setFormaPago(1);
            
            List<EstructuraRecibosModel> recibos = new ArrayList<>();
            EstructuraRecibosModel recibo = new EstructuraRecibosModel();

            if (modelo.getFormaPago() == 1) {
                recibo.setReciboId("");
                recibo.setSerie("1/1");
                recibo.setVigenciaDe(modelo.getVigenciaDe());
                recibo.setVigenciaA(modelo.getVigenciaA());
                if (recibo.getVigenciaDe().length() > 0) {
                    recibo.setVencimiento(fn.dateAdd(recibo.getVigenciaDe(), 30, 1));
                }
                recibo.setPrimaneta(fn.castBigDecimal(modelo.getPrimaneta(), 2));
                recibo.setDerecho(fn.castBigDecimal(modelo.getDerecho(), 2));
                recibo.setRecargo(fn.castBigDecimal(modelo.getRecargo(), 2));
                recibo.setIva(fn.castBigDecimal(modelo.getIva(), 2));

                recibo.setPrimaTotal(fn.castBigDecimal(modelo.getPrimaTotal(), 2));
                recibo.setAjusteUno(fn.castBigDecimal(modelo.getAjusteUno(), 2));
                recibo.setAjusteDos(fn.castBigDecimal(modelo.getAjusteDos(), 2));
                recibo.setCargoExtra(fn.castBigDecimal(modelo.getCargoExtra(), 2));
                recibos.add(recibo);

            }

            modelo.setRecibos(recibos);
            
            return modelo;
        } catch (Exception ex) {
            modelo.setError(AfirmeDiversosCModel.this.getClass().getTypeName() + " - catch:" + ex.getMessage() + " | "
                    + ex.getCause());
            return modelo;
        }
        
    }
}
