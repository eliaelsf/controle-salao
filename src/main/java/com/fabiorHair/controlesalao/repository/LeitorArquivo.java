package com.fabiorHair.controlesalao.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

import com.fabioHair.controlesalao.properties.FileStorageProperties;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class LeitorArquivo {
	
	private FileStorageProperties fileStorageProperties;
	
	/**
	 * Busca os e-mails a serem enviados , a partir o dirorio 
	 * especifico. serão considerados apenas arquivo .txt,
	 * @return
	 */
	public List<List<String>> obterEmail(){
		
		File diretorio = new File(fileStorageProperties.getUploadDir());		
		return Arrays.asList(diretorio.listFiles()).stream()
		.filter(file -> file.isFile() && file.getName().endsWith(".txt"))
		.map(emails -> this.obtemEmails(emails))
		.collect(Collectors.toList());
		
	}
	
	
	private List<String> obtemEmails(File file){		
		Charset charset = Charset.forName("UTF-8");		
		try (BufferedReader reader = Files.newBufferedReader(file.toPath(), charset)) {				
			return reader.lines()
					     .distinct()
					     .collect(Collectors.toList());			   
		}catch (IOException e) {
			 e.printStackTrace();
		}		
		return null;
	}
	
	public List<String> obtemEmailsEnviados(){		
		return this.obtemEmails(new File (this.fileStorageProperties.getCaminhoEmailsEnviados()));
	}
	
	public List<String> readFromTxt(boolean emaisEnviar) {
		
		List<String> emails = new ArrayList<>();
		try {
			BufferedReader arquivo;
			if(emaisEnviar) {
				arquivo = new BufferedReader(new FileReader(this.fileStorageProperties.getUploadDir()));
			}else {
				arquivo = new BufferedReader(new FileReader(this.fileStorageProperties.getCaminhoEmailsEnviados()));
			}
			
			String linha = arquivo.readLine();				
			 while (linha != null && !linha.isEmpty()){
				 emails.add(linha);
				 linha = arquivo.readLine(); 
			 }
			 arquivo.close(); 
		} catch(IOException e) {
			 e.printStackTrace();
		} 
		
		return emails;
	}	
	
	// Ajustar pois e-mails serão duplicados utilizar o Set ao inves do ArrayList
	public  synchronized void writeEmailsEnviados( List<String> emails) {	
		List<String> emailEnviados = this.obtemEmailsEnviados();
		if(emailEnviados != null && !emailEnviados.isEmpty())
		   emails.addAll(emailEnviados);
		
		try(BufferedWriter buffWrite = new BufferedWriter(new FileWriter(this.fileStorageProperties.getCaminhoEmailsEnviados()))) {				
            
			emails.stream().forEach(email -> {
				try {
					buffWrite.append(email + "\n");
				} catch (IOException e) {		
						e.printStackTrace();						
				}
			});			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
