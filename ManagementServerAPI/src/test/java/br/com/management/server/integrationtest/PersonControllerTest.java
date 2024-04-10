package br.com.management.server.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.management.server.data.vo.PersonVO;
import br.com.management.server.integrationtest.core.BasicCrudTest;
import br.com.management.server.mapper.mocks.MockPerson;

public class PersonControllerTest extends BasicCrudTest {

	private MockPerson mock = new MockPerson();

	@Override
	@Test
	@Order(1)
	public void testCreateEntity() throws Exception {
		PersonVO personVO = mock.mockVO(1);

		MvcResult responseObject = mockMvc
				.perform(MockMvcRequestBuilders.post("/person").content(mapper.writeValueAsString(personVO))
						.headers(httpHeaders).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		PersonVO vo = mapper.readValue(responseObject.getResponse().getContentAsString(), PersonVO.class);

		assertNotNull(vo);

		assertEquals("Addres Test1", vo.getAddress());
		assertEquals(1, vo.getKey());
	}

	@Override
	@Test
	@Order(2)
	public void testFindEntityById() throws Exception {
		MvcResult responseObject = mockMvc.perform(MockMvcRequestBuilders.get("/person/" + 1).headers(httpHeaders))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		PersonVO vo = mapper.readValue(responseObject.getResponse().getContentAsString(), PersonVO.class);

		assertEquals("Addres Test1", vo.getAddress());
		assertEquals(1, vo.getKey());
		assertTrue(vo.getEnabled());
	}

	@Override
	@Test
	@Order(3)
	public void testFindEntities() throws Exception {
		MvcResult responseObject = mockMvc.perform(MockMvcRequestBuilders.get("/person").headers(httpHeaders))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		List<PersonVO> people = mapper.readValue(responseObject.getResponse().getContentAsString(),
				new TypeReference<List<PersonVO>>() {});

		assertNotNull(people);
		assertFalse(people.isEmpty());
	}

	@Override
	@Test
	@Order(4)
	public void testUpdateEntity() throws Exception {
		PersonVO personVO = mock.mockVO(1);
		personVO.setAddress("Rua dos Amarelos, n 80");

		MvcResult responseObject = mockMvc
				.perform(MockMvcRequestBuilders.put("/person").content(mapper.writeValueAsString(personVO))
						.headers(httpHeaders).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		PersonVO vo = mapper.readValue(responseObject.getResponse().getContentAsString(), PersonVO.class);

		assertNotNull(vo);
		assertEquals(1, vo.getKey());
		assertEquals("Rua dos Amarelos, n 80", vo.getAddress());
	}
	
	@Test
	@Order(5)
	public void testDisablePersonByID() throws Exception {
		MvcResult responseObject = mockMvc.perform(MockMvcRequestBuilders.patch("/person/" + 1).headers(httpHeaders))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		PersonVO vo = mapper.readValue(responseObject.getResponse().getContentAsString(), PersonVO.class);

		assertNotNull(vo);
		assertEquals(1, vo.getKey());
		assertFalse(vo.getEnabled());
	}

	@Override
	@Test
	@Order(6)
	public void testDeleteEntityById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/person/" + 1).headers(httpHeaders)).andDo(print())
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(7)
	public void whenRequestNotContainsToken() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/person")).andDo(print()).andExpect(status().isForbidden())
				.andReturn();
	}

	@Test
	@Order(8)
	public void whenNotContainsPersonInGet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/person/" + 1000).headers(httpHeaders)).andDo(print())
				.andExpect(status().isNotFound()).andReturn();
	}

	@Test
	@Order(9)
	public void whenNotContainsPersonInDelete() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/person/" + 1000).headers(httpHeaders)).andDo(print())
				.andExpect(status().isNotFound()).andReturn();
	}

	@Test
	@Order(10)
	public void whenNotContainsPersonInUpdate() throws Exception {
		PersonVO personVO = mock.mockVO(2);

		mockMvc.perform(MockMvcRequestBuilders.put("/person").content(mapper.writeValueAsString(personVO))
				.headers(httpHeaders).contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isNotFound());
	}

}
