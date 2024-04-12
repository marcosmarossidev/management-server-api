package br.com.management.server.exception;

public class FileNotFoundInServerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public FileNotFoundInServerException(String msg) {
		super(msg);
	}
	
	public FileNotFoundInServerException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
