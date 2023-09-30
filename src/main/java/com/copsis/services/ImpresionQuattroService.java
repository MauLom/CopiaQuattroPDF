package com.copsis.services;

import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.ImpresionCaratulaForm;
import com.copsis.controllers.forms.ImpresionReclamacion;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.exceptions.ValidationServiceException;
import com.copsis.models.impresionQuattrocrm.ImpresionSiniestroReclamacion;
import com.copsis.utils.ErrorCode;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class ImpresionQuattroService {

     public byte[] impresionSiniestroReclamacion(ImpresionReclamacion  impresionReclamacion){
        try {
            byte[] byteArrayPDF = null;
            byteArrayPDF = new ImpresionSiniestroReclamacion().buildPDF(impresionReclamacion);
            return byteArrayPDF;
        }
        catch (ValidationServiceException e) {
            throw e;
        }  catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }
    
}
