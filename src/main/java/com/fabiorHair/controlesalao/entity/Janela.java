package com.fabiorHair.controlesalao.entity;

import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name="JANELA")
public class Janela {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_JANELA")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_JANELA", nullable = false)
	private Date dataJanela;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="ID_FUNCIONARIO", referencedColumnName="ID_PESSOA", nullable = false)
	private Pessoa profissional;
	
	@OneToMany(mappedBy = "janela", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<Horario> horarios;
}
