package br.com.person.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import br.com.person.data.vo.PersonVO;
import br.com.person.model.Person;

public class CustomMapper {
	
	private static ModelMapper mapper = new ModelMapper();
	
	static {
		mapper.createTypeMap(Person.class, PersonVO.class)
			.addMapping(Person::getId, PersonVO::setKey);
		
		mapper.createTypeMap(PersonVO.class, Person.class)
			.addMapping(PersonVO::getKey, Person::setId);
	}
	
	public static <O, D> D parseObject(O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}
	
	public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
		List<D> destinations = new ArrayList<>();
		
		for (O o : origin) {
			destinations.add(mapper.map(o, destination));
		}

		return destinations;
	}
	
}
