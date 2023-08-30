package com.copsis.services;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.EstructuraJsonModel;
import com.copsis.models.caractula.LecturaCaractulaAxa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class IndentificaCaratulaAxaService {
  public LecturaCaractulaAxa lecturaCaractulaAxa;

  public EstructuraJsonModel caractulaAxa(PdfForm pdfForm) throws Exception {
    EstructuraJsonModel caractula = new EstructuraJsonModel();

    try {

      PDDocument documentToBeParsed = null;
      COSDocument cosDoc = null;
      PDDocument pdDoc = null;

      final URL scalaByExampleUrl = new URL(pdfForm.getUrl());
      documentToBeParsed = PDDocument.load(scalaByExampleUrl.openStream());
      PDFTextStripper pdfStripper = new PDFTextStripper();
      cosDoc = documentToBeParsed.getDocument();
      pdDoc = new PDDocument(cosDoc);
      pdfStripper.setStartPage(1);
      pdfStripper.setEndPage(1);
      String contenido = caratula(1, 1, pdfStripper, pdDoc).replaceAll("\t", "");
      


      if (contenido.contains("Carátula") || contenido.contains("contratante")) {

        LecturaCaractulaAxa lecturaCaractulaAxa = new LecturaCaractulaAxa();

        caractula = lecturaCaractulaAxa.procesar(caratula(1, 20, pdfStripper, pdDoc).replaceAll("\t", " "));

      }

      return caractula;

    } catch (Exception e) {
      return caractula;
    }
  }

  public String caratula(int inicio, int fin, PDFTextStripper stripper, PDDocument doc) throws IOException {
    stripper.setStartPage(inicio);
    stripper.setEndPage(fin);
    stripper.setParagraphStart("@@@");
    stripper.setWordSeparator("###");
    stripper.setSortByPosition(true);
    return stripper.getText(doc);
  }
}
