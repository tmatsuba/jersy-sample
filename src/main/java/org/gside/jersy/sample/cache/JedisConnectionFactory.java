package org.gside.jersy.sample.cache;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redisへのコネクションを生成するクラス。
 * 
 * @author matsuba
 *
 */
@RequestScoped
public class JedisConnectionFactory {
 
    @Inject
    private JedisPool jedisPool;
 
    @Produces
    public Jedis getJedis(){
        return jedisPool.getResource();
    }
 
    public void returnResource(@Disposes Jedis jedis){
        jedis.close();
    }
 
 
}
