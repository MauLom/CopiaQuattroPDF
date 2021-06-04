package com.copsis.models.impresion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.controllers.forms.UrlForm;
import com.copsis.models.EstructuraAseguradosModel;

public class ImpresionConsolidadoModelPdf {

	public byte[] buildPDF(ImpresionForm impresionForm) {
		byte[] pdfArray = null;
		PDFMergerUtility PDFmerger = new PDFMergerUtility();
		try {
			ByteArrayOutputStream output;
			try (PDDocument document = new PDDocument()) {
				try {


					List<UrlForm> list = impresionForm.getUrls();
					
					
					list.sort(Comparator.comparing(UrlForm::getOrden));

					for (UrlForm urlForm : list) {
						System.out.println(urlForm.getUrl());

						URL scalaByExampleUrl = new URL(urlForm.getUrl());

						final PDDocument documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
						PDFmerger.appendDocument(document, documentToBeParsed);
					}

					PDFmerger.mergeDocuments();

					output = new ByteArrayOutputStream();
					document.save(output);
					
					File ruta = new File ("/home/development/Videos/adan.pdf");
					
					document.save(ruta);
					return output.toByteArray();
				} finally {
					document.close();
				}

			}

		} catch (Exception ex) {
			System.out.println("Error en la impresion de Sio4CertificadoInterPdf  ==> " + ex.getMessage());
			return pdfArray;
		}

	}

}
