package com.fabiorHair.controlesalao.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TesteChave {
	
	public static void main(String[] args) {
		
		  String host = args[0];
	      String user = host.substring(0, host.indexOf('@'));
	      host = host.substring(host.indexOf('@')+1);

		String empresa ="Wisconsin 1164615-wisconsin_international_assessoria".replaceAll("[\\ ].*", "");
		System.out.println(empresa);
		/*String texto = "teste msg sera criptografada.";
		String chave = "eliael18";
		String textCript = FormatadorUtil.encryptDES(texto, chave);
		System.out.println("Mensagem criptogafada:" + textCript);
		
		String msgDesc = FormatadorUtil.decryptDES(textCript, chave);
		System.out.println("Mensagem descriptogafada:" + msgDesc);*/
		//int nucleos = Runtime.getRuntime().availableProcessors();
		
	   //System.out.println(" nucleos disponiveis: " + nucleos );
		
	  
	}	
}
