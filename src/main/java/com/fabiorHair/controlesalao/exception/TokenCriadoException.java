package com.fabiorHair.controlesalao.exception;

import com.fabiorHair.controlesalao.entity.SegurancaAPI;

public class TokenCriadoException extends OAuthException {
	 private SegurancaAPI segurancaAPI;
     
     public TokenCriadoException(SegurancaAPI segurancaAPI) {
             super("Token ja criado para este usuario.");
             this.segurancaAPI = segurancaAPI;
     }

     public SegurancaAPI getSegurancaAPI() {
             return segurancaAPI;
     }
}
