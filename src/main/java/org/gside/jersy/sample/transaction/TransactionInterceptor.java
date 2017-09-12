package org.gside.jersy.sample.transaction;


import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

import org.slf4j.Logger;

/**
 * トランザクションを制御するためのインターセプター
 * 
 * @author matsuba
 *
 */
@Transaction
@Interceptor
public class TransactionInterceptor {

	@Inject
	private Logger log;

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ctx) throws Exception {
		EntityManager em = EntityManagerHelper.getEntityManager();
		Object obj = null;
		try {
			em.getTransaction().begin();
			
			log.debug("method start transaction:" + em.getTransaction().hashCode());
			obj = ctx.proceed();
			log.debug("method end transaction:" + em.getTransaction().hashCode());
			
			em.getTransaction().commit();
			em.close();


		} catch (Exception ex) {
			if (em.isOpen()) {
				em.getTransaction().rollback();
				em.close();
			}
			throw ex;
		} finally {
			if (em.isOpen()) {
				em.getTransaction().rollback();
				em.close();
			}
		}
		return obj;
	}
}

