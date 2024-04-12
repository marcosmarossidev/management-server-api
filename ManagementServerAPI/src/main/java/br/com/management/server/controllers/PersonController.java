package br.com.management.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.management.server.controllers.advice.ErrorMessage;
import br.com.management.server.core.MediaType;
import br.com.management.server.data.vo.PersonVO;
import br.com.management.server.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/person")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController {

	@Autowired
	private PersonService service;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Finds a person", description = "Finds a persoe", tags = { "People" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonVO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public PersonVO findById(@PathVariable(name = "id") Long id) {
		return service.findById(id);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Finds all people", description = "Finds all people", tags = { "People" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public PagedModel<EntityModel<PersonVO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		
		var sortDirection = direction.equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		
		return service.findAll(pageable);
	}
	
	@GetMapping(value = "findPersonByName/{firstName}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Finds people by first name", description = "Finds people by first name", tags = { "People" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public PagedModel<EntityModel<PersonVO>> findPeopleByName(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			@PathVariable(name = "firstName") String firstName) {
		
		var sortDirection = direction.equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		
		return service.findPersonByName(firstName, pageable);
	}

	@DeleteMapping(path = "/{id}")
	@Operation(summary = "Delete a person", description = "Delete a person", tags = { "People" }, responses = {
			@ApiResponse(responseCode = "204", description = "No Content"),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML }, 
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Update a person's details", description = "Update a person's details", tags = { "People" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public PersonVO update(@RequestBody PersonVO person) throws Exception {
		return service.update(person);
	}
	
	@Operation(summary = "Create a new person", description = "Create a new person", tags = { "People" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	@PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	public PersonVO insert(@RequestBody PersonVO person) throws Exception {
		return service.insert(person);
	}
	
	@PatchMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(summary = "Disable a specific person by your id", description = "Disable a specific person by your id", tags = { "People" }, responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = PersonVO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", description = "Not Found", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)) }), })
	public PersonVO disablePerson(@PathVariable(name = "id") Long id) {
		return service.disablePerson(id);
	}

}
