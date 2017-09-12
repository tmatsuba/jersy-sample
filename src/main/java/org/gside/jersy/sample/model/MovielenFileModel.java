package org.gside.jersy.sample.model;

import java.util.Optional;

import javax.persistence.NoResultException;

import org.gside.jersy.sample.entity.MovielenFile;


/**
 * MOVIELENFILEテーブルを操作するクラス。
 * 
 * @author matsuba
 *
 */
public class MovielenFileModel extends Model {

	/**
	 * ハッシュ値からレコードを取得する。
	 * 
	 * @param hash
	 * @return
	 */
	public Optional<MovielenFile> getByHash(String hash) {
		try {
			MovielenFile file = getEntityManger().createNamedQuery("MovielenFile.byHash", MovielenFile.class)
					.setParameter(1, hash).getSingleResult();
			return Optional.of(file);
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}
	
	/**
	 * レコードを保存する。
	 * 
	 * @param hash
	 * @param path
	 * @return
	 */
	public MovielenFile save(String hash, String path) {
		log.debug("model's Transaction Object:" + getEntityManger().getTransaction().hashCode());
		MovielenFile movielenFile = new MovielenFile();
		movielenFile.setHash(hash);
		movielenFile.setUrl(path);
		getEntityManger().persist(movielenFile);
		

		return movielenFile;
	}
}
