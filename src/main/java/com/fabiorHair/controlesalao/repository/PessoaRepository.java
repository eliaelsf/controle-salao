package com.fabiorHair.controlesalao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fabiorHair.controlesalao.entity.Pessoa;
import com.fabiorHair.controlesalao.geral.RolePessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {	
	
	List<Pessoa> findByTipo(RolePessoa tipo);
	
	@Query("select case when count(p) > 0 then true else false end from Pessoa p where p.cpf = :cpf")
	Boolean existePessoaComcpf(@Param("cpf")String cpf);
	
	@EntityGraph(value = "pessoa.janelas.horarios", type = EntityGraph.EntityGraphType.FETCH)
    Pessoa findByCpf(String cpf);
}
