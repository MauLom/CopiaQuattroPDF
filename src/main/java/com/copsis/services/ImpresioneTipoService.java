package com.copsis.services;

import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.models.impresion.ImpresionConsetimientoPdf;
import com.copsis.models.impresion.ImpresionConsolidadoModelPdf;
import com.copsis.models.impresion.ImpresionInter;
import com.copsis.models.impresion.ImpresionReclamacionPdf;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImpresioneTipoService {
	private byte[] byteArrayPDF;

	public byte[] getByteArrayPDF() {
		return this.byteArrayPDF;
	}

	public ImpresioneTipoService(ImpresionForm impresionForm) {
		switch (impresionForm.getTipoImpresion()) {
		case 100:
			ImpresionConsolidadoModelPdf impresionConsolidao = new ImpresionConsolidadoModelPdf();
			this.byteArrayPDF = impresionConsolidao.buildPDF(impresionForm);
			break;
		case 102:
			ImpresionInter impresionInter = new ImpresionInter();
			this.byteArrayPDF = impresionInter.buildPDF(impresionForm);
			break;
		case 103:
			ImpresionConsetimientoPdf impresionConsetimientoPdf = new ImpresionConsetimientoPdf();
			this.byteArrayPDF = impresionConsetimientoPdf.buildPDF(impresionForm);
			break;
		case 105:
			ImpresionReclamacionPdf impresionReclamacionPdf = new ImpresionReclamacionPdf();
			this.byteArrayPDF = impresionReclamacionPdf.buildPDF(impresionForm);
			break;	
			
		default:
			break;
		}
	}
}
