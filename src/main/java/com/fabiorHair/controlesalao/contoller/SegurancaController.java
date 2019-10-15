package com.fabiorHair.controlesalao.contoller;

import javax.servlet.http.HttpServletRequest;

import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fabiorHair.controlesalao.model.UsuarioDTO;
import com.fabiorHair.controlesalao.seguranca.OAuthController;
import com.fabiorHair.controlesalao.seguranca.annotation.Publico;
import com.fabiorHair.controlesalao.service.SegurancaServico;
import com.fabiorHair.controlesalao.service.UsuarioService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@CrossOrigin(origins = "*")
@Controller
@RequestMapping(value = "/seguranca")
public class SegurancaController extends OAuthController {
	
	private SegurancaServico segurancaServico;
	private UsuarioService usuarioService;
	
	@Publico
	@PostMapping("/logar")
	@ResponseBody
	public String logar(HttpServletRequest request) {
        OAuthResponse response = segurancaServico.logarOAuth(request);
        return response.getBody();
	}
	
	@Publico
	@PostMapping("/criar")
	@ResponseStatus(HttpStatus.CREATED)
	public void criarUsuario(@RequestBody UsuarioDTO usuario){
		this.usuarioService.criarUsuario(usuario);
	}
	
	@Publico
	@GetMapping("/criaPermissoes")
	@ResponseStatus(HttpStatus.OK)
	public void criarPermissoes() {
		this.usuarioService.obtemPermissoes();
	}
	
}
