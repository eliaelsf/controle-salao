package com.fabiorHair.controlesalao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fabiorHair.controlesalao.entity.Permissao;
import com.fabiorHair.controlesalao.geral.Role;

public interface PermissaoRepository extends JpaRepository<Permissao, Long>{
	
	List<Permissao> findByPapel(Role papel);
}
