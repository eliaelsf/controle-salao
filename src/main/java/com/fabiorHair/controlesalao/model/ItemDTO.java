package com.fabiorHair.controlesalao.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ItemDTO {
	
	private Long id;
	private String descricao;
	private BigDecimal valor;
	private Integer quantidade;
	private Date dataValidade;

}
