package com.fabiorHair.controlesalao.geral;

import lombok.Getter;

@Getter
public enum SituacaoItem {
	AGUARDANDO_AGENDAMENTO ("AG"),
	AGENDADO("AG"),
	UTILIZADO("UT");
	
	private String situacao;
	
	SituacaoItem(String situacao){
		this.situacao = situacao;
	}	

}
