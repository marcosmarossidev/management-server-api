package br.com.management.server.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.management.server.data.vo.BookVO;
import br.com.management.server.integrationtest.core.BasicCrudTest;
import br.com.management.server.mapper.mocks.MockBook;

public class BookControllerTest extends BasicCrudTest {
	
	private MockBook mock = new MockBook();

	@Override
	@Test
	@Order(1)
	public void testCreateEntity() throws Exception {
		BookVO bookVO = mock.mockVO(1);
		
		MvcResult responseObject = mockMvc.perform(MockMvcRequestBuilders.post("/books")
				.content(mapper.writeValueAsString(bookVO))
				.headers(httpHeaders).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		
		BookVO vo = mapper.readValue(responseObject.getResponse().getContentAsString(), BookVO.class);
		
		assertNotNull(vo);
		
		assertEquals("Title1", vo.getTitle());		
		assertEquals(1, vo.getKey());		
	}

	@Override
	@Test
	@Order(2)
	public void testFindEntityById() throws Exception {
		MvcResult responseObject = mockMvc.perform(MockMvcRequestBuilders.get("/books/" + 1)
				.headers(httpHeaders))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		
		BookVO vo = mapper.readValue(responseObject.getResponse().getContentAsString(), BookVO.class);
		
		assertEquals("Title1", vo.getTitle());		
		assertEquals(1, vo.getKey());		
	}

	@Override
	@Test
	@Order(3)
	public void testFindEntities() throws Exception {
		MvcResult responseObject = mockMvc.perform(MockMvcRequestBuilders.get("/books")
				.headers(httpHeaders))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		
		String contentAsString = responseObject.getResponse().getContentAsString();
		List<BookVO> people = mapper.readValue(contentAsString, new TypeReference<List<BookVO>>() {});
		
		assertNotNull(people);
		assertFalse(people.isEmpty());		
	}
	
	@Override
	@Test
	@Order(4)
	public void testUpdateEntity() throws Exception {
		BookVO bookVO = mock.mockVO(1);
		bookVO.setTitle("Title2");
		
		MvcResult responseObject = mockMvc.perform(MockMvcRequestBuilders.put("/books")
				.content(mapper.writeValueAsString(bookVO))
				.headers(httpHeaders).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		
		BookVO vo = mapper.readValue(responseObject.getResponse().getContentAsString(), BookVO.class);
		
		assertNotNull(vo);
		
		assertEquals(1, vo.getKey());
		assertEquals("Title2", vo.getTitle());		
	}
	
	@Override
	@Test
	@Order(5)
	public void testDeleteEntityById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + 1)
				.headers(httpHeaders))
				.andDo(print()).andExpect(status().isNoContent());
	}	
	
	@Test
	@Order(6)
	public void whenRequestNotContainsToken() throws Exception {		
		mockMvc.perform(MockMvcRequestBuilders.get("/books"))
				.andDo(print()).andExpect(status().isForbidden()).andReturn();
	}
	
	@Test
	@Order(7)
	public void whenNotContainsBookInGet() throws Exception {		
		mockMvc.perform(MockMvcRequestBuilders.get("/books/" + 1000)
				.headers(httpHeaders))
				.andDo(print()).andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	@Order(8)
	public void whenNotContainsBookInDelete() throws Exception {		
		mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + 1000)
				.headers(httpHeaders))
				.andDo(print()).andExpect(status().isNotFound()).andReturn();
	}
	
	@Test
	@Order(9)
	public void whenNotContainsBookInUpdate() throws Exception {
		BookVO bookVO = mock.mockVO(20);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/books")
				.content(mapper.writeValueAsString(bookVO))
				.headers(httpHeaders).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());
	}

}
