package org.gside.jersy.sample.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * EntityManagerを操作するためのクラス。
 * 
 * @author matsuba
 *
 */
public class EntityManagerHelper {

    private static final EntityManagerFactory emf; 
    private static final ThreadLocal<EntityManager> threadLocal;

    static {
        emf = Persistence.createEntityManagerFactory("cm_assignment");      
        threadLocal = new ThreadLocal<EntityManager>();
    }

    /**
     * EntityManagerを取得する。
     * 
     * @return
     */
    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();
        
        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    /**
     * EntityManagerをクローズする。
     */
    public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        if (em != null) {
            em.close();
            threadLocal.set(null);
        }
    }

    /**
     * トランザクションを開始する。
     */
    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    /**
     * トランザクションをロールバックする。
     */
    public static void rollback() {
        getEntityManager().getTransaction().rollback();
    }

    /**
     * コミットする。
     */
    public static void commit() {
        getEntityManager().getTransaction().commit();
    } 
}

