package com.fabiorHair.controlesalao.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class JanelaDTO {
	
	private Date data;
	private Long idPessoa;
	private Long id;	
	private List<HorarioDTO> horarios;	
	private Date dataInicial;
	private Date dataFinal;

}
