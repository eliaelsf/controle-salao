package com.fabiorHair.controlesalao.geral;
import lombok.Getter;

@Getter
public enum SatusPedido { 
	 AGENDADO_TOTAL("AG"),
	 AGENDADO_PARCIAL("AP"),
	 FECHADO("FE"),
	 PAGAMENTO_EFETUADO("PF"),
	 PAGAMENTO_ESTORNADO("PE");
	 
	 private String status;
	 
	 SatusPedido(String status){
		 this.status = status;
	 }
	 
}
