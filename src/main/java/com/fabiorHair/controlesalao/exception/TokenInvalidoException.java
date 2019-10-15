package com.fabiorHair.controlesalao.exception;

public class TokenInvalidoException extends OAuthException  {
	 public TokenInvalidoException(String message) {
         super(message);
     }

	 public TokenInvalidoException(Throwable cause) {
	         super(cause);
	 }
}
