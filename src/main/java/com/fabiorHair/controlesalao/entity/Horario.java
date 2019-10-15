package com.fabiorHair.controlesalao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import lombok.Data;

@Data
@Entity
@Table(name="HORARIO")
public class Horario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_HORARIO")
	private Long id;
    
    @Temporal(TemporalType.TIME)
    @Column(name="HORA_INICIO", nullable = false)
    private Date horaInicio;
    
    @Temporal(TemporalType.TIME)
    @Column(name="HORA_FIM", nullable = false)
    private Date horaFim;
    
    @Type(type="true_false")
    @Column(name="reservado")
    private boolean isReservado;
    
    @ManyToOne(optional = false)
    @JoinColumn(name="ID_JANELA", referencedColumnName="ID_JANELA")
    private Janela janela; 
    
    @ManyToOne
    @JoinColumn(name="ID_CLIENTE", referencedColumnName="ID_PESSOA")
    private Pessoa cliente;
    
}
