package com.fabiorHair.controlesalao.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HorarioDTO {
	
	private Long id;
	private String horaInicio;	
	private String horaFim;
	private String reservado;
	private Long idCliente;
}
