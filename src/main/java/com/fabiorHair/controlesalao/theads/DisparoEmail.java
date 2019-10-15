package com.fabiorHair.controlesalao.theads;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.fabioHair.controlesalao.properties.FileStorageProperties;
import com.fabiorHair.controlesalao.exception.EmailException;
import com.fabiorHair.controlesalao.model.EnvioEmail;
import com.fabiorHair.controlesalao.util.StringUtil;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public class DisparoEmail implements Callable<List<String>> {
	
	private final List<String> listaEmails;	
	private final FileStorageProperties fileStorageProperties;
	private final EnvioEmail envioEmail;

	@Override
	public List<String> call() throws Exception {
		List<String> emailsEnviado = new CopyOnWriteArrayList<String> ();
		if(!listaEmails.isEmpty()) {			
			listaEmails.parallelStream().forEach(email -> {
				try {
					if(StringUtil.isEmailValido(email)) {
						this.enviarEmailLocWeb(email, envioEmail);					
						emailsEnviado.add(email);
					}
				} catch (UnsupportedEncodingException | MessagingException e) {
					e.printStackTrace();
				}
			});		   
		}else {
			throw new EmailException("Arquivos de E-mails não informados");
		}
		
		return emailsEnviado;
	}
	
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
	     /* tr.connect(this.fileStorageProperties.getMail().getServidor(),
	    		     this.fileStorageProperties.getMail().getUsurio(), 
	    		     this.fileStorageProperties.getMail().getSenha());*/

	      msg.saveChanges();
	      //tr.sendMessage(msg, msg.getAllRecipients());
	      System.out.println("Enviado Email para: " + email+ " hora: " + System.currentTimeMillis());
	      tr.close();		    
		  
	}

}
