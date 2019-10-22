package com.fabiorHair.controlesalao.contoller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import com.fabiorHair.controlesalao.exception.JanelaException;
import com.fabiorHair.controlesalao.geral.Role;
import com.fabiorHair.controlesalao.model.JanelaDTO;
import com.fabiorHair.controlesalao.seguranca.annotation.Privado;
import com.fabiorHair.controlesalao.service.JanelaService;
import com.fabiorHair.controlesalao.util.Constantes;
import com.fabiorHair.controlesalao.util.DateUtils;
import com.fabiorHair.controlesalao.util.StringUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/horario")
public class HorarioController {
	
	private JanelaService janelaService;
	
	@Privado(role=Role.ROLE_ADMIN)
	@PostMapping(value="/cadastrarJanela")
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrarJanela(@RequestBody JanelaDTO janela) {
		try {
		    this.validaJanela(janela);
		}catch(JanelaException j) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, j.getMessage());			
		}
		this.janelaService.criarJanela(janela);		
	}	
	
	@Privado(role=Role.ROLE_GERAL)
	@PostMapping("/janelas")
	public ResponseEntity<List<JanelaDTO>> buscarJanelas(@RequestBody JanelaDTO janela){
		try {
			this.validaDatas(janela.getDataInicial(), janela.getDataFinal());
		}catch(JanelaException j) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, j.getMessage());			
		}
		return ResponseEntity.ok().body(this.janelaService.buscaJanelasProfissional(janela.getIdPessoa(), janela.getDataInicial(), janela.getDataFinal()));
	}
	
	
	private void validaDatas(Date dataInicial, Date dataFinal ) {		
		if(!DateUtils.isDataInicialFinalValidas(dataInicial, dataFinal)	) {
			throw new JanelaException("A data da Inicial deve ser menor ou igual a data Final.");
		}
		
		if(DateUtils.getDiferencaDias(dataInicial, dataFinal) > Constantes.QDT_MAXIMO_DIAS_PESQUISA_JANELAS) {
			throw new JanelaException("Intervalo de datas não pode ser superior a 7 dias.");
		}
	}
	private void validaJanela(JanelaDTO janela)  {		
		if(janela.getData().before(new Date())) {
			throw new JanelaException("A data da janela inv�lida. Deve ser superior ou igual a data atual");
		}
		
		if(this.janelaService.janelaExisteProfissional(janela)) {
			throw new JanelaException("Janela já cadastrada para o profissional.");
		}		
		
		janela.setHorarios(
				janela.getHorarios().stream()
                .filter(horario -> !StringUtil.isStringVazia(horario.getHoraInicio()) &&  !StringUtil.isStringVazia(horario.getHoraFim()))
                .collect(Collectors.toList()));
		
		if(janela.getHorarios() == null || janela.getHorarios().isEmpty()) {
			throw new JanelaException ( "Janela deve conter pelo 1 horário.");			
		}		
		
	}
	
}
