package br.com.person.controllers.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import br.com.person.exception.RequiredFieldException;
import br.com.person.exception.ResourceNotFoundException;

@ControllerAdvice
@RestController
public class CustomAdviceController {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorMessage> handleException(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		
		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}
	
	@ExceptionHandler(RequiredFieldException.class)
	public ResponseEntity<ErrorMessage> handleException(RequiredFieldException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleException(ResourceNotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;

		ErrorMessage em = new ErrorMessage(ex.getMessage(), request.getDescription(false), new Date(), String.valueOf(status.value()));
		
		return new ResponseEntity<>(em, status);		
	}

}
