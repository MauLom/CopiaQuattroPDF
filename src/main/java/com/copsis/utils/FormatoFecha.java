package com.copsis.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.copsis.exceptions.GeneralServiceException;

@Component
public class FormatoFecha {
	
	public Date getDateFormat(String dateString, String dateFormat) {
		Date dateFormater = null;
		try {
			Locale locale = new Locale("ES","MX");
			DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
			dateFormatSymbols.setMonths(new String[] {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"});
			dateFormatSymbols.setShortMonths(new String[] {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"});
			SimpleDateFormat formmater = new SimpleDateFormat(dateFormat, dateFormatSymbols);
			dateFormater = formmater.parse(dateString);
		} catch (ParseException e) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000, e.getMessage());
		}
		return dateFormater;
	}
	
	public String getStringFormat(Date date, String dateFormat) {	
		String stringFormater = "";
		try {
			Locale locale = new Locale("ES","MX");
			DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
			dateFormatSymbols.setMonths(new String[] {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"});
			dateFormatSymbols.setShortMonths(new String[] {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"});
			SimpleDateFormat formmater = new SimpleDateFormat(dateFormat, dateFormatSymbols);
			stringFormater = formmater.format(date);
		} catch (Exception e) {
			throw new GeneralServiceException(ErrorCode.MSJ_ERROR_00000,e.getMessage());
		}
		return stringFormater;
	}
}
