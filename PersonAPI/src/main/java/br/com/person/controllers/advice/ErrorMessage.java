package br.com.person.controllers.advice;

import java.io.Serializable;
import java.util.Date;

public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String message;
	private String description;
	private Date timestamp;
	private String statusCode;
	
	public ErrorMessage(String message, String description, Date timestamp, String statusCode) {
		this.message = message;
		this.description = description;
		this.timestamp = timestamp;
		this.statusCode = statusCode;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}	
	
}
