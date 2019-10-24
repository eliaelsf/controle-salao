package com.fabiorHair.controlesalao.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
public class Item {
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_ITEM")
	private Long id;
	
	@Column(name="DESCRICAO")
	@NotEmpty
    @Size(max = 150)
	private String descricao;
	
	@Column(name="VALOR")
	@NotNull
	@Min(0)
	private BigDecimal valor;
	
	@Column(name="QTDE")
	@NotNull
	@Min(1)
	private Integer quantidade;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_VALIDADE")
	private Date dataValidade;
	
    @Length(
            min = 1,
            max = 2,
            message = "deve ser: 'AA'(Aguardando Agendamento),'AG'(Agendado), 'UT'(Utilizado)")
    @Pattern(
            regexp = "(AA|AG|UT){1}",
            message = "\"deve ser: 'AA'(Aguardando Agendamento),'AG'(Agendado), 'UT'(Utilizado")  
    @Column(name = "SITUACAO", nullable = false, length = 2)
	private String situacao;
    
    @ManyToOne(optional = false)
    @JoinColumn(name="ID_PEDIDO", referencedColumnName="ID_PEDIDO")
    private Pedido pedido;

}
