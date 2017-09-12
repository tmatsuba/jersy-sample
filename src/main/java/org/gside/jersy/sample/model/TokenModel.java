package org.gside.jersy.sample.model;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.gside.jersy.sample.entity.Token;


/**
 * TOKENテーブルを操作するクラス。
 * 
 * @author matsuba
 *
 */
public class TokenModel extends Model{

	/**
	 * レコードを保存する。
	 * @param token
	 * @return
	 */
	public Token save(Token token) {

		log.debug("model's Transaction Object:" + getEntityManger().getTransaction().hashCode());
		getEntityManger().persist(token);

		return token;
	}

	/**
	 * ハッシュ値からレコードを取得する。
	 * 有効時間内のレコードを取得する。
	 * 
	 * @param hash
	 * @return
	 */
	public List<Token> getByHash(String hash) {
		Date date = new Date();
		List<Token> token = getEntityManger().createNamedQuery("Token.byHash", Token.class)
				.setParameter(1, hash).setParameter(2, date).getResultList();
		return token;
	}

	/**
	 * トークンIDからレコードを取得する。
	 * 
	 * @param tokenId
	 * @return
	 */
	public Optional<Token> get(String tokenId) {
		try {
			Date date = new Date();
			Token token = getEntityManger().createNamedQuery("Token.byTokenId", Token.class)
					.setParameter(1, tokenId).setParameter(2, date).getSingleResult();
			return Optional.of(token);
		} catch(NoResultException e){
			return Optional.empty();
		}
	}

}
