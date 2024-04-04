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

import br.com.management.server.data.vo.BookVO;
import br.com.management.server.exception.RequiredFieldException;
import br.com.management.server.mapper.mocks.MockBook;
import br.com.management.server.model.Book;
import br.com.management.server.repositories.BookRepository;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {
	
	MockBook input;
	
	@InjectMocks
	private BookService service;
	
	@Mock
	private BookRepository repository;
	
	@BeforeEach
	void setUp() throws Exception {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Book book = input.mockEntity();
		book.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(book));
		
		BookVO vo = service.findById(1L);
		
		assertBook(vo);
	}

	@Test
	void testFindAll() {
		List<Book> books = input.mockEntityList();
		
		when(repository.findAll()).thenReturn(books);
		
		List<BookVO> bookVOs = service.findAll();
		
		assertFalse(bookVOs.isEmpty());
		assertEquals(14, bookVOs.size());
	}

	@Test
	void testDelete() {
		Book book = input.mockEntity();
		book.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(book));
		
		service.delete(1L);
	}

	@Test
	void testUpdate() throws Exception {		
		Book persisted = input.mockEntity();
		persisted.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(persisted));
		when(repository.save(any(Book.class))).thenReturn(persisted);
		
		BookVO mockVO = input.mockVO();
		mockVO.setKey(1L);
		
		BookVO updateObj = service.update(mockVO);
		
		assertBook(updateObj);
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
		BookVO book = input.mockVO();
		book.setKey(null);
		
		Exception ex = assertThrows(RequiredFieldException.class, () -> {
			service.update(book);
		});
		
		String expectedMessage = "The field key is required";
		String currentMessage = ex.getMessage();
		
		assertEquals(expectedMessage, currentMessage);
	}	

	@Test
	void testInsert() throws Exception {		
		Book persisted = input.mockEntity();
		persisted.setId(1L);
		
		when(repository.save(any(Book.class))).thenReturn(persisted);
		
		BookVO vo = input.mockVO(1);
				
		BookVO insertedObj = service.insert(vo);
		
		assertBook(insertedObj);
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
		BookVO book = input.mockVO();
		book.setAuthor(null);
		
		Exception ex = assertThrows(RequiredFieldException.class, () -> {
			service.insert(book);
		});
		
		String expectedMessage = "The field author is required";
		String currentMessage = ex.getMessage();
		
		assertEquals(expectedMessage, currentMessage);
	}
	
	private void assertBook(BookVO bookVO) {
		assertNotNull(bookVO);
		assertNotNull(bookVO.getKey());
		assertNotNull(bookVO.getLinks());
		
		assertEquals(bookVO.getLinks().toString(), "</books/1>;rel=\"self\"");
	}
	
}
