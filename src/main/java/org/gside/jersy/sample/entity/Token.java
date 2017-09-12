package org.gside.jersy.sample.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * TOKENテーブル用のEntity
 * 
 * @author matsuba
 *
 */
@Entity
@NamedQueries({
		@NamedQuery(name = "Token.byTokenId",
		query = "Select t from Token t where t.tokenId = ?1 and t.expireDate > ?2"), 
		@NamedQuery(name = "Token.byHash",
		query = "Select t from Token t where t.hash = ?1 and t.expireDate > ?2"), 
})
public class Token {
	@Id
	private String tokenId;
	@Column
	private Timestamp expireDate;
	@Column
	private String hash;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Timestamp getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String value) {
		this.tokenId = value;
	}
}
