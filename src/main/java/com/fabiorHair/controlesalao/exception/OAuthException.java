package com.fabiorHair.controlesalao.exception;

public class OAuthException extends RuntimeException {
	
	public OAuthException() {
        super();
     }

	public OAuthException(String message) {
	        super(message);
	}
	
	public OAuthException(Throwable cause) {
	        super(cause);
	}
}
