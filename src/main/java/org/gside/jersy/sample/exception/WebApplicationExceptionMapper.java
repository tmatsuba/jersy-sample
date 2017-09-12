package org.gside.jersy.sample.exception;

import java.util.HashMap;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * WebApplicationExceptionが発生した時のResponseを生成するクラス
 * 
 * @author matsuba
 *
 */
@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

	@Override
	public Response toResponse(WebApplicationException exception) {
		HashMap<String, String> map = new HashMap<>();
		map.put("message", exception.getMessage());
		return Response.status(exception.getResponse().getStatus())
				.entity(map)
				.type(MediaType.APPLICATION_JSON).
				build();
	}
}
