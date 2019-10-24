package com.fabiorHair.controlesalao.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.fabiorHair.controlesalao.geral.Modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="PEDIDO")
public class Pedido extends Modelo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3888977263983888176L;

	@Id
	@SequenceGenerator(name = "seq_ped", sequenceName = "seq_ped", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ped")	
	@Column(name="ID_PEDIDO")
	private Long id;
	
    @Length(
            min = 1,
            max = 2,
            message = "deve ser: 'AU'(Aguardando Utilizacao),'AT'(Agendado Total),'AP'(Agendado Parcial),'PF'(Pagamento efetuado),'FE'(Fechado),'PE'(Pagamento Estornado)")
    @Pattern(
            regexp = "(AT|AP|AU|PF|FE|PE){1}",
            message = "\"deve ser: 'AU'(Aguardando Utilizacao),'AT'(Agendado Total),'AP'(Agendado Parcial),'PF'(Pagamento efetuado),'FE'(Fechado),'PE'(Pagamento Estornado)")  
    @Column(name = "STATUS", nullable = false, length = 2)
	private String status;
    
    @ManyToOne
    @JoinColumn(name="ID_CLIENTE", referencedColumnName="ID_PESSOA")
    private Pessoa cliente;    
    
	@OneToMany(mappedBy = "pedido", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Item> itens;	
	
}
