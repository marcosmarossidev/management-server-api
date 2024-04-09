package br.com.management.server.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJWTAuthenticationException extends AuthenticationException {	

	private static final long serialVersionUID = 1L;
	
	public InvalidJWTAuthenticationException(String msg) {
		super(msg);
	}

}
