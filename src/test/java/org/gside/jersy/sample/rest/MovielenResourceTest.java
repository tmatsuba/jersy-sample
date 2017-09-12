package org.gside.jersy.sample.rest;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.gside.jersy.sample.resource.MovielenResponse;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class MovielenResourceTest extends MovielenTest {

	@Test
	public void testMovielenResource() throws JsonProcessingException, IOException {

		Client client = ClientBuilder.newBuilder()
				.register(MultiPartFeature.class).build();
		WebTarget target = client.target(BASE_URI);


		// get token
		String hash = "567abb45b9319401d7114ac18812565e";
		Response response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		String response1 = response.readEntity(String.class);
		ObjectMapper om = new ObjectMapper();
		MovielenResponse response2 = om.readValue(response1, MovielenResponse.class);
		assertEquals(200, response.getStatus());
		
		assertNull(response2.getUrl());
		assertNull(response2.getExpireDate());
		assertEquals(hash, response2.getHash());
		assertNotNull(response2.getToken());
		response.close();

		// post movielen data
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		ClassLoader classLoader = MovielenResourceTest.class.getClassLoader();
		URL url = classLoader.getResource("ml-latest-small.zip");
		File file = new File(url.getFile());
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		multiPart.bodyPart(fileDataBodyPart);
		multiPart.field("tokenId", response2.getToken());

		
		response = target.path("/rest/movielen/post").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));
		String response3 = response.readEntity(String.class);
		MovielenResponse response4 = om.readValue(response3, MovielenResponse.class);
		assertEquals(200, response.getStatus());
		
		assertEquals("http://cm.gside.org/jsons/" + hash + "/ratings.zip" ,response4.getUrl());
		response.close();

		// post duplicated movielen data
		response = target.path("/rest/movielen/post").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));
		
		String response5 = response.readEntity(String.class);
		Info response6 = om.readValue(response5, Info.class);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		assertEquals("your file already generated:  http://cm.gside.org/jsons/567abb45b9319401d7114ac18812565e/ratings.zip" ,response6.message);
		response.close();
		
		// get token(normal.zip)
		hash = "251fa3e8b76d19659e21579f5874fac8";
		response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		String response7 = response.readEntity(String.class);
		MovielenResponse response8 = om.readValue(response7, MovielenResponse.class);
		assertEquals(200, response.getStatus());
		
		assertNull(response8.getUrl());
		assertNull(response8.getExpireDate());
		assertEquals(hash, response8.getHash());
		assertNotNull(response8.getToken());
		response.close();

		// post movielen data(normal.zip)
		multiPart = new FormDataMultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		classLoader = MovielenResourceTest.class.getClassLoader();
		url = classLoader.getResource("normal.zip");
		file = new File(url.getFile());
		fileDataBodyPart = new FileDataBodyPart("file", file,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		multiPart.bodyPart(fileDataBodyPart);
		multiPart.field("tokenId", response8.getToken());

		
		response = target.path("/rest/movielen/post").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));

		String response9 = response.readEntity(String.class);
		MovielenResponse response10 = om.readValue(response9, MovielenResponse.class);
		assertEquals(200, response.getStatus());

		assertEquals("http://cm.gside.org/jsons/" + hash + "/ratings.zip" ,response10.getUrl());
		response.close();

		// get token(ilegal-csv.zip)
		hash = "d47cc22e153ea0507bf3cc27a34962e4";
		response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		String response11 = response.readEntity(String.class);
		MovielenResponse response12 = om.readValue(response11, MovielenResponse.class);
		assertEquals(200, response.getStatus());

		assertNull(response12.getUrl());
		assertNull(response12.getExpireDate());
		assertEquals(hash, response12.getHash());
		assertNotNull(response12.getToken());
		response.close();
		// post movielen data(ilegal-csv.zip)
		multiPart = new FormDataMultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		classLoader = MovielenResourceTest.class.getClassLoader();
		url = classLoader.getResource("ilegal-csv.zip");
		file = new File(url.getFile());
		fileDataBodyPart = new FileDataBodyPart("file", file,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		multiPart.bodyPart(fileDataBodyPart);
		multiPart.field("tokenId", response12.getToken());


		response = target.path("/rest/movielen/post").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));

		String response13 = response.readEntity(String.class);
		MovielenResponse response14 = om.readValue(response13, MovielenResponse.class);
		assertEquals(200, response.getStatus());

		assertEquals("http://cm.gside.org/jsons/" + hash + "/ratings.zip" ,response14.getUrl());
		response.close();
		
		// get token(ilegal-csv2.zip)
		hash = "cc9ce46398272c66115e7a45a8f589e7";
		response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		response11 = response.readEntity(String.class);
		response12 = om.readValue(response11, MovielenResponse.class);
		assertEquals(200, response.getStatus());

		assertNull(response12.getUrl());
		assertNull(response12.getExpireDate());
		assertEquals(hash, response12.getHash());
		assertNotNull(response12.getToken());
		response.close();

		// post movielen data(ilegal-csv2.zip)
		multiPart = new FormDataMultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		classLoader = MovielenResourceTest.class.getClassLoader();
		url = classLoader.getResource("ilegal-csv2.zip");
		file = new File(url.getFile());
		fileDataBodyPart = new FileDataBodyPart("file", file,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		multiPart.bodyPart(fileDataBodyPart);
		multiPart.field("tokenId", response12.getToken());


		response = target.path("/rest/movielen/post").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));

		 response13 = response.readEntity(String.class);
		 response14 = om.readValue(response13, MovielenResponse.class);
		assertEquals(200, response.getStatus());

		assertEquals("http://cm.gside.org/jsons/" + hash + "/ratings.zip" ,response14.getUrl());
		response.close();
		
		
	}
	
	@Test
	public void testTokenResourceIlegal() throws JsonProcessingException, IOException {

		Client client = ClientBuilder.newBuilder()
				.register(MultiPartFeature.class).build();
		WebTarget target = client.target(BASE_URI);


		// get token
		String hash = "fb676f78bcf3aaaf42979b4988dd9709";
		Response response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		String response1 = response.readEntity(String.class);
		ObjectMapper om = new ObjectMapper();
		MovielenResponse response2 = om.readValue(response1, MovielenResponse.class);
		assertEquals(200, response.getStatus());
		
		assertNull(response2.getUrl());
		assertNull(response2.getExpireDate());
		assertEquals(hash, response2.getHash());
		assertNotNull(response2.getToken());
		response.close();

		// post no header movielen data
		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		ClassLoader classLoader = MovielenResourceTest.class.getClassLoader();
		URL url = classLoader.getResource("ml-no-header.zip");
		File file = new File(url.getFile());
		FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file", file,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		multiPart.bodyPart(fileDataBodyPart);
		multiPart.field("tokenId", response2.getToken());

		
		response = target.path("/rest/movielen/post").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));
		String response3 = response.readEntity(String.class);
		Info response4 = om.readValue(response3, Info.class);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		assertEquals("header not found." ,response4.message);
		response.close();

		// TODO empty (no rating.csv)
		// TODO data empty

		// TODO different hash file
		// TODO ratings.csvが複数ある
		// get token(ilegal-csv2.zip)
		hash = "89ff53a8149e33406d17fe9cc00b1404";
		response = target.path("/rest/token/get").queryParam("hash", hash).request().get();
		String response11 = response.readEntity(String.class);
		MovielenResponse response12 = om.readValue(response11, MovielenResponse.class);
		assertEquals(200, response.getStatus());

		assertNull(response12.getUrl());
		assertNull(response12.getExpireDate());
		assertEquals(hash, response12.getHash());
		assertNotNull(response12.getToken());
		response.close();

		// post movielen data(ilegal-csv2.zip)
		multiPart = new FormDataMultiPart();
		multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
		classLoader = MovielenResourceTest.class.getClassLoader();
		url = classLoader.getResource("empty.zip");
		file = new File(url.getFile());
		fileDataBodyPart = new FileDataBodyPart("file", file,
				MediaType.APPLICATION_OCTET_STREAM_TYPE);
		multiPart.bodyPart(fileDataBodyPart);
		multiPart.field("tokenId", response12.getToken());


		response = target.path("/rest/movielen/post").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(multiPart, multiPart.getMediaType()));


		String response13 = response.readEntity(String.class);
		Info response14 = om.readValue(response13, Info.class);
		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		
		assertEquals("raitings.csv is not exists." ,response14.message);
		response.close();
		
	}	


}
