package com.fabiorHair.controlesalao.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.fabiorHair.controlesalao.entity.Pessoa;
import com.fabiorHair.controlesalao.geral.RolePessoa;
import com.fabiorHair.controlesalao.model.PessoaDTO;
import com.fabiorHair.controlesalao.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Resource
	private PessoaRepository pessoaDao;
	
	public Pessoa cadastraPessoa(PessoaDTO pessoaDTO){
		Pessoa pessoa = this.converterPessoa(pessoaDTO);		
		return pessoaDao.save(pessoa);
	}
	
	public boolean existePessoaComCpf(String cpf) {	
		return this.pessoaDao.existePessoaComcpf(cpf);
	}
	
	public  List<PessoaDTO> buscarProfissionais(){		
		List<Pessoa> pessoas = pessoaDao.findByTipo(RolePessoa.PROFISSIONAL);
		return pessoas.stream().map(pes -> this.converterEnTDto(pes))
				.sorted(Comparator.comparing(PessoaDTO::getNome))
				.collect(Collectors.toList());
	}
	
	private PessoaDTO converterEnTDto(Pessoa pessoa) {
		PessoaDTO pessoaDTO = new PessoaDTO();
		pessoaDTO.setId(pessoa.getId());
		pessoaDTO.setNome(pessoa.getNome());
		return pessoaDTO;
	}
	
	private Pessoa converterPessoa(PessoaDTO pessoaDTO){
		Pessoa pessoa = new Pessoa();		
		pessoa.setEmail(pessoaDTO.getEmail());
		pessoa.setEndereco(pessoaDTO.getEndereco());
		pessoa.setNome(pessoaDTO.getNome());
		pessoa.setTelefone(pessoaDTO.getTelefone());
		pessoa.setCpf(pessoaDTO.getCpf());
		pessoa.setTipo( pessoaDTO.getTipo() != null ? pessoaDTO.getTipo() :RolePessoa.CLIENTE );
		return pessoa;
	}	
	
	public Optional<PessoaDTO> buscarPessoabyId(Long id){
		return this.pessoaDao.findById(id)
		              .map(op -> this.converterEnTDto(op)) ;		              
	}
	
}
