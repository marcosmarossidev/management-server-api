package br.com.management.server.integrationtest.wrappers;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.management.server.data.vo.BookVO;

public class BookEmbeddedVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("bookVOList")
	private List<BookVO> books;
	
	public BookEmbeddedVO() {
		
	}

	public List<BookVO> getBooks() {
		return books;
	}

	public void setBooks(List<BookVO> books) {
		this.books = books;
	}

}
