package br.com.management.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.management.server.data.vo.PersonVO;
import br.com.management.server.exception.RequiredFieldException;
import br.com.management.server.mapper.mocks.MockPerson;
import br.com.management.server.model.Person;
import br.com.management.server.repositories.PersonRepository;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
	
	MockPerson input;
	
	@InjectMocks
	private PersonService service;
	
	@Mock
	private PersonRepository repository;
	
	@BeforeEach
	void setUp() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Person person = input.mockEntity();
		person.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(person));
		
		PersonVO vo = service.findById(1L);
		
		assertPerson(vo);
	}

	@Test
	void testFindAll() {
		List<Person> people = input.mockEntityList();
		
		when(repository.findAll()).thenReturn(people);
		
		List<PersonVO> personVOs = service.findAll();
		
		assertFalse(personVOs.isEmpty());
		assertEquals(14, personVOs.size());
	}

	@Test
	void testDelete() {
		Person person = input.mockEntity();
		person.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(person));
		
		service.delete(1L);
	}

	@Test
	void testUpdate() throws Exception {		
		Person persisted = input.mockEntity();
		persisted.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(persisted));
		when(repository.save(any(Person.class))).thenReturn(persisted);
		
		PersonVO mockVO = input.mockVO();
		mockVO.setKey(1L);
		
		PersonVO updateObj = service.update(mockVO);
		
		assertPerson(updateObj);
	}
	
	@Test
	void testUpdateWhenRequestIsNull() throws Exception {		
		Exception ex = assertThrows(RequiredFieldException.class, () -> {
			service.update(null);
		});
		
		String expectedMessage = "The request object can't be null";
		String currentMessage = ex.getMessage();
		
		assertEquals(expectedMessage, currentMessage);
	}
	
	@Test
	void testUpdateWhenRequestIsInvalid() throws Exception {
		PersonVO person = input.mockVO();
		person.setKey(null);
		
		Exception ex = assertThrows(RequiredFieldException.class, () -> {
			service.update(person);
		});
		
		String expectedMessage = "The field key is required";
		String currentMessage = ex.getMessage();
		
		assertEquals(expectedMessage, currentMessage);
	}	

	@Test
	void testInsert() throws Exception {		
		Person persisted = input.mockEntity();
		persisted.setId(1L);
		
		when(repository.save(any(Person.class))).thenReturn(persisted);
		
		PersonVO vo = input.mockVO(1);
				
		PersonVO insertedObj = service.insert(vo);
		
		assertPerson(insertedObj);
	}

	@Test
	void testInsertWhenRequestIsNull() throws Exception {		
		Exception ex = assertThrows(RequiredFieldException.class, () -> {
			service.insert(null);
		});
		
		String expectedMessage = "The request object can't be null";
		String currentMessage = ex.getMessage();
		
		assertEquals(expectedMessage, currentMessage);
	}
	
	@Test
	void testInsertWhenRequestIsInvalid() throws Exception {
		PersonVO person = input.mockVO();
		person.setFirstName(null);
		
		Exception ex = assertThrows(RequiredFieldException.class, () -> {
			service.insert(person);
		});
		
		String expectedMessage = "The field firstName is required";
		String currentMessage = ex.getMessage();
		
		assertEquals(expectedMessage, currentMessage);
	}
	
	private void assertPerson(PersonVO personVO) {
		assertNotNull(personVO);
		assertNotNull(personVO.getKey());
		assertNotNull(personVO.getLinks());
		
		assertEquals(personVO.getLinks().toString(), "</person/1>;rel=\"self\"");
	}
	
}
