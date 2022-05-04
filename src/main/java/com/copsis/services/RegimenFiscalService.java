/**
 * 
 */
package com.copsis.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.copsis.models.CatalogoReformaFiscalProps;
import com.copsis.models.RegimenFiscalAndTipoPersona;

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
	
	public RegimenFiscalAndTipoPersona get(String strRegimen) {
		//log.info("STEP0");
		RegimenFiscalAndTipoPersona response = new RegimenFiscalAndTipoPersona();
		String regimenFound = "";
		String tipoFound = "";
		for(Map<String, String> regimenObject : catalogoReformaFiscalProps.getRegimenes()) {
			//log.info("regimenObject: {}", regimenObject);
			if(regimenObject.get("regimenFiscal").contains(strRegimen)) {
				regimenFound = regimenObject.get("regimenFiscal");
				tipoFound = regimenObject.get("tipo");
			}			
			if(strRegimen.contains(regimenObject.get("regimenFiscal").split("-")[1].trim())) {
				regimenFound = regimenObject.get("regimenFiscal");
				tipoFound = regimenObject.get("tipo");
			}
		}
		if(!regimenFound.equals("")) {
			response.setRegimenFiscal(regimenFound);
			response.setTipo(tipoFound);
		}
		//log.info("regimenFound: {}", regimenFound);
		//log.info("STEP1");
		return response;
	}
}
