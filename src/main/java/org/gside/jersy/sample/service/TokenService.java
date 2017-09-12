package org.gside.jersy.sample.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gside.jersy.sample.entity.Token;
import org.gside.jersy.sample.model.TokenModel;
import org.slf4j.Logger;

import cm.assignment.config.Config;
import org.gside.jersy.sample.exception.UnAuthorizedException;

/**
 * Tokenに関連するサービスクラス。
 * 
 * @author matsuba
 *
 */
@ApplicationScoped
public class TokenService {
	
	@Inject
	private TokenModel tokenModel;
	@Inject
	private Logger log;
	@Inject @Config("expire.duration")
	private String expireDuration;

	/**
	 * トークンを取得する。
	 * 
	 * @param hash
	 * @return
	 * @throws UnAuthorizedException
	 */
	public String getToken(String hash) throws UnAuthorizedException {
		List<Token> tokenList = tokenModel.getByHash(hash);
		if (tokenList != null && tokenList.size() > 0) {
			throw new UnAuthorizedException("Your specified file already reserved to process.");
		}
		
		String value = UUID.randomUUID().toString();
	    log.debug("token:" + value);
	    
		Token token = new Token();
		token.setTokenId(value);
		token.setHash(hash);
		LocalDateTime now = LocalDateTime.now();
		token.setExpireDate(Timestamp.valueOf(now.plus(Integer.valueOf(expireDuration), ChronoUnit.MINUTES)));
		tokenModel.save(token);
		
		return token.getTokenId();
	}

}
