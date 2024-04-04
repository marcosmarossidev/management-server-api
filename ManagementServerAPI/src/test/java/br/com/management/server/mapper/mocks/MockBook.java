package br.com.management.server.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.management.server.data.vo.BookVO;
import br.com.management.server.model.Book;

public class MockBook {

	public Book mockEntity() {
		return mockEntity(0);
	}

	public List<Book> mockEntityList() {
		List<Book> books = new ArrayList<>();
		
        for (int i = 0; i < 14; i++) {
        	books.add(mockEntity(i));
        }
        
        return books;
	}
	
	public BookVO mockVO() {
		return mockVO(0);
	}

	private Book mockEntity(Integer number) {
		Book book = new Book();
        book.setAuthor("Test" + number);
        
        Date launch = new Date();
        launch.setTime(0);
        
        book.setLaunchDate(launch);
        book.setTitle("Title" + number);
        book.setId(number.longValue());
        book.setPrice(2.00 + number);
        
        return book;
	}

	public BookVO mockVO(Integer number) {
		BookVO bookVO = new BookVO();
        bookVO.setAuthor("Test" + number);
        
        Date launch = new Date();
        launch.setTime(0);
        
        bookVO.setLaunchDate(launch);
        bookVO.setTitle("Title" + number);
        bookVO.setKey(number.longValue());
        bookVO.setPrice(2.00 + number);
        
		return bookVO;
	}
	
}
