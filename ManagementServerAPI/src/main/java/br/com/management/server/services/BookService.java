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

	@Autowired
	private PagedResourcesAssembler<BookVO> assembler;
	
	public BookVO findById(Long id) {
		Book book = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
		
		BookVO bookVO = CustomMapper.parseObject(book, BookVO.class);
		bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		
		return bookVO;
	}

	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
		Page<Book> bookPage = repository.findAll(pageable);
		
		Page<BookVO> bookVosPage = bookPage.map(b -> CustomMapper.parseObject(b, BookVO.class));
		
		bookVosPage.stream().forEach(item -> {
			item.add(linkTo(methodOn(BookController.class).findById(item.getKey())).withSelfRel());
		});
		
		Link link = linkTo(methodOn(BookController.class)
				.findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		return assembler.toModel(bookVosPage, link);
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
