package com.fabiorHair.controlesalao.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError.CodeResponse;
import org.apache.oltu.oauth2.common.error.OAuthError.ResourceResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fabiorHair.controlesalao.entity.SegurancaAPI;
import com.fabiorHair.controlesalao.entity.User;
import com.fabiorHair.controlesalao.exception.TokenCriadoException;
import com.fabiorHair.controlesalao.exception.TokenExpiradoException;
import com.fabiorHair.controlesalao.exception.TokenInvalidoException;
import com.fabiorHair.controlesalao.exception.UsuarioOuSenhaInvalidaException;
import com.fabiorHair.controlesalao.repository.SegurancaRepository;
import com.fabiorHair.controlesalao.repository.UserRepository;
import com.fabiorHair.controlesalao.seguranca.SegurancaAPIThreadLocal;
import com.fabiorHair.controlesalao.util.DateUtils;
import com.fabiorHair.controlesalao.util.FormatadorUtil;

@Service
public class SegurancaServico {
	
    private final String APP_CLIENT_ID = "exemploaplicativocliente";
    private final String APP_CLIENT_PASSWD = "9834ba657bb2c60b5bb53de6f4201905";
    
    @Resource
    private UserRepository userRepository;    
    
    @Resource
    private SegurancaRepository segurancaRepository;    
    
    /**
     * Metodo responsavel por validar as informações do cliente e do usuário que deseja logar.
     * Caso as dados estejam corretos será gerado um token, que possibilitará o usuário acessar
     * o componentes privados.
     * @param request
     * @return
     */
    public OAuthResponse logarOAuth(HttpServletRequest request) {
            try {
                    OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
                    String appClientId = oauthRequest.getClientId();
                    String appClientSecret = oauthRequest.getClientSecret();

                    try {
                           this.validarAcessoAplicativo(appClientId, appClientSecret);
                    } catch (TokenInvalidoException e) {
                            return this.retornarErroOAuth(HttpServletResponse.SC_UNAUTHORIZED, CodeResponse.UNAUTHORIZED_CLIENT, e);
                    }
                    
                    String senha = oauthRequest.getPassword();
                    String login = oauthRequest.getUsername();
                    User usuario = null;
                    
                    try {
                            usuario = this.retornarPorLoginESenha(login, senha);
                    } catch (UsuarioOuSenhaInvalidaException e) {
                            return this.retornarErroOAuth(HttpServletResponse.SC_UNAUTHORIZED, CodeResponse.UNAUTHORIZED_CLIENT, e);
                    }
                    
                    String accessToken = new OAuthIssuerImpl(new MD5Generator()).accessToken();
                    Date proximaDataExpiracao = this.retornarProximaDataExpiracao();
                    
                    try {
                            this.atualizarToken(usuario, accessToken, proximaDataExpiracao);
                    } catch (TokenExpiradoException e) {
                            return this.retornarErroOAuth(HttpServletResponse.SC_UNAUTHORIZED, ResourceResponse.EXPIRED_TOKEN, e);
                    } catch (TokenInvalidoException e) {
                            return this.retornarErroOAuth(HttpServletResponse.SC_BAD_REQUEST, ResourceResponse.INVALID_TOKEN, e);
                    } catch (TokenCriadoException e) {
                            //token jah criado anteriormente, somente retorna.
                            proximaDataExpiracao = e.getSegurancaAPI().getExpiracaoToken();
                    }

                    return OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK).setAccessToken(accessToken).setExpiresIn(
                            this.transformarProximaDataExpiracaoEmSegundos(new Date(), proximaDataExpiracao))
                            .setParam("nome", usuario.getNome())
                            .setParam("login", usuario.getLogin())
                            .setParam("perfil", usuario.getPerfil().getNome())
                            .setParam("idUser", usuario.getId().toString())
                            .buildJSONMessage();

            } catch (OAuthProblemException e) {
                    return this.retornarErroOAuth(HttpServletResponse.SC_UNAUTHORIZED, CodeResponse.INVALID_REQUEST, e);
            } catch (Exception e) {
                    return this.retornarErroOAuth(HttpServletResponse.SC_BAD_REQUEST, CodeResponse.SERVER_ERROR, e);
            }
    }
    
    /**
     * Responsável por validar se os atributos do cliente foram informados.
     * @param appClientId Id Cliente
     * @param appClientSecret Senha
     * @throws TokenInvalidoException
     */
    private void validarAcessoAplicativo(String appClientId, String appClientSecret) throws TokenInvalidoException {
        if (StringUtils.isBlank(appClientId)) {
                throw new TokenInvalidoException("Atributo clientId nulo.");
        }
        if (StringUtils.isBlank(appClientId)) {
                throw new TokenInvalidoException("Atributo clientSecret nulo.");
        }
        if (!appClientId.equalsIgnoreCase(APP_CLIENT_ID) && !appClientSecret.equalsIgnoreCase(APP_CLIENT_PASSWD)) {
                throw new TokenInvalidoException("Seguranca: aplicativo nao autorizado.");
        }
     }
    
    private User retornarPorLoginESenha(String login, String senha) throws UsuarioOuSenhaInvalidaException{
    	User  usuario = this.userRepository.findByLoginAndSenha(login, FormatadorUtil.encryptMD5(senha));
        if(usuario == null){
                throw new UsuarioOuSenhaInvalidaException("Usuário não encontrado.");
        }
        return usuario;
    }
    
   /**
    * Metodo chamado pelo WS para guardar o Token temporario da empresa a
    * ser retornado para o cliente da requisicao.
    * @param usuario Uuario logado
    * @param token Token gerado
    * @param proximaDataExpiracao Data de expiração do token
    * @throws TokenInvalidoException
    * @throws TokenExpiradoException
    * @throws TokenCriadoException
    */
    private synchronized void atualizarToken(User usuario, String token, Date proximaDataExpiracao) throws TokenInvalidoException, TokenExpiradoException, TokenCriadoException {
            if (usuario == null) {
                    throw new TokenInvalidoException("Problema interno ao criar token: usuario nulo.");
            }
            if (StringUtils.isBlank(token)) {
                    throw new TokenInvalidoException("Problema interno ao criar token: token vazio.");
            }
            if (proximaDataExpiracao == null) {
                    throw new TokenInvalidoException("Problema interno ao criar token: proximaDataExpiracao nula.");
            }

            SegurancaAPI segurancaAPI = this.retornarPorUsuario(usuario);
            if (segurancaAPI == null) {
                    segurancaAPI = new SegurancaAPI(token, proximaDataExpiracao, usuario);
            } else {
                    segurancaAPI.atualizarToken(token, proximaDataExpiracao);
            }
            segurancaAPI = this.segurancaRepository.save(segurancaAPI);
            
            if (segurancaAPI.isSalvo()) {
                    if (segurancaAPI.expirado()) {
                            throw new TokenExpiradoException("Token de acesso expirado. Gere um novo token e tente novamente.");
                    }
                    else {
                            throw new TokenCriadoException(segurancaAPI);
                    }
            }
    }
    
	 private SegurancaAPI retornarPorToken(String token) {
	        return this.segurancaRepository.findByToken(token);
	}
	
	
	private SegurancaAPI retornarPorUsuario(User usuario) {
	        return this.segurancaRepository.findByUsuario(usuario);
	}
    
    public OAuthResponse retornarErroOAuth(int errorCode, String error, Exception e) {
        try {
                String descricao = e.getMessage();
                return OAuthASResponse.errorResponse(errorCode).setError(error + (descricao != null ? " - " + descricao : "")).setErrorDescription(descricao).buildJSONMessage();
        } catch (OAuthSystemException ex) {
                throw new RuntimeException(ex);
        }
    }
    
    private Date retornarProximaDataExpiracao() {
        Date agora = new Date();
        int dia = DateUtils.retornaUnidade(agora, DateUtils.DIA);
        int mes = DateUtils.retornaUnidade(agora, DateUtils.MES);
        int ano = DateUtils.retornaUnidade(agora, DateUtils.ANO);
        return DateUtils.retornaData(dia + "/" + mes + "/" + ano + " 23:59:59", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
   }
    
    private String transformarProximaDataExpiracaoEmSegundos(Date atual, Date proxima) {
        int horas = DateUtils.getDiferencaHoras(atual, proxima);
        return "" + (horas * 60 * 60);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized void verificaValidadeTokenAdicionandoNoContexto(HttpServletRequest request) throws TokenExpiradoException, TokenInvalidoException {
        try {
                OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);
                String token = oauthRequest.getAccessToken();

                if (StringUtils.isBlank(token)) {
                        throw new TokenInvalidoException("Token vazio.");
                }

                SegurancaAPI segurancaAPI = this.retornarPorToken(token);
                if (segurancaAPI == null) {
                        throw new TokenInvalidoException("Token invalido.");
                }

                User usuario = segurancaAPI.getUsuario();
                if (usuario == null) {
                        throw new TokenInvalidoException("Problema interno no retorno do usuario: nulo.");
                }

                if (segurancaAPI.getToken().contains(token)) {
                        if (segurancaAPI.expirado()) {
                                segurancaAPI.expirarToken();
                                this.segurancaRepository.save(segurancaAPI);
                                throw new TokenExpiradoException("Token de acesso expirado. Gere um novo token e tente novamente.");
                        } else {
                                Hibernate.initialize(segurancaAPI.getUsuario().getPerfil().getPermissoes()); //force! :/
                                SegurancaAPIThreadLocal.setSegurancaAPI(segurancaAPI);
                        }
                } else {
                        throw new TokenInvalidoException("Token invalido. Tente novamente.");
                }
        } catch (OAuthProblemException e) {
                throw new TokenInvalidoException("Login invalido. Tente novamente.");
        } catch (OAuthSystemException ex) {
                throw new RuntimeException(ex);
        }
   }
    
    public SegurancaAPI getUsuarioLogado() throws TokenInvalidoException {
        SegurancaAPI seg = SegurancaAPIThreadLocal.getSegurancaAPI();
        if (seg == null) {
                throw new TokenInvalidoException("Usuário não logado.");
        } else {
                return seg;
        }
}
}
