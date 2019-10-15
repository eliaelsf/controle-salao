package com.fabiorHair.controlesalao.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnvioEmail {	
	
	@Size(min=5)	
	private String assuntoEmail;
	
	@NotBlank
	@Email
	private String remetenteEmail;	
	
	@Size(min=10)
	@NotBlank
	private String corpoEmail;
	private Integer qtde;	

}
