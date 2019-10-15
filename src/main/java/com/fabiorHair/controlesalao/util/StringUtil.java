package com.fabiorHair.controlesalao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
   private static final	String EMAIL_PATTERN = 
	        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
   
   
	public static boolean isStringVazia(String valor){
		boolean retorno = false;
		if(valor == null || valor.isEmpty()){
			retorno =  true;
		}		
		return retorno;
	}
	
	public static boolean isEmailValido(String email) {		
		Pattern p = Pattern.compile(EMAIL_PATTERN);
	    Matcher m = p.matcher(email);
	    return m.matches();
	}

}
