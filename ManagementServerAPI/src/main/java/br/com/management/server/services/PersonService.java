package br.com.management.server.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.management.server.controllers.PersonController;
import br.com.management.server.core.FieldValidator;
import br.com.management.server.data.vo.PersonVO;
import br.com.management.server.exception.ResourceNotFoundException;
import br.com.management.server.mapper.CustomMapper;
import br.com.management.server.model.Person;
import br.com.management.server.repositories.PersonRepository;
import jakarta.transaction.Transactional;

@Service
public class PersonService {

	@Autowired
	private PersonRepository repository;
	
	@Autowired
	private PagedResourcesAssembler<PersonVO> assembler;

	public PersonVO findById(Long id) {
		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
		
		PersonVO personVO = CustomMapper.parseObject(person, PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		return personVO;
	}

	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
		
		Page<Person> personPage = repository.findAll(pageable);
		
		Page<PersonVO> personVosPage = personPage.map(p -> CustomMapper.parseObject(p, PersonVO.class));
		
		personVosPage.stream().forEach(item -> {
			item.add(linkTo(methodOn(PersonController.class).findById(item.getKey())).withSelfRel());
		});
		
		Link link = linkTo(methodOn(PersonController.class)
				.findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return assembler.toModel(personVosPage, link);
	}
	
	public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {
		
		Page<Person> personPage = repository.findPersonByName(firstName, pageable);
		
		Page<PersonVO> personVosPage = personPage.map(p -> CustomMapper.parseObject(p, PersonVO.class));
		
		personVosPage.stream().forEach(item -> {
			item.add(linkTo(methodOn(PersonController.class).findById(item.getKey())).withSelfRel());
		});
		
		Link link = linkTo(methodOn(PersonController.class)
				.findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return assembler.toModel(personVosPage, link);
	}

	public void delete(Long id) {
		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));

		repository.delete(person);
	}

	public PersonVO update(PersonVO req) throws Exception {
		FieldValidator.check(new String[] { "firstName", "lastName", "gender", "key", "enabled" }, req);
		
		Person person = repository.findById(req.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Person with id " + req.getKey() + " not found"));
		 
		person = new Person(person.getId(), req.getFirstName(), req.getLastName(), req.getAddress(), req.getGender(),
				req.getEnabled());
		
		PersonVO res = CustomMapper.parseObject(repository.save(person), PersonVO.class);
		res.add(linkTo(methodOn(PersonController.class).findById(res.getKey())).withSelfRel());
		
		return res;
	}

	public PersonVO insert(PersonVO person) throws Exception {
		FieldValidator.check(new String[] { "firstName", "lastName", "gender" }, person);
		
		Person convertedPerson = CustomMapper.parseObject(person, Person.class);
		
		PersonVO personVO = CustomMapper.parseObject(repository.save(convertedPerson), PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
		
		return personVO;
	}
	
	@Transactional	
	public PersonVO disablePerson(Long id) {
		
		repository.disablePerson(id);
		
		Person person = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Person with id " + id + " not found"));
		
		PersonVO personVO = CustomMapper.parseObject(person, PersonVO.class);
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		return personVO;	
	}

}
