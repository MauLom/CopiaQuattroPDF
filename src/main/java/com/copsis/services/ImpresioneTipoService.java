package com.copsis.services;

import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.models.impresion.ImpresionConsolidadoModelPdf;
import com.copsis.models.impresion.ImpresionInter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImpresioneTipoService {
	private byte[] byteArrayPDF;
	
    public byte[] getByteArrayPDF() {
        return this.byteArrayPDF;
    }

	public ImpresioneTipoService(ImpresionForm impresionForm ) {
		switch (impresionForm.getTipoImpresion()) {
	 	case 100:
	 		ImpresionConsolidadoModelPdf  impresionConsolidao  = new ImpresionConsolidadoModelPdf();
	 		this.byteArrayPDF = impresionConsolidao.buildPDF(impresionForm);	 		
			break;
		case 102:
			ImpresionInter  Impresioninter  = new ImpresionInter();
	 		this.byteArrayPDF = Impresioninter.buildPDF(impresionForm);	 		
			break;

		
		}
		
	}
	


}
