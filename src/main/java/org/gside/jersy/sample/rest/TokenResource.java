package org.gside.jersy.sample.rest;


import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.gside.jersy.sample.resource.MovielenResponse;
import org.slf4j.Logger;

import org.gside.jersy.sample.entity.MovielenFile;
import org.gside.jersy.sample.service.MovieLenService;
import org.gside.jersy.sample.service.TokenService;
import org.gside.jersy.sample.transaction.Transaction;

/**
 * Token用リソースクラス
 * 
 * @author matsuba
 *
 */
@Path("/token")
public class TokenResource {

	@Inject	
	private TokenService tokenService;
	@Inject 
	private MovieLenService movielenService;
    @Inject 
    private Logger logger;

	/**
 	 * uploadする前に、ハッシュ値を渡して既に変換済みのファイルが存在するかをチェックする。
	 * 存在する場合はURLを返却する。
	 * 存在しない場合はupload時に利用するtokenを返却する。
	 * @param hash
	 * @return
	 */
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	@Transaction
	public MovielenResponse get(@QueryParam("hash") String hash)  {

		if (hash == null || hash.isEmpty()) {
			throw new BadRequestException("Please specify hash value.");
		}
		logger.info("hash:" + hash);
		Optional<MovielenFile> movieLenfile = movielenService.getFile(hash);
		
		MovielenResponse token = new MovielenResponse();
		token.setHash(hash);
		if (! movieLenfile.isPresent() ) {
			String tokenId = tokenService.getToken(hash);
			logger.debug("file is not exists. generate token:" + tokenId);
			token.setToken(tokenId);
		} else {
			logger.debug("file is exists. return file url:" + movieLenfile.get().getUrl());
			token.setUrl(movieLenfile.get().getUrl());
		};
		
		return token;
	}
	


}
