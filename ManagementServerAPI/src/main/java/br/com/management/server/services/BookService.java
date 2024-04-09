package br.com.management.server.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.management.server.controllers.BookController;
import br.com.management.server.core.FieldValidator;
import br.com.management.server.data.vo.BookVO;
import br.com.management.server.exception.ResourceNotFoundException;
import br.com.management.server.mapper.CustomMapper;
import br.com.management.server.model.Book;
import br.com.management.server.repositories.BookRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository repository;

	public BookVO findById(Long id) {
		Book book = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
		
		BookVO bookVO = CustomMapper.parseObject(book, BookVO.class);
		bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		
		return bookVO;
	}

	public List<BookVO> findAll() {
		List<BookVO> books = CustomMapper.parseListObjects(repository.findAll(), BookVO.class);
		
		books.stream().forEach(item -> {
			item.add(linkTo(methodOn(BookController.class).findById(item.getKey())).withSelfRel());
		});
		
		return books;
	}

	public void delete(Long id) {
		Book book = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));

		repository.delete(book);
	}

	public BookVO update(BookVO book) throws Exception {
		FieldValidator.check(new String[] { "author", "price", "title", "key" }, book);
		
		Book aux = repository.findById(book.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Book with id " + book.getKey() + " not found"));

		aux.setAuthor(book.getAuthor());		
		aux.setLaunchDate(book.getLaunchDate());
		aux.setPrice(book.getPrice());
		aux.setTitle(book.getTitle());
		
		BookVO bookVO = CustomMapper.parseObject(repository.save(aux), BookVO.class);
		bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
		
		return bookVO;
	}

	public BookVO insert(BookVO book) throws Exception {
		FieldValidator.check(new String[] { "author", "price", "title" }, book);
		
		Book convertedBook = CustomMapper.parseObject(book, Book.class);
		
		BookVO bookVO = CustomMapper.parseObject(repository.save(convertedBook), BookVO.class);
		bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
		
		return bookVO;
	}

}
