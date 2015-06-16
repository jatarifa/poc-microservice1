package com.capgemini.poc.microservice1;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Microservice1Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
public class Microservice1ApplicationTests 
{
	@Value("${local.server.port}")
	private int port;
	
	@Test
	public void generatePassword() 
	{
		ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:" + this.port + "/crypto/generaPassword?key=1",
														  				HttpMethod.GET, null, String.class);
		
		Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());

		String pwd = entity.getBody();
		System.out.println(pwd);
	}
	
	@Test
	public void ciferDecifer() 
	{
		ResponseEntity<String> entity = new TestRestTemplate().exchange("http://localhost:" + this.port + "/crypto/cifra?key=2&texto=prueba",
														  				HttpMethod.GET, null, String.class);
		
		Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());

		String cifrado = entity.getBody();
		System.out.println(cifrado);
		
		entity = new TestRestTemplate().exchange("http://localhost:" + this.port + "/crypto/descrifra?key=2&texto=" + cifrado,
												 HttpMethod.GET, null, String.class);
		
		Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());

		String descifrado = entity.getBody();
		System.out.println(descifrado);
		
		Assert.assertEquals("prueba", descifrado);
	}
}
