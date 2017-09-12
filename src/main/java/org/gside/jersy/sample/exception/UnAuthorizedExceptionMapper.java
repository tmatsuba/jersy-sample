package org.gside.jersy.sample.exception;

import java.util.HashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * 許可のない操作を行った時のResponseを生成するクラス
 * 
 * @author matsuba
 *
 */
@Provider
public class UnAuthorizedExceptionMapper implements ExceptionMapper<UnAuthorizedException> {

	public Response toResponse(UnAuthorizedException exception) {
		HashMap<String, String> map = new HashMap<>();
		map.put("message", exception.getMessage());
		return Response.status(Response.Status.UNAUTHORIZED)
				.entity(map)
				.type(MediaType.APPLICATION_JSON).
				build();
	}
}
