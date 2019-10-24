package com.fabiorHair.controlesalao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fabiorHair.controlesalao.geral.Modelo;
import com.fabiorHair.controlesalao.geral.RolePessoa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="PESSOA")
public class Pessoa extends Modelo {
 
	private static final long serialVersionUID = 3159340602804443700L;

	@Id
	//@GeneratedValue(strategy = GenerationType.TABLE, generator = "pessoaGenerator")
	//@TableGenerator(name = "pessoaGenerator", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_PESSOA")
	private Long id;
	
	@Column(name="NOME")
	@NotEmpty
    @Size(max = 150)
	private String nome;
	
	@NotEmpty
	@Size(min = 11, max = 14)
	@Column(name="CPF", unique = true)
	private String cpf;
	
	@Column(name="EMAIL")	
    @Size(max = 100)	
	private String email;	
	
	@Column(name="TELEFONE")	
    @Size(max = 20)
	private String telefone;
	
	@Column(name="ENDERECO")
    @Size(max = 150)
	private String endereco;
	
	@Column(name="TIPO")	
	@Enumerated(EnumType.STRING)
	private RolePessoa tipo;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="ID_USER", referencedColumnName="ID_USER")
	private User usuario;	
	
	@OneToMany(mappedBy = "profissional")
	private List<Janela> janelas;
	
	@OneToMany(mappedBy = "cliente")
	private List<Horario> horarios;
	
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos;
}
