package com.copsis.controllers;


import java.io.FileNotFoundException;
import java.io.IOException;


import javax.servlet.http.HttpServlet;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.copsis.controllers.forms.PdfForm;
import com.copsis.models.CopsisResponse;
import com.copsis.services.IdentificaPolizaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pdf/lector-pdf")
@RequiredArgsConstructor
public class LectorPDFController  extends HttpServlet {
	
	@Autowired
	private IdentificaPolizaService textPdf;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CopsisResponse> lectorpdf (@RequestBody PdfForm pdfForm,BindingResult bindingResult,@RequestHeader HttpHeaders headers) {
		
		if(bindingResult.hasErrors()) {
			return new CopsisResponse.Builder().ok(false).status(HttpStatus.BAD_REQUEST).message("No cuenta con los parametros suficientes").build();
		}

		   JSONObject response = new JSONObject();
		try {
		  System.out.print(pdfForm.getUrl());
			response = textPdf.QueCIA(pdfForm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new CopsisResponse.Builder()
				.ok(true)
				.status(HttpStatus.OK)
				.result(response).build();

	}
	
//	 public void postBody(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		response.setContentType("application/json;charset=UTF-8");
//		response.addHeader("Access-Control-Allow-Origin", "*");
//
//		try (PrintWriter out = response.getWriter()) {
//			try {
//					// Get post/get parameters
//					BufferedReader reader = request.getReader();
//					String jsonObject = reader.readLine();
//					String line = null;
//					while ((line = reader.readLine()) != null) {
//						jsonObject += line;
//					}
//					JSONObject jsonObj = new JSONObject(jsonObject);
//					String url = jsonObj.getString("url");
//					try {
//						IdentificaPolizaService textPdf = new IdentificaPolizaService();
//					     JSONObject obj = textPdf.QueCIA(url);
//						out.println(obj);
//					} catch (Exception ex) {
//						out.println("{\"error\": \"1002: " + ex.getMessage() + "\"}");
//					}
//
//			} catch (Exception ex) {
//				out.println("{\"error\": \"1003: " + ex.getMessage() + "\"}");
//			}
//		}
//      
//    }
	
}
