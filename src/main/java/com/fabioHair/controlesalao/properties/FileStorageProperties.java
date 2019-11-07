package com.fabioHair.controlesalao.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
/** 
 * @author eliael.figueiredo
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class FileStorageProperties {	
	
	private String uploadDir;
	private String caminhoEmailsEnviados;
	private final Mail mail = new Mail();
	
	@Getter
	@Setter
	public static class Mail {		
		private String servidor;
		private Integer porta;
		private String usurio;
		private String senha;
	}

}
