package org.gside.jersy.sample.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

/**
 * MOVIELENFILEテーブル用のEntity
 * 
 * @author matsuba
 *
 */
@Entity
@NamedQuery(name = "MovielenFile.byHash", 
query = "Select f from MovielenFile f where f.hash = ?1")
public class MovielenFile {
	@Id
	private Integer id;
	@Column
	private String hash;
	@Column
	private String url;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
