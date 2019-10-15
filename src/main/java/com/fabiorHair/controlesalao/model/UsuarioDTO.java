package com.fabiorHair.controlesalao.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioDTO implements Serializable  {
	private static final long serialVersionUID = 1L;
	private String login;
	private String senha;
	private String nome;
	private Long id;
	private Long idPessoa;
}

/*
AllArgsConstructor: cria automaticamente um construtor com todas os atributos da classe como argumento.
NoArgsConstructor: cria automaticamente um construtor vazio (sem argumentos).
Data: cria automaticamente os métodos toString, equals, hashCode, getters e setters
*/