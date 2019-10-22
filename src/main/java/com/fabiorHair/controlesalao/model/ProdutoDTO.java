package com.fabiorHair.controlesalao.model;


import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProdutoDTO {
	
	private Long id;
	private String descricao;
	private BigDecimal valor;
	private Date dataValidade;	
	private byte[] imagem;
	private String url;

}
