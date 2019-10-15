package com.fabiorHair.controlesalao.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fabiorHair.controlesalao.geral.Modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="SEGURANCA_API")
public class SegurancaAPI extends Modelo {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6138327916129265734L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_SEGURANCA")
    private Long id;
    
	@Column(length = 1000, name="TOKEN")
	private String token = "(init)";
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EXPIRACAO_TOKEN")
	private Date expiracaoToken = new Date();
	
	@OneToOne
	@JoinColumn(name="ID_USER", referencedColumnName="ID_USER")
	private User usuario;
	
	public boolean expirado() {
		return expiracaoToken != null && expiracaoToken.before(new Date());
	}
	
	 public SegurancaAPI(String token, Date expiracaoToken, User usuario) {
         this.token = token;
         this.expiracaoToken = expiracaoToken;
         this.usuario = usuario;
    }

	 public SegurancaAPI() {
	 }
	 
	 public void atualizarToken(String token, Date expiracaoToken) {
			this.token = token;
			this.expiracaoToken = expiracaoToken;
	 }
	        
     public void expirarToken() {
		this.atualizarToken("", new Date());
	}
		
}
