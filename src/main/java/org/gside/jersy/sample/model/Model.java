package org.gside.jersy.sample.model;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;

import org.gside.jersy.sample.transaction.EntityManagerHelper;

/**
 * モデルの基底クラス。
 * 
 * @author matsuba
 *
 */
@ApplicationScoped
public class Model {

	@Inject
	protected Logger log;

	
	/**
	 * EntityManagerを取得する。
	 * @return
	 */
	protected EntityManager getEntityManger() {
		EntityManager em = EntityManagerHelper.getEntityManager();
		return em;
	}
}
