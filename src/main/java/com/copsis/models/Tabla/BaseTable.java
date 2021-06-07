package com.copsis.models.Tabla;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;



public class BaseTable  extends Table<PDPage> {
	
	   /*ESTE ES LE METODO QUE USAMO ACTUALMENTE*/
    public BaseTable(float yStart, float yStartNewPage, float bottomMargin, float width, float margin, PDDocument document, PDPage currentPage, boolean drawLines, boolean drawContent) throws IOException {        
        super(yStart, yStartNewPage, 0, bottomMargin, width, margin, document, currentPage, drawLines, drawContent, new DefaultPageProvider(document, currentPage.getMediaBox()));
    }
    


    @Override
    protected void loadFonts() {
        // Do nothing as we don't have any fonts to load
    }

}
