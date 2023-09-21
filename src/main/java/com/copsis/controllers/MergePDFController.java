package com.copsis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.copsis.controllers.forms.MergePDFForm;
import com.copsis.models.CopsisResponse;
import com.copsis.services.MergePDFService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/merge")
@RequiredArgsConstructor
public class MergePDFController {
    @Autowired
    private MergePDFService mergePDFService;

    @PostMapping
    public ResponseEntity<CopsisResponse> merger(@RequestBody MergePDFForm form) {
        return new CopsisResponse.Builder().ok(true).status(HttpStatus.OK).result(mergePDFService.mergePDF(form)).build();
    }
}
