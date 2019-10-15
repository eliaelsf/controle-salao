package com.fabiorHair.controlesalao.model;

import com.fabiorHair.controlesalao.geral.RolePessoa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PessoaDTO {
	
	private Long id;
	private String nome;	
	private String cpf;
	private String email;
	private String telefone;
	private String endereco;
	private RolePessoa tipo;
	
	
}
