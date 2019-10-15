package com.fabiorHair.controlesalao.entity;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fabiorHair.controlesalao.geral.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="PRODUTO")
public class Produto extends Item {

	private static final long serialVersionUID = 5877429350879734946L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_PRODUTO")
	private Long id;
	
	@Column(name="DESCRICAO")
	@NotEmpty
    @Size(max = 150)
	private String descricao;
	
	@Column(name="VALOR")
	@NotNull
	@Min(0)
	private BigDecimal valor;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DT_FIM_VIGENCIA")
	private Date dataFimVigencia;
	
	@Lob @Column(name="PIC")
	private byte[] imagem;
	
}
