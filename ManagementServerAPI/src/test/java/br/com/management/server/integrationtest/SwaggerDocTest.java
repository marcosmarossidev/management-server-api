package br.com.management.server.integrationtest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;

import br.com.management.server.integrationtest.core.JUnitTestConfigurer;

public class SwaggerDocTest extends JUnitTestConfigurer {
	
	@Test
	public void checkIfSwaggerIsAvailable() throws Exception {		
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.get("/v3/api-docs"))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		
		String contentAsString = andReturn.getResponse().getContentAsString();
		Map<String, Object> swagger = mapper.readValue(contentAsString, new TypeReference<HashMap<String,Object>>() {});
		
		assertNotNull(swagger);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> tags = (List<Map<String, Object>>) swagger.get("tags");
		
		assertTrue(!tags.isEmpty());
		assertTrue(tags.size() == 4);
	}
}
