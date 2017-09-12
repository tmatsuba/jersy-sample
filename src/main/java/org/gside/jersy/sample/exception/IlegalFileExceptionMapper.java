package org.gside.jersy.sample.exception;

import java.util.HashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * 入力が不正であった場合のResponseを生成するクラス
 * 
 * @author matsuba
 *
 */
@Provider
public class IlegalFileExceptionMapper implements ExceptionMapper<IllegalInputException> {

	public Response toResponse(IllegalInputException exception) {
		HashMap<String, String> map = new HashMap<>();
		map.put("message", exception.getMessage());
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(map)
				.type(MediaType.APPLICATION_JSON).
				build();
	}
}
