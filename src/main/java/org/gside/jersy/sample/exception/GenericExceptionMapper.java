package org.gside.jersy.sample.exception;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import .slf4j.Logger;

/**
 * 全ての例外が発生した時のResponseを生成するクラス
 * 
 * @author matsuba
 *
 */
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	@Inject
	Logger log;

	@Override 
	public Response toResponse(Throwable ex) {
		log.debug(ex.getStackTrace().toString());
		log.debug(ex.getCause().toString());
		log.debug(ex.getMessage());
		return Response.status(500)
				.entity("Server Error")
				.type(MediaType.APPLICATION_JSON)
				.build();	
	}
 
}
