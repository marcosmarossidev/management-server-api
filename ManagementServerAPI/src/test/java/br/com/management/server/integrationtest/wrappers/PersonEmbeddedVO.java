package br.com.management.server.integrationtest.wrappers;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.management.server.data.vo.PersonVO;

public class PersonEmbeddedVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("personVOList")
	private List<PersonVO> people;
	
	public PersonEmbeddedVO() {
	}

	public List<PersonVO> getPeople() {
		return people;
	}

	public void setPeople(List<PersonVO> people) {
		this.people = people;
	}

}
