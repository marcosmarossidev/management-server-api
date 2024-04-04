package br.com.management.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.management.server.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
