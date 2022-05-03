/**
 * 
 */
package com.copsis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.copsis.models.CatalogoReformaFiscalProps;

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
	
	public String getRegimen(String strRegimen) {
		//log.info("STEP0");
		String regimenFound = "";
		for(String regimenObject : catalogoReformaFiscalProps.getRegimenes()) {
			//log.info("regimenObject: {}", regimenObject);
			if(regimenObject.contains(strRegimen)) {
				regimenFound = regimenObject;
			}
			if(strRegimen.contains(regimenObject.split("-")[1].trim())) {
				regimenFound = regimenObject;
			}
		}
		//log.info("regimenFound: {}", regimenFound);
		//log.info("STEP1");
		return regimenFound;
	}
}
