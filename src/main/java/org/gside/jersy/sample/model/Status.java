package org.gside.jersy.sample.model;

import javax.inject.Inject;

import redis.clients.jedis.Jedis;

public class Status {
	@Inject Jedis jedis;

	public boolean isExists(String hash) {
		String key = jedis.get(hash);
		return key != null; 
	}

	public void putStatus(String hash) {
		jedis.setex(hash, 60, "");
	}
	
	public void deleteStatus(String hash) {
		jedis.del(hash);
	}
}
