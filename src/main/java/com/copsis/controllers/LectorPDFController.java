package com.copsis.controllers;

import javax.servlet.http.HttpServlet;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lector-pdf")
@RequiredArgsConstructor
public class LectorPDFController  extends HttpServlet {
	

	@PostMapping("/postbody")
	 public String postBody(@RequestBody String fullName) {
        return "Hello " + fullName;
    }
	
}
