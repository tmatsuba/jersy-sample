package org.gside.jersy.sample.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.gside.jersy.sample.resource.MovielenResponse;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenResourceTest extends MovielenTest {


	@Test
	public void testTokenResource() throws JsonProcessingException, IOException {
		WebTarget target = client.target(BASE_URI);

		// hash is null
		Response response = target.path("/rest/token/get").request().get();
		String responce1 = response.readEntity(String.class);
		ObjectMapper om = new ObjectMapper();
		Info info = om.readValue(responce1, Info.class);
		assertEquals(400, response.getStatus());
		assertEquals("Please specify hash value.", info.message);
		response.close();
		

		// hash is not null
		String hash = "fuga";
		response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		String response2 = response.readEntity(String.class);
		
		MovielenResponse token = om.readValue(response2, MovielenResponse.class);
		assertEquals(200, response.getStatus());
		
		
		assertEquals(null, token.getUrl());
		assertEquals(null, token.getExpireDate());
		assertEquals(hash, token.getHash());
		assertNotNull(token.getToken());
		response.close();

		// hash duplicated
		response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		String responce3 = response.readEntity(String.class);
		
		info = om.readValue(responce3, Info.class);
		assertEquals(401, response.getStatus());
		assertEquals("Your specified file already reserved to process.", info.message);
		response.close();
		
		// differrent hash can process
		hash = "hoge";
		response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		String response4 = response.readEntity(String.class);
		token = om.readValue(response4, MovielenResponse.class);
		assertEquals(200, response.getStatus());
		
		assertEquals(null, token.getUrl());
		assertEquals(null, token.getExpireDate());
		assertEquals(hash, token.getHash());
		assertNotNull(token.getToken());
		response.close();
		
	}


}
