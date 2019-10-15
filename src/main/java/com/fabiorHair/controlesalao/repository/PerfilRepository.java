package com.fabiorHair.controlesalao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiorHair.controlesalao.entity.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long>  {
	
	Perfil findByNome(String nome);
}
