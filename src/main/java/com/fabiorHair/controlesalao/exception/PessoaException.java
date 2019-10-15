package com.fabiorHair.controlesalao.exception;

public class PessoaException extends RuntimeException{
	
	 private static final long serialVersionUID = 1L;
	   
	   public PessoaException(String message) {
	       super(message);
	   }

	   public PessoaException(String message, Throwable cause) {
	       super(message, cause);
	   }
}
