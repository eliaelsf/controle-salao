package com.fabiorHair.controlesalao.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fabiorHair.controlesalao.geral.Modelo;
import com.fabiorHair.controlesalao.geral.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="PERFIL")
public class Perfil extends Modelo {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3483290608150356558L;

	@Id
	//@GeneratedValue(strategy = GenerationType.TABLE, generator = "perfilGenerator")
	//@TableGenerator(name = "perfilGenerator", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_PERFIL")
	private Long id;
	
	 @NotEmpty
	 @Column(name="NOME")
     private String nome;
     
	 @Column(name="DESCRICAO")
     private String descricao;
     
     @JsonIgnore
     @ManyToMany
     @JoinTable(name="perfil_permissao", joinColumns=
     {@JoinColumn(name="id_perfil")}, inverseJoinColumns=
       {@JoinColumn(name="id_permissao")})
     private List<Permissao> permissoes;
     
     @JsonIgnore
     @OneToMany(mappedBy = "perfil")
     private List<User> usuarios;

     @Override
     public Long getId() {	
		return id;
     }
     
     public boolean contemRoleOuAdmin(Role roleConfigurada) {
         if(permissoes!=null && !permissoes.isEmpty()){
                 if (permissoes.stream().anyMatch((perm) -> (perm.getPapel().equals(roleConfigurada) || perm.isAdmin()))) {
                         return true;
                 }
         }
         return false;
    }
}
