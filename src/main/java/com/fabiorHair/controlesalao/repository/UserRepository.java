package com.fabiorHair.controlesalao.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiorHair.controlesalao.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@EntityGraph(value = "perfil.permissoes", type = EntityGraph.EntityGraphType.FETCH)
    User findByLogin(String login);
    
    @EntityGraph(value = "perfil.permissoes", type = EntityGraph.EntityGraphType.FETCH)
    User findByLoginAndSenha(String login, String senha);
}
