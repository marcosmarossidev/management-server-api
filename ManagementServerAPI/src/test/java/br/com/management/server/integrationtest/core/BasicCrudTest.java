package br.com.management.server.integrationtest.core;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.management.server.data.vo.security.AccountCredentialsVO;
import br.com.management.server.data.vo.security.TokenVO;

public abstract class BasicCrudTest extends JUnitTestConfigurer {
	
	protected static HttpHeaders httpHeaders;

	@Test
	@Order(0)
	public void doAuthenticateAndSaveTokenInHttpHeaders() throws Exception {		
		MvcResult responseObject = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
				.content(mapper.writeValueAsString(new AccountCredentialsVO("sysadmin", "admin234")))
				.contentType(MediaType.APPLICATION_JSON)).andReturn();
		
		TokenVO vo = mapper.readValue(responseObject.getResponse().getContentAsString(), TokenVO.class);
		httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + vo.getAccessToken());
	}
	
	public abstract void testCreateEntity() throws Exception;
	
	public abstract void testFindEntityById() throws Exception;
	
	public abstract void testFindEntities() throws Exception;
	
	public abstract void testUpdateEntity() throws Exception;
	
	public abstract void testDeleteEntityById() throws Exception;
	
}
