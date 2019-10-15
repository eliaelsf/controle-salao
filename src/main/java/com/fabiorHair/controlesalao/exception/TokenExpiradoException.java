package com.fabiorHair.controlesalao.exception;

public class TokenExpiradoException extends OAuthException {
	 public TokenExpiradoException(String message) {
         super(message);
	 }
	
	 public TokenExpiradoException(Throwable cause) {
	         super(cause);
	 }
}
