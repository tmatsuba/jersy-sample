package org.gside.jersy.sample.cache;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import cm.assignment.config.Config;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redisへのコネクションプールを作成するクラス
 * 
 * @author matsuba
 *
 */
public class JedisProducer {
	@Inject @Config("jedis.host")
	private String host;
    
    private JedisPool jedisPool;
    @Singleton
    public @Produces JedisPool getJedisPool(){
        jedisPool = new JedisPool(new JedisPoolConfig(), host);
        return jedisPool;
    }
 
    public void detroy(@Disposes JedisPool jedisPool){
        jedisPool.destroy();
    }
}