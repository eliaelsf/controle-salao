package com.fabiorHair.controlesalao.seguranca;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fabiorHair.controlesalao.entity.SegurancaAPI;
import com.fabiorHair.controlesalao.geral.Role;
import com.fabiorHair.controlesalao.seguranca.annotation.Privado;
import com.fabiorHair.controlesalao.seguranca.annotation.Publico;
import com.fabiorHair.controlesalao.service.SegurancaServico;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class SegurancaInterceptor extends HandlerInterceptorAdapter{
	
	private SegurancaServico segurancaServico;
	/**
	 * método reposável pela intercepção das chamadas feitas os serviço.
	 * Caso o operaçao seja publica, a requisiçao será liberada, caso não
	 * é vefiricado se o token informado é válidoe se o usuário solitiante
	 * possu permissão pra acessar o recurso.
	 */
	 @Override
     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
             if(handler instanceof HandlerMethod){
                     HandlerMethod met = (HandlerMethod) handler;
                     
                     //acesso publico, libera
                     Publico annotPublico = met.getMethodAnnotation(Publico.class);
                     if(annotPublico!=null){
                             return true;
                     }
                     
                     //acesso privado, verifica as roles do usuario com a role configurada no metodo.
                     Privado annotPrivado = met.getMethodAnnotation(Privado.class);
                     if(annotPrivado!=null){
                         this.segurancaServico.verificaValidadeTokenAdicionandoNoContexto(request);
                         
                         Role roleConfigurada = annotPrivado.role();
                         SegurancaAPI usuarioLogado = segurancaServico.getUsuarioLogado();
                         if( usuarioLogado.getUsuario().getPerfil().contemRoleOuAdmin(roleConfigurada) ){
                                 return true;
                         }                            
                     }                     
                    
                     if(HttpStatus.BAD_REQUEST.value() == response.getStatus() ||
                    		 HttpStatus.INTERNAL_SERVER_ERROR.value() == response.getStatus() ) {                    	 
                    	 return super.preHandle(request, response, handler);
                     }
                     //se nao eh publico nem privado, nega por padrao.
                      return this.negarAcesso(response);
                    
             }else{
                     return super.preHandle(request, response, handler);
             }
     }
     
     private boolean negarAcesso(HttpServletResponse response) throws IOException{
             response.getWriter().println("Usuário Sem acesso ao recurso.");
             response.setStatus(HttpServletResponse.SC_FORBIDDEN);
             return false;
     }
     
}
