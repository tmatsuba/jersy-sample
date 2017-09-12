package org.gside.jersy.sample.rest;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ManagedAsync;
import org.gside.jersy.sample.resource.MovielenResponse;
import org.slf4j.Logger;

import org.gside.jersy.sample.entity.MovielenFile;
import org.gside.jersy.sample.service.MovieLenService;
import org.gside.jersy.sample.transaction.Transaction;

/**
 * 
 * Movielenデータ用リソースクラス
 * 
 * @author matsuba
 *
 */
@ApplicationScoped
@Path("/movielen")
public class MovielenFileResource {

	@Inject
	private MovieLenService movieLenService;
	
    @Inject
    private Logger logger;

    /**
     * movielenファイルをuploadするためのメソッド。
     * 事前に取得したtokenを指定することでupload可能となる。
     * jsonlineに変換後、変換されたファイル取得用のURLを返す。
     * 
     * @param fileInputStream
     * @param tokenId
     * @param contentDispositionHeader
     * @param asyncResponse
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    @POST
	@Path("/post")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Transaction
    @ManagedAsync
	public void post(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("tokenId") String tokenId,
			@FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
			@Suspended final AsyncResponse asyncResponse
    			) throws NoSuchAlgorithmException, IOException {
		if (tokenId == null || tokenId.isEmpty()) {
			throw new BadRequestException("Please specify token.");
		}
    	
    	String url = movieLenService.entry(tokenId, fileInputStream);

    	MovielenResponse responce = new MovielenResponse();
    	responce.setUrl(url);
    	asyncResponse.resume(Response.status(200).entity(responce).build());
    }
	
	/**
	 * 指定したハッシュ値のファイルをjsonline形式に変換したファイルのURLを返す。
	 * 
	 * @param hash
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get")
	public Response get(@QueryParam("hash") String hash) {
		if (hash == null) {
			throw new BadRequestException("Please specify hash value.");
		}
		
		logger.info("querying hash" + hash);
		Optional<MovielenFile> movilenFile = movieLenService.getFile(hash);
		movilenFile.orElseThrow(() -> new NotFoundException("your query hash:" + hash + " is not found"));
		
    	HashMap<String, String> map = new HashMap<>();
    	map.put("url", movilenFile.get().getUrl());
    	return Response.status(200).entity(map).build();
		
	}
}
