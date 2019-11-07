package com.fabiorHair.controlesalao.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fabiorHair.controlesalao.entity.Perfil;
import com.fabiorHair.controlesalao.entity.Permissao;
import com.fabiorHair.controlesalao.entity.User;
import com.fabiorHair.controlesalao.geral.Role;
import com.fabiorHair.controlesalao.model.UsuarioDTO;
import com.fabiorHair.controlesalao.repository.PerfilRepository;
import com.fabiorHair.controlesalao.repository.PermissaoRepository;
import com.fabiorHair.controlesalao.repository.UserRepository;
import com.fabiorHair.controlesalao.util.FormatadorUtil;

/**
 * 
 * @author eliael.figueiredo
 *
 */

@Service
public class UsuarioService {
	
	@Resource
	private UserRepository userRepository;	
	@Resource
	private PerfilRepository perfilRepository;
	@Resource
	private PermissaoRepository permissaoRepository;
	
	
	public void criarUsuario(UsuarioDTO usuario) {
		this.userRepository.save(this.convetUsuario(usuario));
	}
	
	private User convetUsuario(UsuarioDTO userDTO) {		
		User user = new User();
		user.setLogin(userDTO.getLogin());
		user.setNome(userDTO.getNome());
		user.setSenha(FormatadorUtil.encryptMD5(userDTO.getSenha()));
		user.setPerfil(this.obterPerfilCliente());
		return user;
	}
	
	private Perfil obterPerfilCliente() {
		Perfil pefil = this.perfilRepository.findByNome("Cliente");	
		
		if(pefil == null) {
			pefil = new Perfil();
			pefil.setNome("Cliente");
			pefil.setDescricao("Perfil com permiss�o de cliente");
			pefil.setDataCriacao(new Date());
			pefil.setDataAtualizacao(new Date());
			pefil.setPermissoes(this.permissaoRepository.findByPapel(Role.ROLE_GERAL));
			pefil = this.perfilRepository.save(pefil);			
		}
		
		return pefil;
	}
	
	private Perfil obterPerfilAdm() {
		Perfil pefil = this.perfilRepository.findByNome("Administrador");	
		
		if(pefil == null) {
			pefil = new Perfil();
			pefil.setNome("Administrador");
			pefil.setDescricao("Perfil com permissão de Administrador");
			pefil.setDataCriacao(new Date());
			pefil.setDataAtualizacao(new Date());
			pefil.setPermissoes(this.obtemPermissoes());
			pefil = this.perfilRepository.save(pefil);			
		}
		
		return pefil;
	}
	
	
	
	public List<Permissao> obtemPermissoes() {	
		List<Permissao> permissoes = this.permissaoRepository.findAll();
		
		if(permissoes == null || permissoes.isEmpty()) {
			return permissaoRepository.saveAll(
			  Arrays.asList(Role.values()).stream()
			  .map(role -> this.criaPermissao(role))
			  .collect(Collectors.toList())
			 );
		}
		 return permissoes;
	}
	
	private Permissao criaPermissao(Role papel) {
		 if(Role.ROLE_ADMIN.equals(papel)) {
			return new Permissao(papel, "Permissão de Administrador.", "Usuário com permissão total.");
		 }else {
			 return new Permissao(papel, "Permissão Geral.", "Permissão geral da aplicação. Todos os perfis devem ter ao menos essa permissão.");
		 }
	}
	
	
	
}
