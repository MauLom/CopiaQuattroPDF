 package com.copsis.models.impresionQuattrocrm;

    import java.awt.Color ;
import java.io.ByteArrayOutputStream ;
import java.io.File ;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument ;
import org.apache.pdfbox.pdmodel.PDPage ;
import org.apache.pdfbox.pdmodel.PDPageContentStream ;

import com.copsis.clients.projections.ConceptoSiniestrosProjection;
import com.copsis.clients.projections.DocumentoSiniestroProjection;
import com.copsis.clients.projections.SocioDirecProjection ;
import com.copsis.controllers.forms.ImpresionReclamacionForm;
import com.copsis.exceptions.GeneralServiceException ;
import com.copsis.models.Tabla.BaseTable ;
import com.copsis.models.Tabla.ImageUtils ;
import com.copsis.models.Tabla.Row ;
import com.copsis.models.Tabla.Sio4CommunsPdf ;


    public class ImpresionSiniestroReclamacion {

      
        private Color black = new Color(0, 0, 0);
        private Color gray = new Color(229, 234, 237);
        private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
        private float yStart = 780, bottomMargin = 30, fullWidth = 590,yStartNewPage=780, margin = 10;
        private Color grayCell = new Color(148, 166, 187);
        public float pPaginacion;
       
        

        public byte[] buildPDF(ImpresionReclamacionForm reclamacion) {
              float yStarpageNew=0;
              float ysposionfija;
              boolean acumula=false;
              boolean acumula2=false;
              boolean yTexto=false;

            try {
                

                ByteArrayOutputStream output;
                try ( PDDocument document = new PDDocument()) {
                    try {
                       
                        PDPage page = new PDPage();
                        document.addPage(page);
                        BaseTable table;
                        Row<PDPage> baseRow;
                       yStart = this.setEncabezado(document, page, reclamacion, false, false, false, false, false);
                        yStarpageNew = yStart;
                        table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 100, "DOCUMENTACION ADJUNTA", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), grayCell);
                        table.remoBordes(true, 1);             
                        table.draw();
                        
                        yStart -= table.getHeaderAndDataHeight();
                        ysposionfija = yStart;

                        List<DocumentoSiniestroProjection> documentoSiniestro = reclamacion.getDocumentoSiniestro();

                        int x=0;
                        int co=1;
                        int ct=0;
                    String logoCheck="https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2309/1N7rQflDvq65bN1u4E4VKFMSxdk5J1stSC7DxhViOGc7vz1H5gV6PXBNAlEmXhz/iconX.png";
                    String logoCheckF="https://storage.googleapis.com/quattrocrm-prod/quattro-biibiic/2309/1N7rQflDvq65bN1u4E4VKJABXzJ2yq1vERpw1yBLqQEePZDsiojdQAwCgVMyke/icon.png";
                        
                    if(!documentoSiniestro.isEmpty()){
                        while(x < documentoSiniestro.size()){                          
                            acumula=true;
                            if(x % 2 == 0){
                                table = new BaseTable(yStart, yStartNewPage, bottomMargin, 295, margin, document, page, false, true);
                                baseRow = communsPdf.setRow(table);
                                communsPdf.setCell(baseRow, 10, ImageUtils.readImage(documentoSiniestro.get(x).isStatus()  ? logoCheck: logoCheckF).scale(130, 130));
                                communsPdf.setCell(baseRow, 90, documentoSiniestro.get(x).getNombre(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white).setTopPadding(12);
                            }else{                                
                                table = new BaseTable(ysposionfija, yStartNewPage, bottomMargin, 295, 295, document, page, false, true);
                                baseRow = communsPdf.setRow(table);
                                communsPdf.setCell(baseRow, 10, ImageUtils.readImage(documentoSiniestro.get(x).isStatus()  ? logoCheck: logoCheckF).scale(130, 130));
                                communsPdf.setCell(baseRow, 90, documentoSiniestro.get(x).getNombre(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white).setTopPadding(12);
                           
                            }

                            if (isEndOfPage(table)) {
                                table.getRows().remove(table.getRows().size() - 1);
                                table.remoBordes(false, 1);
                                table.draw();
                                this.setEncabezado(document, page, reclamacion,true, false, false, false, false);
                                yStart = yStarpageNew;
                                ysposionfija = yStarpageNew - 15;
                                page = new PDPage();
                                document.addPage(page);
                                acumula=false;
                                break;
                            }else {
                                table.remoBordes(false,1);
                                table.draw();
    
                                if (x % 2 == 0) {
                                    yStart -= table.getHeaderAndDataHeight();
                                } else {
                                    ysposionfija -= table.getHeaderAndDataHeight();
                                }
                            }
                            ct++;
    
                            if (acumula) {
                                x++;
                                co++;
                            }
                            if (ct > 1000) {
                                table.remoBordes(false,1);
                                table.draw();
                                break;
                            }
    
                        }
                    }


                        if(yStart < 55){}else{        
                            yTexto=true;                
                        yStart = yStart-20;
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 12, "FACTURA", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 8, "FECHA", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 13, "RFC", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 47, "CONCEPTO", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 20, "IMPORTE", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        table.remoBordes(true, 1);             
                        table.draw();
                        yStart -= (table.getHeaderAndDataHeight());
                    
                        }


                        int i = 0;
                        int n = 1;
                        int d = 0;
                        int pt = 0;
                        List<ConceptoSiniestrosProjection> conceptos = reclamacion.getConceptoSiniestro();
                        if(!conceptos.isEmpty()){

                        

                        while(i < conceptos.size()){
                         
                          acumula2=true;
                        table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 12, conceptos.get(i).getFolio(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 8, conceptos.get(i).getFechFactura(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 13, conceptos.get(i).getRfc(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 47, conceptos.get(i).getConcepto(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 20, conceptos.get(i).getSumImporte(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                
                        
                          if (n == conceptos.size()) {
                            if(yStart < 55){
                                page = new PDPage();
                                document.addPage(page);
                                setEncabezado(document, page, reclamacion, false, true, true, true, true);
                            }else{
                              baseRow = communsPdf.setRow(table);
                              communsPdf.setCell(baseRow, 60, "", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                              communsPdf.setCell(baseRow, 20, "TOTAL RECLAMADO:", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                              communsPdf.setCell(baseRow, 20, reclamacion.getSiniestro().getTotalReclamado(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);                
                              baseRow = communsPdf.setRow(table);
                              communsPdf.setCell(baseRow, 60, "", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                              communsPdf.setCell(baseRow, 20, "TOTAL PROCEDENTE:", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                              communsPdf.setCell(baseRow, 20,conceptos.get(i).getProcedente(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);                              
                            }
                          }

                           if( isEndOfPage(table)){
                           
                            pt = i;
                            table.getRows().remove(table.getRows().size() - 1);
                            table.remoBordes(true,1);
                            table.draw();
                            page = new PDPage();
                            document.addPage(page);
                            d++;
                            if (d == 1) {
                                if (yTexto) {
                                    setEncabezado(document, page, reclamacion, false, true, true, false, false);
                                } else {
                                    setEncabezado(document, page, reclamacion,  false, true, false, false, false);
                                }
                            } else {
                                 setEncabezado(document, page, reclamacion, false, true, true, false, false);
                            }
                            acumula2 = false;
                           }

                           if (acumula2) {
                            table.remoBordes(true,1);
                            table.draw();
                            yStart -= table.getHeaderAndDataHeight();
                            i++;
                            n++;
                        } else {
                            if (pt > 0) {
                              
                                i = pt - 1;
                                i++;
                              
                                

                            } else {
                                i++;
                                n++;
                            }

                        }

                        if (i > 1000) {
                            table.remoBordes(true,1);
                            table.draw();
                            break;
                        }

                        }
                    }

                    Float poPaginacion = pPaginacion - 10;
                    int nume = document.getNumberOfPages();
                    int pos = document.getNumberOfPages() - 1;
                    String total = Integer.toString(nume);
                    for (int v = 0; v < nume; v++) {
                        PDPage page2 = document.getPage(v);
                        try (PDPageContentStream content = new PDPageContentStream(document, page2, PDPageContentStream.AppendMode.PREPEND, true, true)) {
                            int u = 1;
                            u += v;
                            String numeF = Integer.toString(u);

                            communsPdf.drawText(content, false, 282, poPaginacion, "PAG. " + numeF + "/" + total);

                        }
                    }
                      

                        

                        output = new ByteArrayOutputStream();
                        document.save(output);
                       //document.save(new File("/home/aalbanil/Vídeos/IMPRESIONCARACTULA/reclamncion.pdf"));
                        return output.toByteArray();
                    } finally {
                        document.close();
                    }
                }
            } catch (Exception ex) { 
               
                throw new GeneralServiceException("00001",
                        "Ocurrio un error en el servicio ImpresioncaratulaAutos: " + ex.getMessage());
            }

        }

        private float setEncabezado(PDDocument document, PDPage page, ImpresionReclamacionForm reclamacion,boolean headAdjuntos,
        boolean headerContiFacturas,boolean headeFactura,boolean headresult,boolean headerObservaciones ) {
            try ( PDPageContentStream conten = new PDPageContentStream(document, page)) {
                yStart = 780;
                BaseTable table;
                Row<PDPage> baseRow;
                SocioDirecProjection socio = reclamacion.getSocio();
                if (socio != null) {

                    table = new BaseTable(775, 775, bottomMargin, 150, 10, document, page, false, true);
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 100, ImageUtils.readImage(socio.getAvatar()).scale(130, 130));
                    table.draw();
                }
    
                String direccion =socio !=null ?  socio.getNombSocio() +" "+ socio.getCalle()+" "+ socio.getColonia() +" "+ socio.getEstado():"";

                table = new BaseTable(yStart, yStart, bottomMargin, 245, 180, document, page, false, true);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 100, direccion, black, false, "C", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), gray).setLineSpacing(1.2f);;
                table.draw();
                pPaginacion = (780 - table.getHeaderAndDataHeight());

                table = new BaseTable(yStart, yStart, bottomMargin, 155, 443, document, page, true, true);
                baseRow = communsPdf.setRow(table,10);
                communsPdf.setCell(baseRow, 60, "TRAMITE:", black, false, "L", 8, communsPdf.setLineStyle(grayCell), "", communsPdf.setPadding(3f), grayCell).setLeftPadding(10);                
                communsPdf.setCell(baseRow, 40, reclamacion.getSiniestro().getFechaCaptura(), black, false, "C", 8, communsPdf.setLineStyle(grayCell), "", communsPdf.setPadding(3f), grayCell);
                table.draw();

                yStart -= (table.getHeaderAndDataHeight());
                table = new BaseTable(yStart, yStart, bottomMargin, 155, 443, document, page, true, true);
                baseRow = communsPdf.setRow(table,10);
                communsPdf.setCell(baseRow, 100, "Reclamación", black, false, "C", 8, communsPdf.setLineStyle(gray,gray,Color.white,gray), "", communsPdf.setPadding(3f), Color.white);
                baseRow = communsPdf.setRow(table,10);
                communsPdf.setCell(baseRow, 100, reclamacion.getSiniestro().getFolio(), black, false, "C", 8, communsPdf.setLineStyle(gray,gray,Color.white,gray), "", communsPdf.setPadding(3f), Color.white);
                 baseRow = communsPdf.setRow(table,10);
                communsPdf.setCell(baseRow, 100, "Complemento", black, false, "C", 8, communsPdf.setLineStyle(gray,gray,Color.white,gray), "", communsPdf.setPadding(3f), Color.white);
                baseRow = communsPdf.setRow(table,10);
                communsPdf.setCell(baseRow, 100, reclamacion.getSiniestro().getTramite()+"", black, false, "C", 8, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(3f), Color.white);
                
                table.draw();


                 yStart -= table.getHeaderAndDataHeight()+15;

                table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 49, "COMPAÑIA", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), grayCell);
                communsPdf.setCell(baseRow,2, "", black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(3f), Color.white);
                communsPdf.setCell(baseRow, 49, "AFECTADO", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), grayCell);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 49, reclamacion.getSiniestro().getAseguradora(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow,2, "", black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(3f), Color.white);
                communsPdf.setCell(baseRow, 49,reclamacion.getSiniestro().getAsegurado(), black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 49, reclamacion.getSiniestro().getCveAgente(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow,2, "", black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(3f), Color.white);
                communsPdf.setCell(baseRow, 25,"NACIMIENTO:    "+reclamacion.getSiniestro().getFechaNacimiento(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow, 24,"EDAD:   "+reclamacion.getSiniestro().getEdad()+"", black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 49,"", black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow,2, "", black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(3f), Color.white);
                communsPdf.setCell(baseRow, 49,"PÓLIZA:  "+ reclamacion.getSiniestro().getNoPoliza(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);              
               
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 49, "CONTRATANTE", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), grayCell);
                communsPdf.setCell(baseRow,2, "", black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(3f), Color.white);
                communsPdf.setCell(baseRow, 49, "", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 49,reclamacion.getSiniestro().getNomCliente(), black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow,2, "", black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(3f), Color.white);
                communsPdf.setCell(baseRow, 49, reclamacion.getSiniestro().getCertificado(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);              
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 49,reclamacion.getSiniestro().getCteDirecion(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow,2, "", black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(3f), Color.white);
                communsPdf.setCell(baseRow, 49, reclamacion.getSiniestro().getParentesco(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow, 49, "", black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
               
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 25,"RFC:"+reclamacion.getSiniestro().getRfc(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow, 24,"TEL:"+"", black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow,2, "", black, false, "C", 7, communsPdf.setLineStyle(Color.white,Color.white,Color.white,Color.white), "", communsPdf.setPadding(3f), Color.white);                
                communsPdf.setCell(baseRow, 49, "TITULAR:  " +reclamacion.getSiniestro().getNomTitular(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
               

                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 50, "PADECIMIENTO", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), grayCell);
                communsPdf.setCell(baseRow, 50, "CHEQUE A NOMBRE DE", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), grayCell);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 50,reclamacion.getSiniestro().getPadecimiento(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                communsPdf.setCell(baseRow, 50,reclamacion.getSiniestro().getAseguradoraCuenta(), black, false, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);             
                table.remoBordes(true, 1);             
                table.draw();

                yStart -= table.getHeaderAndDataHeight()+10;

                if(headAdjuntos){
                table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 100, "DOCUMENTACION ADJUNTA", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), grayCell);
                table.remoBordes(true, 1);             
                table.draw();
                yStart -= table.getHeaderAndDataHeight();
               
                }

                if(headerContiFacturas){
                table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 100, "CONTINUACION DEL LISTADO DE FACTURAS", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), grayCell);
                table.remoBordes(true, 1);             
                table.draw();
                yStart -= table.getHeaderAndDataHeight();
               
                }

                if (headeFactura) {
                   table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 12, "FACTURA", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 8, "FECHA", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 13, "RFC", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 47, "CONCEPTO", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 20, "IMPORTE", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
              
                    table.remoBordes(true,1);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight());
    
                }


                if (headresult) {
                       table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 58, "", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 20, "TOTAL RECLAMADO", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 22, reclamacion.getSiniestro().getTotalReclamado() , black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
              
                    table.remoBordes(true,1);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight());  
                }


                if (headerObservaciones) {
                       table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 49, "OBSERVACIONES", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 1, "TOTAL RECLAMADO", black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 50, "FIRMA" , black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
              
                    table.remoBordes(true,1);
                    table.draw();
                    yStart -= (table.getHeaderAndDataHeight());  
                }

                  table = new BaseTable(25, 30, 5, fullWidth, margin, document, page, false, true);
                        baseRow = communsPdf.setRow(table);
                        communsPdf.setCell(baseRow, 33, reclamacion.getSiniestro().getUsuario(), black, true, "L", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 33, "Powered by <b>quattroCRM</b>", black, true, "C", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
                        communsPdf.setCell(baseRow, 33, reclamacion.getSiniestro().getUsuarioFecha() , black, true, "R", 7, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), Color.white);
              table.draw();

             return yStart;
            } catch (Exception ex) {
               
                throw new GeneralServiceException("00001",
                        "Ocurrio un error en el servicio setEncabezado: " + ex.getMessage());
            }
        }


    private boolean isEndOfPage(BaseTable table) {
      
        float currentY = yStart - table.getHeaderAndDataHeight();   
     
        return  currentY <= bottomMargin;
    }

    
    }