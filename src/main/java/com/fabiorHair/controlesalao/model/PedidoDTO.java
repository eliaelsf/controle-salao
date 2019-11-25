package com.fabiorHair.controlesalao.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PedidoDTO {
	
	private Long id;
	private String status;
	private Long idCliente;
	private List<ItemDTO> itens;
}
