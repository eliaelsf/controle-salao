package com.fabiorHair.controlesalao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.fabiorHair.controlesalao.geral.Modelo;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="USER")
@NamedEntityGraph(name = "perfil.permissoes", attributeNodes = @NamedAttributeNode(value = "perfil", subgraph = "permissoes"), 
subgraphs = @NamedSubgraph(name = "permissoes", attributeNodes = @NamedAttributeNode("permissoes")))

public class User extends Modelo {
	
   private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_USER")
	private Long id;	
	
	@NotEmpty
	@Column(name="NOME")
	private String nome;
      
	@NotEmpty
	@Size(min = 5, max = 20)
	@Column(name="LOGIN", unique = true)
	private String login;
      
    @NotEmpty
    @Size(min = 5)
    @Column(name="SENHA")
    private String senha;
      
    @ManyToOne(optional = false)
    @JoinColumn(name="ID_PERFIL", referencedColumnName="ID_PERFIL")
    private Perfil perfil;
      
    @Override
    public Long getId() {
        return id;
    }

}
