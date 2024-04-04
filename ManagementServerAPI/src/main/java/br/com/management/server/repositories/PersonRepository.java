package br.com.management.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.management.server.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
	
}
