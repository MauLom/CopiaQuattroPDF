package com.copsis.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.ImpresionForm;
import com.copsis.controllers.forms.UrlForm;
import com.copsis.encryptor.SiO4EncryptorAES;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImpresionService {


	public UrlForm ImpresionServicePdf (ImpresionForm impresionForm) {	
		String  folder ="";
		String bucket ="";
		
		System.out.println("LLEGO AQUI");
		 ImpresioneTipoService  impresioneTipoService  = new ImpresioneTipoService (impresionForm);
			System.out.println("LLEGO AQUI " +  impresioneTipoService.getByteArrayPDF());
		 byte[] byteArrayPDF = impresioneTipoService.getByteArrayPDF();
		 
		 switch (impresionForm.getTiporespuesta()) {
		case 1:
			Date date = new Date();
            Integer year = date.getYear() + 1900;           
            String mes = (date.getMonth() + 1) + "";
            String fecha = "";
            if (mes.length() == 1) {
                mes = "0" + mes;
            }
            fecha = year.toString().substring(2, 4) + mes;
            
            String nombrePdf = SiO4EncryptorAES.encrypt("Consolidado_" +date ,com.copsis.encryptor.utils.Constants.ENCRYPTION_KEY);
            
          System.out.println("====> " + nombrePdf);
            
			
			break;

		default:
			
			
			break;
		}
		 
		switch (impresionForm.getTiporespuesta()) {
		case 1:
			 return impresionForm.getUrls().get(0) ;
		default:
			 return impresionForm.getUrls().get(0) ;


		}

		 
	}
	 
}
