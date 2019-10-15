package com.fabiorHair.controlesalao.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TesteChave {
	
	public static void main(String[] args) {

		String dataS = "Mon Oct 07 2019 00:00:00";
		DateFormat data = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
		try {
			data.parse(dataS);
		}catch(Exception e) {
			e.printStackTrace();
		}
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
