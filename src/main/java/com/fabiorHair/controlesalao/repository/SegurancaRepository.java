package com.fabiorHair.controlesalao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fabiorHair.controlesalao.entity.SegurancaAPI;
import com.fabiorHair.controlesalao.entity.User;


public interface SegurancaRepository  extends JpaRepository<SegurancaAPI, Long> {
	@Query("SELECT s FROM SegurancaAPI s WHERE s.token = :token")
    SegurancaAPI findByToken(@Param("token") String token);
    
    SegurancaAPI findByUsuario(User usuario);
}
