package com.copsis.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copsis.services.IdentificaPolizaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lector-pdf")
@RequiredArgsConstructor
public class LectorPDFController  extends HttpServlet {
	

	@PostMapping
	 public void postBody(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.addHeader("Access-Control-Allow-Origin", "*");

		try (PrintWriter out = response.getWriter()) {
			try {
					// Get post/get parameters
					BufferedReader reader = request.getReader();
					String jsonObject = reader.readLine();
					String line = null;
					while ((line = reader.readLine()) != null) {
						jsonObject += line;
					}
					JSONObject jsonObj = new JSONObject(jsonObject);
					String url = jsonObj.getString("url");
					try {
						IdentificaPolizaService textPdf = new IdentificaPolizaService();
					     JSONObject obj = textPdf.QueCIA(url);
						out.println(obj);
					} catch (Exception ex) {
						out.println("{\"error\": \"1002: " + ex.getMessage() + "\"}");
					}

			} catch (Exception ex) {
				out.println("{\"error\": \"1003: " + ex.getMessage() + "\"}");
			}
		}
      
    }
	
}
