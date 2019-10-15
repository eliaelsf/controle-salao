package com.fabiorHair.controlesalao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fabiorHair.controlesalao.entity.Janela;

public interface JanelaRepository extends JpaRepository<Janela, Long> {
	
	List<Janela> findByProfissionalId (Long id);
	
	@Query("select case when count(j) > 0 then true else false end from Janela j where j.dataJanela = :dataJanela and j.profissional.id = :idProf ")
	Boolean existeJanelaProfissional(@Param("dataJanela")Date dataJanela, @Param("idProf")Long idProf );
	
	@Query("select j from Janela j where j.profissional.id = :idProf and j.dataJanela between :dataIncio and :dataFim")
	List<Janela> buscarJanelasProf(@Param("idProf")Long idProf , @Param("dataIncio")Date dataIncio, @Param("dataFim")Date dataFim);
}
