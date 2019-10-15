package com.fabiorHair.controlesalao.exception;

public class JanelaException extends RuntimeException{
	
	 private static final long serialVersionUID = 1L;
	   
	   public JanelaException(String message) {
	       super(message);
	   }

	   public JanelaException(String message, Throwable cause) {
	       super(message, cause);
	   }
}
