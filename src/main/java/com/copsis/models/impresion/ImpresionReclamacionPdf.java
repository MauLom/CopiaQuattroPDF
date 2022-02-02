package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.json.JSONObject;

import com.copsis.clients.projections.ImpresionReclamacionProjection;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;
import com.copsis.models.Tabla.VerticalAlignment;

public class ImpresionReclamacionPdf {
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private Color black = new Color(0, 0, 0);
	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color bgColorA = new Color(0, 0, 143);
	private float yStartNewPage = 760, yStart = 760, bottomMargin = 26;
	private float fullWidth = 542;
	public byte[] buildPDF(ImpresionReclamacionProjection impresionReclamacionProjection) {
		ByteArrayOutputStream output;
		try {
			try (PDDocument document = new PDDocument()) {
				try {
				
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					
					boolean acomula = false;
					boolean acomula2 = false;
					boolean acomula3 = false;
					boolean acomula4 = false;
				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 7, "Póliza:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 15, "0245784512", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 6, "Inciso:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 6, "0001", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 18, "3", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Contratante:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 69, "(71) Pedro Montalvo Veloz", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 12, "quattroCRM:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 5, "23", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Concepto:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 70, "KIA FORTE LX MT 4P 4CIL 2021", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 15, "Administrador QuattroCRM", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 12, "Vigencia:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 70, "25 feb 2021 - 25 feb 2022", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);					
					communsPdf.setCell(baseRow, 15, "RESPONSABLE", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					table.draw();
					
					yStart -= table.getHeaderAndDataHeight();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
					
					yStart -= table.getHeaderAndDataHeight() +5;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 65, "(713) (204-1-1) KIA FORTE LX MT 4P 4CIL", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Captura:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "09 dic 2021 09:40 AM", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15, "Conductor:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 50, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Siniestro:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "S/A", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15, "Modelo:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 10, "Nomina:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 19, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Reporte:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "S/A", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15, "No. de Serie:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 50, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Promesa Pago:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "S/A", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 8, "Placas:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 12, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 8, "Clave:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 12, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 8, "Valor:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 17, "", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 14, "Pago Real:", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 21, "S/A", black, false, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
				
					
					table.draw();
					
	                 yStart -= table.getHeaderAndDataHeight()+5;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
				    yStart -= table.getHeaderAndDataHeight()+5;
				    
				    JSONObject documentos = new JSONObject("{'c2':[{'c3':true,'c4':'Articulo 142','c5':false,'c6':false,'c7':'','c1':'qQ3WdYReTFgKnSHabEg1Tw==','c2':true},{'c3':false,'c4':'DOCUMENTOS PRUEBA...','c5':false,'c6':false,'c7':'','c1':'ukosJtaVNf5hcJP+Zrt5YA==','c2':false},{'c3':false,'c4':'DOCUMENTO PARA PRUEBA','c5':false,'c6':false,'c7':'','c1':'btFd6cFUhf/wR/Eq6P4L/A==','c2':false},{'c3':false,'c4':'PARTE DE ACCIDENTE','c5':false,'c6':false,'c7':'','c1':'zXQUkvKsL75HXYjLXZdG6Q==','c2':false}]}");
				  
				    
				    
				    
				    table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Documentos", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					int i =0;
					while(i < documentos.getJSONArray("c2").length()) {
						acomula = true;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
						baseRow = communsPdf.setRow(table, 15);
					    	
						communsPdf.setCell(baseRow, 4, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2201/Siniestro/2201/EbyNLChPxCfJOgraG8wVpAZX61q8AqZVGfcWXBQLkYVMSSUrPJvjYYBKTWUSEHiq/1.png"), i, i, bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 40, documentos.getJSONArray("c2").getJSONObject(i).getString("c4").toString(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						if(isEndOfPage(table)) {
							table.getRows().remove(table.getRows().size()-1);
							table.draw();
							// CRACION NUEVA PAGINA
							page = new PDPage();
							document.addPage(page);
							acomula = false;
							
						}else {
							table.draw();
							yStart -= table.getHeaderAndDataHeight();
						}
						
						if(acomula) {								
							i++;
						} 
					
						
					}
					
					
					yStart -= table.getHeaderAndDataHeight()+5;
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
					yStart -= table.getHeaderAndDataHeight()+5;
					
					 JSONObject facturas = new JSONObject("{'c5':{'c1':[{'c3':'237','c4':'qW7fOx+5Ca6JAzGDa7Uk3w==','c5':'ISRAEL DAVID PEREZ MORENO','c10':187,'c7':'40000.0000','c13':'0.0000','c8':'0.0000','c12':'Honorarios médicos  paciente: AILYN SOLEIDY MONTES CONCHA','c9':'40000.0000','c14':false,'c1':{},'c2':{}},{'c3':'185712','c4':'cbKTTDgshkTjCqRL8H8vXA==','c5':'CLINICA DE MERIDA S.A DE C.V','c10':186,'c7':'3078.6300','c13':'0.0000','c8':'492.5800','c12':'CUARTO RESIDENCIAL/SUMINISTROS MEDICOS','c9':'3571.2100','c14':false,'c1':{},'c2':{}}]}}");
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Facturas", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 70, "FacturaEmisor/Concepto", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 15, "Reclamado", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					communsPdf.setCell(baseRow, 15, "Procedente", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
					
					int t =0;
					while(t < facturas.getJSONObject("c5").getJSONArray("c1").length()) {
						acomula2= true;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
						baseRow = communsPdf.setRow(table, 10);					
						communsPdf.setCell(baseRow, 70, facturas.getJSONObject("c5").getJSONArray("c1").getJSONObject(t).getString("c3").toString() +"   " +facturas.getJSONObject("c5").getJSONArray("c1").getJSONObject(t).getString("c5").toString(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 15, facturas.getJSONObject("c5").getJSONArray("c1").getJSONObject(t).getString("c9").toString(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 15, facturas.getJSONObject("c5").getJSONArray("c1").getJSONObject(t).getString("c13").toString(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 10);
						communsPdf.setCell(baseRow, 90, facturas.getJSONObject("c5").getJSONArray("c1").getJSONObject(t).getString("c12").toString(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						
						if(isEndOfPage(table)) {
							table.getRows().remove(table.getRows().size()-1);
							table.draw();
							// CRACION NUEVA PAGINA
							page = new PDPage();
							document.addPage(page);
							acomula2 = false;
							
						}else {
							table.draw();
							yStart -= table.getHeaderAndDataHeight();
						}
						
						if(acomula2) {								
							t++;
						} 
						
					}
					

					yStart -= table.getHeaderAndDataHeight()+5;
		
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();								
					yStart -= table.getHeaderAndDataHeight()+5;
					
					
					
					 JSONObject historial = new JSONObject("{'c3':[{'c3':'TRAMITE','c4':1,'c5':1,'c6':'12','c7':'','c8':[{'c1':'12'}],'c9':'49','c1':'J3UKgFvZ81zrwx+Cgf4Ffg==','c2':'ADM - 09-12-2021 09:42:47 AM'},{'c3':'VALUACION','c4':2,'c5':3,'c6':'12','c7':'','c8':[{'c1':'12'}],'c9':'0','c1':'BJfaGT7Xbzox/yHy/VoNmQ==','c2':'ADM - 09-12-2021 09:42:36 AM'},{'c3':'REPORTE','c4':1,'c5':3,'c6':'12','c7':'Inicio de Captura','c8':[],'c9':'0','c1':'H2Q3/0QfpMD4x/ufRxGObA==','c2':'ADM - 09-12-2021 09:40:44 AM'}]}");
					
				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Historial", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight()+7;
					
					int x =0;
					while(x < historial.getJSONArray("c3").length()) {
						acomula3 = true;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 50, document, page, false,true);
						baseRow = communsPdf.setRow(table, 15);
					    	
						communsPdf.setCell(baseRow, 10, historial.getJSONArray("c3").getJSONObject(x).getString("c9").toString(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40, historial.getJSONArray("c3").getJSONObject(x).getString("c2").toString(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40, historial.getJSONArray("c3").getJSONObject(x).getString("c3").toString(), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 15, "Observaciones:", black, true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40, "PRUEBA", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 12);
						communsPdf.setCell(baseRow, 6, "Folio:", black, true, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						communsPdf.setCell(baseRow, 40, "12", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);											
						baseRow = communsPdf.setRow(table, 15);
						communsPdf.setCell(baseRow, 100, "___________________________________________________________", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
								
						
						if(isEndOfPage(table)) {
							table.getRows().remove(table.getRows().size()-1);
							table.draw();
							// CRACION NUEVA PAGINA
							page = new PDPage();
							document.addPage(page);
							acomula3 = false;
							
						}else {
							table.draw();
							yStart -= table.getHeaderAndDataHeight();
						}
						
						if(acomula3) {								
							x++;
						} 
					
						
					}
					
					
					
					
		
		
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
													
					yStart -= table.getHeaderAndDataHeight()+5;
					
					
					JSONObject bitacoras = new JSONObject("{'c4':[{'c3':'https://storage.googleapis.com/quattrocrm-copsis/s32tkk/2010/Avatar/ImfIJcsA&fraslBNjC4+ll7GMl&fraslL0BQVSim34WORCdmwEDsAf9BN9np8vrrC6GjXN2fNK.png','c4':'ADM','c5':false,'c6':true,'c10':0,'c7':'Thu Dec 09 09:43:12 CST 2021','c8':1,'c9':'09 dic 2021 09:43 AM','c1':'PRUEBA DE NOTIFICACION','c2':'09 dic 2021 09:43 AM'}]}");
					
					
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Bitacora", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
					
					
					int n =0;
					System.out.println( bitacoras.getJSONArray("c4").getJSONObject(0).getString("c1"));
					while(n < bitacoras.getJSONArray("c4").length()) {
						acomula4= true;
						table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
						baseRow = communsPdf.setRow(table, 10);											
						communsPdf.setCell(baseRow, 8, ImageUtils.readImage(bitacoras.getJSONArray("c4").getJSONObject(n).getString("c3")), i, i, bgColor).setValign(VerticalAlignment.MIDDLE);
						communsPdf.setCell(baseRow, 70,  bitacoras.getJSONArray("c4").getJSONObject(n).getString("c1"), black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 10);	
						communsPdf.setCell(baseRow, 7,  bitacoras.getJSONArray("c4").getJSONObject(n).getString("c4"), black, false, "R", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						baseRow = communsPdf.setRow(table, 10);	
						communsPdf.setCell(baseRow, 60,  bitacoras.getJSONArray("c4").getJSONObject(n).getString("c9"), black, false, "R", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
						
						if(isEndOfPage(table)) {
							table.getRows().remove(table.getRows().size()-1);
							table.draw();
							// CRACION NUEVA PAGINA
							page = new PDPage();
							document.addPage(page);
							acomula4 = false;
							
						}else {
							table.draw();
							yStart -= table.getHeaderAndDataHeight()+2;
						}
						
						if(acomula4) {								
							n++;
						} 
						
					}
					
	
		
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, true,true);
					baseRow = communsPdf.setRow(table, 2);
					communsPdf.setCell(baseRow, 100, "", black, false, "L", 10, communsPdf.setLineStyle(Color.white,Color.white,Color.black,Color.white), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					
					yStart -= table.getHeaderAndDataHeight()+5;
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, 30, document, page, false,true);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100, "Imagenes", black, true, "L", 10, communsPdf.setLineStyle(black), "", communsPdf.setPadding(2f),bgColor);
					table.draw();
					yStart -= table.getHeaderAndDataHeight();
					
					
					
					output = new ByteArrayOutputStream();
					document.save(output);
					document.save(new File("/home/desarrollo8/Pictures/prueba.pdf"));
					return output.toByteArray();
					
				} finally {
					document.close();
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionInter: " + ex.getMessage());
		}
	
	}
	
	private boolean isEndOfPage(BaseTable table) {
        float currentY = yStart - table.getHeaderAndDataHeight();
        return currentY <= bottomMargin;
    }

}
