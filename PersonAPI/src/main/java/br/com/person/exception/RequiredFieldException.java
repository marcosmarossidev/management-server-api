package br.com.person.exception;

public class RequiredFieldException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RequiredFieldException(String msg) {
		super(msg);
	}

}
