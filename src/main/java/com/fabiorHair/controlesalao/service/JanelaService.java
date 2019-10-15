package com.fabiorHair.controlesalao.service;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.fabiorHair.controlesalao.entity.Horario;
import com.fabiorHair.controlesalao.entity.Janela;
import com.fabiorHair.controlesalao.model.HorarioDTO;
import com.fabiorHair.controlesalao.model.JanelaDTO;
import com.fabiorHair.controlesalao.repository.JanelaRepository;
import com.fabiorHair.controlesalao.repository.PessoaRepository;
import com.fabiorHair.controlesalao.util.Constantes;
import com.fabiorHair.controlesalao.util.DateUtils;

@Service
public class JanelaService {
	
	@Resource
	private JanelaRepository janelaRepository;
	
	@Resource
	private PessoaRepository pessoaRepository;
	
	public Janela criarJanela(JanelaDTO janela) {
		return janelaRepository.save(this.converterJanela(janela));
	}	
	
	public Boolean janelaExisteProfissional(JanelaDTO janela) {
		return janelaRepository.existeJanelaProfissional(janela.getData(), janela.getIdPessoa());
	}
	
	public List<JanelaDTO> buscaJanelasProfissional(Long idProf, Date dataIncio, Date dataFim){
		List<Janela> janelas = this.janelaRepository.buscarJanelasProf(idProf, dataIncio, dataFim);
		if(janelas == null) {
			return null;
		}
		
		return janelas.stream()
					  .sorted(Comparator.comparing(Janela::getDataJanela))	
				      .map(janela -> this.conveterJanelaDTO(janela))
				      .collect(Collectors.toList());		
	}
	
	private JanelaDTO conveterJanelaDTO(Janela janela) {
		JanelaDTO janelaDTO = new JanelaDTO();
		janelaDTO.setData(janela.getDataJanela());
		janelaDTO.setIdPessoa(janela.getProfissional().getId());
		janelaDTO.setId(janela.getId());
		
		janelaDTO.setHorarios(janela.getHorarios()
				                    .stream()
				                    .sorted(Comparator.comparing(Horario::getHoraInicio))
				                    .map(horario -> this.converterHoaraioDTO(horario))
				                    .collect(Collectors.toList())
				              );		
		return janelaDTO;
	}
	
	private HorarioDTO converterHoaraioDTO(Horario horario ) {
		HorarioDTO horarioDTO = new HorarioDTO();
		horarioDTO.setId(horario.getId());
		horarioDTO.setReservado(horario.isReservado() ? Constantes.SIM : Constantes.NAO);
		horarioDTO.setIdCliente(horario.getCliente() == null ? null : horario.getCliente().getId());
		horarioDTO.setHoraInicio(DateUtils.retornarHora(horario.getHoraInicio()));
		horarioDTO.setHoraFim(DateUtils.retornarHora(horario.getHoraFim()));
		return horarioDTO;
		
	}
	
	private Janela converterJanela(JanelaDTO janelaDTO) {
		Janela janela = new Janela();
		janela.setDataJanela(janelaDTO.getData());	
		janela.setProfissional(pessoaRepository.findById(janelaDTO.getIdPessoa()).get());
		janela.setHorarios(	janelaDTO.getHorarios()
				         .stream()
				         .map(horario ->  this.conveterHorario(horario, janela))
				         .collect(Collectors.toList()));		
		return janela;
	}
	
	private Horario conveterHorario(HorarioDTO horarioDTO, Janela janela ) {
		Horario horario = new Horario();	
		horario.setHoraFim(DateUtils.retornaData( horarioDTO.getHoraFim()+":00", new SimpleDateFormat("HH:mm:ss")));			
		horario.setHoraInicio(DateUtils.retornaData( horarioDTO.getHoraInicio()+":00", new SimpleDateFormat("HH:mm:ss")));		
		horario.setReservado(Boolean.FALSE);
		horario.setJanela(janela);
		return horario;		
	}
}
