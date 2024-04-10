package br.com.management.server.controllers.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.auth0.jwt.exceptions.TokenExpiredException;

import br.com.management.server.exception.InvalidJWTAuthenticationException;
import br.com.management.server.exception.RequiredFieldException;
import br.com.management.server.exception.ResourceNotFoundException;

@ControllerAdvice
@RestController
public class CustomAdviceController {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleException(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}
	
	@ExceptionHandler(RequiredFieldException.class)
	public ResponseEntity<ErrorMessage> handleRequiredFieldException(RequiredFieldException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<ErrorMessage> handleTokenExpiredException(TokenExpiredException ex, WebRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		
		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;

		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}
	
	@ExceptionHandler(InvalidJWTAuthenticationException.class)
	public ResponseEntity<ErrorMessage> handleInvalidJWTAuthenticationException(InvalidJWTAuthenticationException ex, WebRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN;

		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorMessage> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN;

		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}

}
