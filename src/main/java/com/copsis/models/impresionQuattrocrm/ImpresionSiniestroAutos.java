package com.copsis.models.impresionQuattrocrm;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import com.copsis.clients.projections.DocumentoSiniestroProjection;
import com.copsis.clients.projections.SocioDirecProjection;
import com.copsis.controllers.forms.ImpresionSiniestroAForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionSiniestroAutos {

    private Color black = new Color(0, 0, 0);
    private Color gray = new Color(229, 234, 237);
    private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
    private float yStart = 780, bottomMargin = 30, fullWidth = 582, yStartNewPage = 780, margin = 14;
    private Color grayCell = new Color(148, 166, 187);
    private Color blue = new Color(40, 76, 113);

    public byte[] buildPDF(ImpresionSiniestroAForm  sienstroAutos) {
        float yStarpageNew = 0;
        float ysposionfija;
        boolean acumula = false;
        boolean acumula2 = false;
        boolean yTexto = false;

        try {
            ByteArrayOutputStream output;
            try (PDDocument document = new PDDocument()) {
                try {
                         PDPage page = new PDPage();
                        document.addPage(page);
                        BaseTable table;
                        Row<PDPage> baseRow;
                        yStart = this.setEncabezado(document, page, sienstroAutos,false);

                        table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 60, "Siniestro", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                        communsPdf.setCell(baseRow, 40, "Montos", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f),gray);   
                        if(sienstroAutos.getContrantante().getModelo() == 1 || sienstroAutos.getContrantante().getModelo() == 2 ) {                                                                       
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Descripción", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,sienstroAutos.getSiniestroAProjection().getDescripcion(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Reclamado:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getReclamado(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Tipo de Siniestro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,tipoSiniestro(sienstroAutos.getSiniestroAProjection().getTipo()), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Procedente:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getProcendente(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Fecha del Siniestro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,sienstroAutos.getSiniestroAProjection().getFechRegitro(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Coaseguro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getCoaseguro(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Fecha del Reporte:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,sienstroAutos.getSiniestroAProjection().getFechReporte(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Deducible:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getDeducible(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Fecha del Registro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,sienstroAutos.getSiniestroAProjection().getFechSiniestro(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Pagado:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getPagado(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        } else{

                         baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Tipo de Siniestro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Reclamado:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getReclamado(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Fecha del Siniestro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,sienstroAutos.getSiniestroAProjection().getFechRegitro(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Procedente:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getProcendente(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Fecha del Reporte:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,sienstroAutos.getSiniestroAProjection().getFechReporte(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Coaseguro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getCoaseguro(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                
                         baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Fecha del Registro:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,sienstroAutos.getSiniestroAProjection().getFechRegitro(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Deducible:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getDeducible(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                
                                    
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 12,"Pagado:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
                        communsPdf.setCell(baseRow, 28,sienstroAutos.getSiniestroAProjection().getPagado(), blue, false, "R", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);
        
                        }
                        
                        table.remoBordes(true, 1);
                        table.draw();

                        yStart -=table.getHeaderAndDataHeight()+10;

                    
                       yStart = this.setDatos(document, page, sienstroAutos,yStart);

                     
                
                        table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 60, "Documentación", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                        table.remoBordes(true, 1);
                        table.draw();


                        yStart -= table.getHeaderAndDataHeight();
                        ysposionfija = yStart;

                        List<DocumentoSiniestroProjection> documentoSiniestro = sienstroAutos.getDocumentoSiniestro();

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
                                this.setEncabezado(document, page, sienstroAutos,false);
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

                        yStart -=table.getHeaderAndDataHeight()+10;
                        table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 60, "Descripción del Siniestro", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                        table.remoBordes(true, 1);
                        table.draw();

                        yStart -= table.getHeaderAndDataHeight();

                        if (sienstroAutos.getContrantante() != null && sienstroAutos.getSiniestroAProjection().getObservacion().length() > 0) {

                            String dato = Sio4CommunsPdf.eliminaHtmlTags3(sienstroAutos.getSiniestroAProjection().getObservacion());
                            String datos[] = dato.split("<br>|<br/>|</br>");

                            int y = 0;
                            while (y < datos.length) {
                                acumula = true;
                                table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, false, true);
                                baseRow = communsPdf.setRow(table, 13);
                                communsPdf.setCell(baseRow, 100, Sio4CommunsPdf.eliminaHtmlTags3(datos[y]), black, false, "L", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(3f), gray);
                                if (isEndOfPage(table)) {
                                    table.getRows().remove(table.getRows().size() - 1);
                                    table.draw();
                                    page = new PDPage();
                                    document.addPage(page);
                                     this.setEncabezado(document, page, sienstroAutos,true);
                                    acumula = false;
                                } else {
                                    table.remoBordes(true, 1);
                                    table.draw();
                                    yStart -= table.getHeaderAndDataHeight();
                                }
                                if (acumula) {
                                    y++;
                                }
                                if (y > 100) {
                                    table.draw();
                                    break;

                                }
                            }
                        }




                    output = new ByteArrayOutputStream();
                    document.save(output);
                    //document.save(new File("/home/aalbanil/Vídeos/IMPRESIONCARACTULA/SINIESTRO.pdf"));
                    return output.toByteArray();
                } finally {
                    document.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new GeneralServiceException("00001",
                    "Ocurrio un error en el servicio ImpresioncaratulaAutos: " + ex.getMessage());
        }

    }


    private float setEncabezado(PDDocument document, PDPage page, ImpresionSiniestroAForm  siniestroAuto,boolean descripcion ) {
            try ( PDPageContentStream conten = new PDPageContentStream(document, page)) {
                yStart = 780;
                BaseTable table;
                Row<PDPage> baseRow;
                SocioDirecProjection socio = siniestroAuto.getSocio();
                if (socio != null) {

                    table = new BaseTable(778, 778, bottomMargin, 150, 10, document, page, false, true);
                    baseRow = communsPdf.setRow(table, 12);
                    communsPdf.setCell(baseRow, 100, ImageUtils.readImage(socio.getAvatar()).scale(130, 130));
                    table.draw();
                }
    
                String direccion =socio !=null ?  socio.getNombSocio() +" "+ socio.getCalle()+" "+ socio.getColonia() +" "+ socio.getEstado():"";

                table = new BaseTable(yStart, yStart, bottomMargin, 245, 180, document, page, false, true);
                baseRow = communsPdf.setRow(table);
                communsPdf.setCell(baseRow, 100, direccion, black, false, "C", 9, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(4f), gray).setLineSpacing(1.2f);;
                table.draw();
               

                table = new BaseTable(yStart, yStart, bottomMargin, 133, 462, document, page, true, true);
                baseRow = communsPdf.setRow(table,12);
                communsPdf.setCell(baseRow, 100, "No.Siniestro", black, false, "C",11, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(3f), gray).setLeftPadding(10);                
                 baseRow = communsPdf.setRow(table,12);
                communsPdf.setCell(baseRow, 100,siniestroAuto.getSiniestroAProjection().getNoSiniestro() , black, false, "C", 11, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(3f), Color.white);
                baseRow = communsPdf.setRow(table,12);
                communsPdf.setCell(baseRow, 100, "Orden", black, false, "C", 11, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(3f), gray).setLeftPadding(10);                
                baseRow = communsPdf.setRow(table,12);
                communsPdf.setCell(baseRow, 100, "", black, false, "C", 11, communsPdf.setLineStyle(gray), "", communsPdf.setPadding(3f),  Color.white);
                table.setCellCallH(true);
                table.draw();

               

                 yStart -= table.getHeaderAndDataHeight()+20;

                 table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                 baseRow = communsPdf.setRow(table,12);
                 communsPdf.setCell(baseRow, 42, "Contratante", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                 communsPdf.setCell(baseRow, 38, "Movimiento", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f),gray);                                            
                 baseRow = communsPdf.setRow(table,16);
                 communsPdf.setCell(baseRow, 42,siniestroAuto.getContrantante().getContrantante(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 14,"Póliza:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 58,siniestroAuto.getContrantante().getNoPoliza(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);
                
                 baseRow = communsPdf.setRow(table,12);
                 communsPdf.setCell(baseRow, 42,siniestroAuto.getContrantante().getRfc(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 14,"Vigencia:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 44,siniestroAuto.getContrantante().getVigencia(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);
         
                 baseRow = communsPdf.setRow(table,12);
                 communsPdf.setCell(baseRow, 42,siniestroAuto.getContrantante().getCteCalle(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 14,"Emisión:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 44,siniestroAuto.getContrantante().getFechaEmision(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);
                 baseRow = communsPdf.setRow(table,12);
                 communsPdf.setCell(baseRow, 42,siniestroAuto.getContrantante().getCteColonia(), blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 14,"Subramo:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 44,siniestroAuto.getContrantante().getSubRamo(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);
                 baseRow = communsPdf.setRow(table,12);
                 communsPdf.setCell(baseRow, 42,"", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);                  
                 communsPdf.setCell(baseRow, 14,"Forma de Pago:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);
                 communsPdf.setCell(baseRow, 44,siniestroAuto.getContrantante().getFormaPago() , blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);
                 baseRow = communsPdf.setRow(table,12);
                 communsPdf.setCell(baseRow, 42,"", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white); 
                 communsPdf.setCell(baseRow, 14,"Moneda", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);                  
                 communsPdf.setCell(baseRow, 44,siniestroAuto.getContrantante().getMoneda(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(4f), Color.white);                 
                 table.remoBordes(true, 1);
                 table.draw();

                 yStart -=table.getHeaderAndDataHeight()+10;
                 table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                 baseRow = communsPdf.setRow(table,14);
                 communsPdf.setCell(baseRow, 100, "Agente:", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                 
                 baseRow = communsPdf.setRow(table,12);
                 communsPdf.setCell(baseRow, 14,"Aseguradora:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 80,siniestroAuto.getContrantante().getAseguradora(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                  
                 baseRow = communsPdf.setRow(table,12);
                 communsPdf.setCell(baseRow, 14,"Clave:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                 communsPdf.setCell(baseRow, 80,siniestroAuto.getContrantante().getClaveAngente(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                                   
                 table.remoBordes(true, 1);
                table.draw();
                   yStart -=table.getHeaderAndDataHeight()+5;

                   if(descripcion){
                       table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 60, "Descripción del Siniestro", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                        table.remoBordes(true, 1);
                        table.draw();
                        yStart -=table.getHeaderAndDataHeight()+5;
                   }
                
             return yStart;
            } catch (Exception ex) {
               
                throw new GeneralServiceException("00001",
                        "Ocurrio un error en el servicio setEncabezado: " + ex.getMessage());
            }
        }


       private float setDatos(PDDocument document, PDPage page, ImpresionSiniestroAForm  siniestroAuto ,Float yStartIn) {
            try {
                yStart= yStartIn;
                  BaseTable table;
                 Row<PDPage> baseRow;
                
                      table = new BaseTable(yStart, yStart, bottomMargin, fullWidth, margin, document, page, true, true);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 60, "Datos de Contacto", black, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), gray);
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Asegurado:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,siniestroAuto.getContacto().getNombAsegurado() !=null ? siniestroAuto.getContacto().getNombAsegurado():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                    
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Correo:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,siniestroAuto.getContacto().getCorreo(), blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                    
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Celular:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,siniestroAuto.getContacto().getCelular() !=null ? siniestroAuto.getContacto().getCelular():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                    
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Nombre en Cuenta:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,siniestroAuto.getContacto().getNoCuenta() !=null ? siniestroAuto.getContacto().getNoCuenta():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                    
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Cuenta:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,siniestroAuto.getContacto().getCuenta() !=null ? siniestroAuto.getContacto().getCuenta():"" , blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                    
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Banco:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,siniestroAuto.getContacto().getBanco() !=null ? siniestroAuto.getContacto().getBanco():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Clave:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,siniestroAuto.getContacto().getClabe() !=null ? siniestroAuto.getContacto().getClabe():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                    
                        baseRow = communsPdf.setRow(table,14);
                        communsPdf.setCell(baseRow, 17,"Referencia:", blue, true, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white); 
                        communsPdf.setCell(baseRow, 43,siniestroAuto.getContacto().getReferencia() !=null ? siniestroAuto.getContacto().getReferencia():"", blue, false, "L", 9,communsPdf.setLineStyle(gray),"", communsPdf.setPadding(3f), Color.white);                    
                        table.remoBordes(true, 1);
                        table.draw();

                    yStart -=table.getHeaderAndDataHeight()+10;
                
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

    public String tipoSiniestro(Integer tipo) {
        String result = "";
        switch (tipo) {
            case 18:
                result = "ATROPELLAMIENTO";
                break;
            case 19:
                result = "INTENTO DE ROBO";
                break;
            case 20:
                result = "AJUSTE EXPRES";
                break;
            case 21:
                result = "INUNDACIÓN";
                break;
            case 22:
                result = "ROBO PARCIAL";
                break;
            case 23:
                result = "CAIDA DE OBJETOS";
                break;
            case 24:
                result = "ALCANCE (DAR)";
                break;
            case 25:
                result = "VOLCADURA";
                break;
            case 26:
                result = "AUTOPISTA";
                break;
            case 27:
                result = "VUELTA INDEBIDA";
                break;
            case 28:
                result = "FALTA DE PERICIA";
                break;
            case 29:
                result = "COLISION CON OBJETO FIJO";
                break;
            case 50:
                result = "ACCIDENTE";
                break;
            case 51:
            case 10:
                result = "COLISIÓN/VUELCO";
                break;
            case 52:
            case 11:
                result = "INCENDIO / RAYO / EXPLOSIÓN";
                break;
            case 53:
            case 12:
                result = "FHM";
                break;
            case 54:
            case 13:
                result = "TERREMOTO";
                break;
            case 55:
            case 14:
                result = "ROBO";
                break;
            case 56:
            case 15:
                result = "ASISTENCIA";
                break;
            case 58:
            case 17:
                result = "CRISTALES";
                break;
            case 59:
                result = "EQUIPO ELECTRONICO";
                break;
            case 60:
                result = "ROTURA DE MAQUINARIA";
                break;
            case 61:
                result = "ANUNCIOS LUMINOSOS";
                break;
            case 62:
                result = "DINERO Y VALORES";
                break;
            case 63:
                result = "RESPONSABILIDAD CIVIL";
                break;
            case 67:
            case 16:
                result = "OTRO";
                break;

        }
        return result;
    }


}
