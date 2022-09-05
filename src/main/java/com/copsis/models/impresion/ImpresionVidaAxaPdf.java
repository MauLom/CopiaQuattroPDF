package com.copsis.models.impresion;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import com.copsis.controllers.forms.ImpresionAxaForm;
import com.copsis.controllers.forms.ImpresionAxaVidaForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.models.Tabla.BaseTable;
import com.copsis.models.Tabla.ImageUtils;
import com.copsis.models.Tabla.Row;
import com.copsis.models.Tabla.Sio4CommunsPdf;

public class ImpresionVidaAxaPdf {
	
	private Sio4CommunsPdf communsPdf = new Sio4CommunsPdf();
	private final Color bgColor = new Color(255, 255, 255, 0);
	private final Color bgColorAb = new Color(116, 111, 159, 0);
	private final Color bgColorA = new Color(36, 44, 106, 0);
	private float margin = 25, yStartNewPage = 780, yStart = 780, bottomMargin = 30;
	private float fullWidth = 566;
	private Boolean acumula;

	public byte[] buildPDF(ImpresionAxaVidaForm  impresionAxaVidaForm) {
		ByteArrayOutputStream output;
		try {
			
			try (PDDocument document = new PDDocument()) {
				try {
					PDPage page = new PDPage();
					document.addPage(page);
					BaseTable table;
					Row<PDPage> baseRow;
					
					this.setEncabezado(document, page);
					System.out.println("paso 2 ==> " +yStart);
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page,true , true);	    
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 20,"Datos del Contratante",Color.white,true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorA);
					communsPdf.setCell(baseRow, 80,"(Es la persona que se compromete a realizar el pago de la prima)",Color.white,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(0f,1f,4f,0f),bgColorA);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"El nombre completo, el RFC con homoclave y la CURP son datos necesarios para la emisión de las constancias y CFDI para la deducción de impuestos y, en su caso, para la recuperación de estos.",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80,"Nombre completo (como aparece en su identificación oficial)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 20,"Género",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80,"Nombre completo (como aparece en su identificación oficial)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 20,"Género",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"RFC con homoclave",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"CURP",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"No. de serie FIEL",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"Estado civil",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 72,"Domicilio",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 14,"No. exterior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 14,"No. interior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 72,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 14,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 14,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"Colonia",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Código postal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"Municipio / Alcaldía",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 31,"Población o ciudad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 31,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"Estado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 44,"Régimen fiscal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 15,"Clave de uso",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 19,"C.P. domicilio fiscal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 22,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 44,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 15,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 19,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
								
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 32,"Tel. particular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 32,"Tel. celular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 36,"Correo electrónico",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 32,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 32,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 36,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 23,"Actividad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 16,"Ingreso Anual",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Ciudadanía en Estados",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 36,"Residencia fiscal en el extranjero",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 23,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 16,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 36,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
				
					baseRow = communsPdf.setRow(table, 15);					
					communsPdf.setCell(baseRow, 100,"Completar si es residente fiscal en el extranjero o tiene ciudadanía o nacionalidad de Estados Unidos",Color.white,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorAb);
			
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"Domicilio en el extranjero",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 12,"No. exterior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 12,"No. interior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Colonia",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"Código postal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 12,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 12,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Población o ciudad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 13,"Estado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"País",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 23,"No. de Identificación Fiscal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"Teléfono",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 23,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 26,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);

				
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Datos del Solicitante Titular (solo si es diferente del Contratante)",Color.white,true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorA);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"(Es la persona sobre la que recaen los riesgos amparados en la póliza, si es aceptada la solicitud)",Color.white,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorA);

					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80,"Nombre(s), apellido paterno, apellido materno",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 20,"Género",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 80,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 20,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);

					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Estado Civil",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Relación con el contratante",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Fecha de nacimiento",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"Nacionalidad(es)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"Lugar de nacimiento (país, estado, municipio)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 37,"Domicilio",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"No. interior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow,12,"No. exterior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 37,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					communsPdf.setCell(baseRow, 12,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"Colonia",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Código postal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Municipio / Alcaldía",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow,25,"Población o ciudad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Estado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"Tel. celular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);									
					communsPdf.setCell(baseRow,42,"Correo electrónico",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 42,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
//					baseRow = communsPdf.setRow(table, 15);
//					communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2209/Polizas/2209/Q2N4l4ntCvjSrcYxpTNj3XINojdFJ0ksYi9XZB9TmicMVpVgufnUbOqKk2FZBbnL/Logo1.png"),3,0,Color.white);
//		
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 80,"Nombre(s), apellido paterno, apellido materno",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);				
					communsPdf.setCell(baseRow, 20,"Género",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					baseRow = communsPdf.setRow(table, 12);
					communsPdf.setCell(baseRow, 80,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 20,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);

					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Estado Civil",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Relación con el contratante",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Fecha de nacimiento",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"Nacionalidad(es)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"Lugar de nacimiento (país, estado, municipio)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 37,"Domicilio",bgColorA,true, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"No. interior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow,12,"No. exterior",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 38,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 37,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 13,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					communsPdf.setCell(baseRow, 12,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"Colonia",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"Código postal",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Municipio / Alcaldía",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow,25,"Población o ciudad",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 36,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 14,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);	
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Estado",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"Tel. celular",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);									
					communsPdf.setCell(baseRow,42,"Correo electrónico",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 33,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 42,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);							
					table.setCellCallH(true);
										
					table.draw();
					
					
					page = new PDPage();
					document.addPage(page);
					this.setEncabezado(document, page);

				
					table = new BaseTable(yStart, yStartNewPage, bottomMargin, fullWidth, margin, document, page,true , true);	    
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 15,"Plan solicitado",Color.white,true, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(4f,4f,4f,4f),bgColorA);
					communsPdf.setCell(baseRow, 85,"(Confirmar características aplicables a cada producto)",Color.white,false, "L", 10, communsPdf.setLineStyle(bgColorA), "", communsPdf.setPadding(0f,1f,4f,0f),bgColorA);					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 100,"Otros seguros de vida (En Suma Asegurada, indique la moneda)",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"Solicitante",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Aseguradora",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Suma Asegurada",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Solicitante",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"Aseguradora",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);					
					communsPdf.setCell(baseRow, 25,"Suma Aseguradora",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.white,Color.black), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					baseRow = communsPdf.setRow(table, 15);
					communsPdf.setCell(baseRow, 25,"1231",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"2323",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"234234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					communsPdf.setCell(baseRow, 25,"2324234",bgColorA,false, "L", 10, communsPdf.setLineStyle(Color.black,Color.black,Color.black,Color.white), "", communsPdf.setPadding(4f,4f,2f,4f),bgColor);
					
					
					table.setCellCallH(true);
					table.draw();
					
					output = new ByteArrayOutputStream();
					document.save(output);									
					document.save(new File("/home/aalbanil/Documentos/AXA-SPRING-PF/AxaVida.pdf"));
					return output.toByteArray();
					
				}finally {
					document.close();
				}
				
			}
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionVidaAxaPdf: " + ex.getMessage());
		}
	}
	private float  setEncabezado( PDDocument document, PDPage page) {	
		try (PDPageContentStream content = new PDPageContentStream(document, page)) {
			BaseTable table;
			Row<PDPage> baseRow;
			 yStart =770;
			 table = new BaseTable(770, 770, bottomMargin, 300, 280, document, page, true, true);	    
			 baseRow = communsPdf.setRow(table, 10);
			 communsPdf.setCell(baseRow, 100,"Vida y Ahorro",bgColorAb,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			 baseRow = communsPdf.setRow(table, 10);
			 communsPdf.setCell(baseRow, 100,"Solicitud de Seguro de Vida Individual",bgColorA,true, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);
			 baseRow = communsPdf.setRow(table, 10);
			 communsPdf.setCell(baseRow, 100,"Personas Físicas",bgColorA,false, "R", 11, communsPdf.setLineStyle(Color.white), "", communsPdf.setPadding(5f,5f,3f,5f),bgColor);	
			 table.draw();
			
			table = new BaseTable(770, 770, bottomMargin, 120, margin-22, document, page, false, true);
			baseRow = communsPdf.setRow(table, 12);
			communsPdf.setCell(baseRow, 100, ImageUtils.readImage("https://storage.googleapis.com/quattrocrm/h31fila/2208/Polizas/2208/zeSWdRgKylw7QuyGNMQ9vc8HSPiToM8cVUnQd5e2VOIzWrci1IgN1QLcOhFEfhy/Axalogo.png"));
			table.draw();
			yStart -=table.getHeaderAndDataHeight()+15;
			

			
			return yStart;
			
			
		} catch (Exception ex) {
			throw new GeneralServiceException("00001",
					"Ocurrio un error en el servicio ImpresionConstanciaAntiguedad: setEncabezado " + ex.getMessage());
		}

	}
}
