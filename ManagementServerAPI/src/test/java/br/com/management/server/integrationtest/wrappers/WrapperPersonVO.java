package br.com.management.server.integrationtest.wrappers;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WrapperPersonVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("_embedded")
	private PersonEmbeddedVO embedded;
	
	public WrapperPersonVO() {
	}

	public WrapperPersonVO(PersonEmbeddedVO embedded) {
		this.embedded = embedded;
	}

	public PersonEmbeddedVO getEmbedded() {
		return embedded;
	}

	public void setEmbedded(PersonEmbeddedVO embedded) {
		this.embedded = embedded;
	}	

}
