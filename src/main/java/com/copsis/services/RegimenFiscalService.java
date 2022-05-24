/**
 * 
 */
package com.copsis.services;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.copsis.models.CatalogoReformaFiscalProps;
import com.copsis.models.RegimenFiscalPropsDto;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Israel_work
 *
 */
@Service
@Slf4j
public class RegimenFiscalService {
	@Autowired
	private CatalogoReformaFiscalProps catalogoReformaFiscalProps;
	
	private static final String CLAVE_KEY = "clave";
	private static final String REGIMEN_FISCAL_KEY = "regimenFiscal";
	private static final String TIPO_KEY = "tipo";
	
	public RegimenFiscalPropsDto get(String strRegimen) {
		RegimenFiscalPropsDto response = new RegimenFiscalPropsDto();
		String claveFound = "";
		String regimenFound = "";
		String tipoFound = "";
		for(Map<String, String> regimenObject : catalogoReformaFiscalProps.getRegimenes()) {
			Pattern pattern1 = Pattern.compile(regimenObject.get(REGIMEN_FISCAL_KEY).replace("(", "\\(").replace(")", "\\)"), Pattern.CASE_INSENSITIVE);
			Matcher matcher1 = pattern1.matcher(strRegimen);
			if(matcher1.find()) {
				claveFound = regimenObject.get(CLAVE_KEY);
				regimenFound = regimenObject.get(REGIMEN_FISCAL_KEY);
				tipoFound = regimenObject.get(TIPO_KEY);
			}
			
			Pattern pattern2 = Pattern.compile(strRegimen.replace("(", "\\(").replace(")", "\\)"), Pattern.CASE_INSENSITIVE);
			Matcher matcher2 = pattern2.matcher(regimenObject.get(REGIMEN_FISCAL_KEY));
			if(matcher2.find()) {
				claveFound = regimenObject.get(CLAVE_KEY);
				regimenFound = regimenObject.get(REGIMEN_FISCAL_KEY);
				tipoFound = regimenObject.get(TIPO_KEY);
			}
		}
		if(!regimenFound.equals("")) {
			response.setClave(claveFound);
			response.setDescripcion(regimenFound);
			response.setTipo(tipoFound);
		}
		//log.info("regimenFound: {}", regimenFound);
		
		return response;
	}
}
