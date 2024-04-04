package br.com.management.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.management.server.controllers.PersonController;
import br.com.management.server.core.FieldValidator;
import br.com.management.server.data.vo.PersonVO;
import br.com.management.server.exception.ResourceNotFoundException;
import br.com.management.server.mapper.CustomMapper;
import br.com.management.server.model.Person;
import br.com.management.server.repositories.PersonRepository;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;

	public PersonVO findById(Long id) {
		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
		
		PersonVO personVO = CustomMapper.parseObject(person, PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		return personVO;
	}

	public List<PersonVO> findAll() {
		List<PersonVO> peopleVO = CustomMapper.parseListObjects(repository.findAll(), PersonVO.class);
		
		peopleVO.stream().forEach(item -> {
			item.add(linkTo(methodOn(PersonController.class).findById(item.getKey())).withSelfRel());
		});
		
		return peopleVO;
	}

	public void delete(Long id) {
		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));

		repository.delete(person);
	}

	public PersonVO update(PersonVO person) throws Exception {
		FieldValidator.checkField(new String[] { "firstName", "lastName", "gender", "key" }, person);
		
		Person aux = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Person with id " + person.getKey() + " not found"));

		aux.setFirstName(person.getFirstName());
		aux.setLastName(person.getLastName());
		aux.setGender(person.getGender());
		aux.setAddress(person.getAddress());
		
		PersonVO personVO = CustomMapper.parseObject(repository.save(aux), PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
		
		return personVO;
	}

	public PersonVO insert(PersonVO person) throws Exception {
		FieldValidator.checkField(new String[] { "firstName", "lastName", "gender" }, person);
		
		Person convertedPerson = CustomMapper.parseObject(person, Person.class);
		
		PersonVO personVO = CustomMapper.parseObject(repository.save(convertedPerson), PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
		
		return personVO;
	}

}
