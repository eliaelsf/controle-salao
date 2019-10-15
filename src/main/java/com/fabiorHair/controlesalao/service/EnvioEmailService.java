package com.fabiorHair.controlesalao.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fabioHair.controlesalao.properties.FileStorageProperties;
import com.fabiorHair.controlesalao.exception.EmailException;
import com.fabiorHair.controlesalao.model.EnvioEmail;
import com.fabiorHair.controlesalao.repository.LeitorArquivo;
import com.fabiorHair.controlesalao.theads.DisparoEmail;
import com.fabiorHair.controlesalao.util.Constantes;
import com.fabiorHair.controlesalao.util.StringUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EnvioEmailService {
	/*
	@Value("${app.mail.servidor}")
	String servidorEmail; */	
	
	private LeitorArquivo leitorArquivo;	
	private FileStorageProperties fileStorageProperties;
	
	/**
	 * Realiza o envio de E-mail sem a utilização de theads.
	 *  O processo será feito de forma assíncrona.
	 * @param envioEmail dados do e-mail , como remetente, assunto e titulo.	
	 * @throws EmailException
	 */
	@Async
	public void enviarEmail(EnvioEmail envioEmail) throws EmailException {
		List<String> emailsEnviado = new CopyOnWriteArrayList<String> ();		
		List<List<String>> listaEmail = this.leitorArquivo.obterEmail();
		
		if(listaEmail.isEmpty()) {
			throw new EmailException("Arquivos de E-mails não informados");
		}
		
		listaEmail.forEach(list -> 
		 list.parallelStream().forEach(email -> {
			try {
				if(StringUtil.isEmailValido(email)) {
					this.enviarEmailLocWeb(email, envioEmail);					
					emailsEnviado.add(email);
				}
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
			}
		}));
		
	    leitorArquivo.writeEmailsEnviados(emailsEnviado);		
	}
	
	/**
	 * Realiza envio de e-mail de forma paralela para cada lista de email que 
	 * representa uma arquivo. Será utilizado 3 dos 4 núcleos do processador. 
	 * Apos envio os emails que obtiveram sucesso serão gravados em arquivo.
	 * O processo será feito de forma assíncrona.
	 * @param envioEmail  dados do e-mail , como remetente, assunto e titulo
	 */
	@Async
	public void envioEmailParalelo(EnvioEmail envioEmail) {
		List<List<String>> listaEmail = this.leitorArquivo.obterEmail();
		
		if(listaEmail.isEmpty()) {
			return;
		}
		
		List<DisparoEmail> diparos = listaEmail
				                     .stream()
				                     .map(list -> new DisparoEmail(list, this.fileStorageProperties,  envioEmail))
				                     .collect(Collectors.toList());
		
		/*List<DisparoEmail> diparos = Arrays.asList(
				new DisparoEmail(listaEmail.get(0), this.fileStorageProperties,  envioEmail)
				);*/
		
		ExecutorService threadPool = Executors.newFixedThreadPool(Constantes.NUMERO_THREADS);
		
		ExecutorCompletionService<List<String>> completionService = new ExecutorCompletionService<>(threadPool);
		
		for(DisparoEmail diparo : diparos) {
			completionService.submit(diparo);
		}
		List<String> emailsEnviado = new ArrayList<String>();
		for (int i = 0; i < diparos.size(); i++) {
            try {
            	emailsEnviado.addAll(completionService.take().get());               
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }		
		threadPool.shutdown();
		
		this.leitorArquivo.writeEmailsEnviados(emailsEnviado);
	
	}
	
	public void enviaEmail(List<String> emails, List<String> emailsEnviados, EnvioEmail envioEmail) {
		int count = 0;		
		for (String email : emails) {
			if(count > 300) {break;}
			try {
			  if(!emailsEnviados.contains(email)) {			  
				  this.enviarEmailLocWeb(email, envioEmail);
				  emailsEnviados.add(email);				
				  count = count + 1;
			  }
			}catch(Exception e) {				
				System.out.println("Erro ao enviar para o e-mail: " + email);				
				e.printStackTrace();
			}			
			
		}
		System.out.println("Emails enviados " + count);	
		
	}
	
	/**
	 * Responsável por realizar o envio de e-mail para uma determinado usário conforme 
	 * os dados repassados.
	 * @param email e-mail de destino
	 * @param envioEmail dodos do email: remetente, assunto e corpo
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	private void enviarEmailLocWeb(String email, EnvioEmail envioEmail) throws UnsupportedEncodingException, MessagingException {
		 Properties props = new Properties();
		 props.put("mail.transport.protocol", "smtp");
		 props.put("mail.smtp.host", this.fileStorageProperties.getMail().getServidor());
		 props.put("mail.smtp.auth", "true");
		 props.put("mail.smtp.port", this.fileStorageProperties.getMail().getPorta());

		 Session session = Session.getDefaultInstance(props, null);
		 session.setDebug(false);
		 
		
	      InternetAddress iaFrom = new InternetAddress(envioEmail.getRemetenteEmail(), "eliaelleo@gmail.com");
	      InternetAddress[] iaTo = new InternetAddress[1];
	      InternetAddress[] iaReplyTo = new InternetAddress[1];

	      iaReplyTo[0] = new InternetAddress(email);
	      iaTo[0] = new InternetAddress(email);

	      MimeMessage msg = new MimeMessage(session);

	      if (iaReplyTo != null) msg.setReplyTo(iaReplyTo);
	      if (iaFrom != null) msg.setFrom(iaFrom);
	      if (iaTo.length > 0)msg.setRecipients(Message.RecipientType.TO, iaTo);
	      
	      msg.setSubject(envioEmail.getAssuntoEmail());
	      msg.setSentDate(new Date());

	      msg.setContent(envioEmail.getCorpoEmail(), "text/plain");

	      Transport tr = session.getTransport("smtp");
          tr.connect(this.fileStorageProperties.getMail().getServidor(),
    		         this.fileStorageProperties.getMail().getUsurio(), 
	    		     this.fileStorageProperties.getMail().getSenha());

	      msg.saveChanges();
	      tr.sendMessage(msg, msg.getAllRecipients());
	      System.out.println("Enviado Email para: " + email+ " hora: " + System.currentTimeMillis());
	      tr.close();		    
		  
	}
	

}
