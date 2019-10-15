package com.fabiorHair.controlesalao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fabiorHair.controlesalao.entity.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query("select p from Produto p where p.descricao like :descricao")
	public List<Produto> findByDescricaoLike (@Param("descricao")String descricao);	
	public List<Produto> findByDescricao (String descricao);

}
