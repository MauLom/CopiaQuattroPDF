package com.copsis.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.MergePDFForm;
import com.copsis.exceptions.GeneralServiceException;
import com.copsis.utils.ErrorCode;

@Service
public class MergePDFService {

    public byte[] mergePDF(MergePDFForm form) {
        try {
            PDFMergerUtility mergePdf = new PDFMergerUtility();

            for (byte[] base64 : form.getBase64()) {
                mergePdf.addSource(new ByteArrayInputStream(base64));
            }

            ByteArrayOutputStream pdfDocOutputstream = new ByteArrayOutputStream();
            mergePdf.setDestinationFileName(String.format("%s.%s", LocalDateTime.now().toString(), new Random().nextInt(9999)) + ".pdf");
            mergePdf.setDestinationStream(pdfDocOutputstream);
            mergePdf.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

            return pdfDocOutputstream.toByteArray();
        } catch (Exception ex) {
            throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, ex.getMessage());
        }
    }
}