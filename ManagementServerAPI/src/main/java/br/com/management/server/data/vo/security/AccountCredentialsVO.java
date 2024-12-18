package br.com.management.server.data.vo.security;

import java.io.Serializable;

public class AccountCredentialsVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	public AccountCredentialsVO() {
		
	}
	
	public AccountCredentialsVO(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
