package com.copsis.services;

import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.models.impresion.ImpresionConsolidadoModelPdf;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImpresioneTipoService {
	private byte[] byteArrayPDF;
	
    public byte[] getByteArrayPDF() {
        return this.byteArrayPDF;
    }

	public ImpresioneTipoService(ImpresionForm impresionForm ) {
		System.out.println("DATOS");
		switch (impresionForm.getTipoImpresion()) {
	 	case 100:
	 		ImpresionConsolidadoModelPdf  impresionConsolidao  = new ImpresionConsolidadoModelPdf();
	 		this.byteArrayPDF = impresionConsolidao.buildPDF(impresionForm);
			break;

		
		}
		
	}
	


}
