package br.com.management.server.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.management.server.data.vo.security.AccountCredentialsVO;
import br.com.management.server.data.vo.security.TokenVO;
import br.com.management.server.integrationtest.core.JUnitTestConfigurer;

public class AuthControllerTest extends JUnitTestConfigurer {
	
	private static HttpHeaders headers = new HttpHeaders();
	
	@Test
	@Order(1)
	public void testAuthentication() throws Exception {
		AccountCredentialsVO credentials = new AccountCredentialsVO();
		credentials.setUsername("sysadmin");
		credentials.setPassword("admin234");
		
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
				.content(mapper.writeValueAsString(credentials))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		TokenVO vo = mapper.readValue(contentAsString, TokenVO.class);
		assertTokenVO(vo);
		
		headers.add("Authorization", "Bearer " + vo.getRefreshToken());
	}	
	
	@Test
	@Order(2)
	public void testRefreshToken() throws Exception {		
		MvcResult andReturn = mockMvc.perform(MockMvcRequestBuilders.put("/auth/refresh/sysadmin")
				.headers(headers))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String contentAsString = andReturn.getResponse().getContentAsString();
		TokenVO vo = mapper.readValue(contentAsString, TokenVO.class);
		
		assertTokenVO(vo);
	}
	
	@Test
	@Order(3)
	public void whenInvalidCredentialsLoginIsNotMade() throws Exception {		
		AccountCredentialsVO credentials = new AccountCredentialsVO();
		credentials.setUsername("sysadmin");
		credentials.setPassword("admin2342");
		
		mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
				.content(mapper.writeValueAsString(credentials))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isForbidden());
	}
	
	@Test
	@Order(4)
	public void whenExpiredTokenRefreshIsNotMade() throws Exception {		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6WyJBRE1JTiJdLCJpYXQiOjE3MTI2ODI0MzYsImV4cCI6MTcxMjY4NjAzNiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdCIsInN1YiI6InN5c2FkbWluIn0.QDkb9p4XdDKOfUnpKgQzGufQ0TCSXhy49PU0NREi-0A");
		
		mockMvc.perform(MockMvcRequestBuilders.put("/auth/refresh/sysadmin")
				.headers(headers))
				.andDo(print()).andExpect(status().isForbidden());
	}
	
	@Test
	@Order(5)
	public void whenInvalidUserRefreshIsNotMade() throws Exception {		
		mockMvc.perform(MockMvcRequestBuilders.put("/auth/refresh/sysadmin2")
				.headers(headers))
				.andDo(print()).andExpect(status().isBadRequest());
	}
	
	private void assertTokenVO(TokenVO vo) {
		assertNotNull(vo);
		assertNotNull(vo.getAccessToken());
		assertNotNull(vo.getRefreshToken());
		
		assertEquals("sysadmin", vo.getUserName());
		
		assertTrue(vo.getAuthenticated());		
		assertTrue(vo.getCreated().before(new Date()));
		assertTrue(vo.getExpiration().after(vo.getCreated()));
	}

}
