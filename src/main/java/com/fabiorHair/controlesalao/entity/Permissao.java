package com.fabiorHair.controlesalao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fabiorHair.controlesalao.geral.Modelo;
import com.fabiorHair.controlesalao.geral.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="PERMISSAO")
public class Permissao extends Modelo {	
	private static final long serialVersionUID = -8821904675322873169L;

	@Id
	//@GeneratedValue(strategy = GenerationType.TABLE, generator = "permissaoGenerator")
	//@TableGenerator(name = "permissaoGenerator", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_PERMISSAO")
	private Long id;
    
    @NotEmpty
    @Column(name="NOME")
    private String nome;
    
    @Column(name="DESCRICAO")
    private String descricao;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role papel;	

	@Override
	public Long getId() {		
		return id;
	}
	
	 boolean isAdmin() {
         return this.papel.equals(Role.ROLE_ADMIN);
	 }
	 
	 public Permissao(Role papel, String nome, String desc) {
		 this.papel = papel;
		 this.nome = nome;
		 this.descricao = desc;
		 super.setDataAtualizacao(new Date());
	 }
}
