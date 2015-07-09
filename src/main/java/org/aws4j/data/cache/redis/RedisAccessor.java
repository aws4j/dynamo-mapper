package org.aws4j.data.cache.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisAccessor {

	private final static Logger Log = LoggerFactory.getLogger( RedisAccessor.class );

	private JedisPool jedisPool;
	private int timeoutMillis;

	// TODO
	private ExecutorService pool = Executors.newCachedThreadPool();

	public RedisAccessor( RedisConfiguration conf ) {

		Log.debug(conf.getHost());
		Log.debug(conf.getPort().toString());
		Log.debug(conf.getTimeoutMillis().toString());

		jedisPool = new JedisPool(new JedisPoolConfig(), conf.getHost(),
				conf.getPort());
		this.timeoutMillis = conf.getTimeoutMillis();
	}

	// TODO
	public List<String> get(final String[] keys) {

		Future<List<String>> getTask = pool
				.submit(new Callable<List<String>>() {
					@Override
					public List<String> call() throws Exception {

						Jedis jedis = null;
						try {
							jedis = jedisPool.getResource();
							return jedis.mget(keys);
						} catch (Exception e) {
							Log.warn("Redis get exception.", e);
							return null;
						} finally {
							jedisPool.returnResource(jedis);
						}
					}
				});

		try {
			return getTask.get(timeoutMillis, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			Log.warn("Redis request timeout.");
		}
		return new ArrayList<String>();
	}

	public String set(final String key, final String value, final int timeout) {

		Future<String> setTask = pool.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {

				Jedis jedis = null;

				try {
					String retCode = null;
					jedis = jedisPool.getResource();
					if (timeout > 0) {
						retCode = jedis.setex(key, timeout, value);
					} else {
						retCode = jedis.set(key, value);
					}
					return retCode;
				} catch (Exception e) {
					e.printStackTrace();
					return "";
				} finally {
					jedisPool.returnResource(jedis);
				}
			}
		});

		try {
			return setTask.get(timeoutMillis, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			Log.warn("Redis request timeout.");
			return "";
		}
	}

	public Long msetnx(final String[] keysValues) {

		Future<Long> setTask = pool.submit(new Callable<Long>() {

			@Override
			public Long call() throws Exception {

				Jedis jedis = null;

				try {
					jedis = jedisPool.getResource();
					Long retCode = jedis.msetnx(keysValues);
					return retCode;
				} catch (Exception e) {
					e.printStackTrace();
					return 0L;
				} finally {
					jedisPool.returnResource(jedis);
				}
			}
		});

		try {
			return setTask.get(timeoutMillis, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			Log.warn("Redis request timeout.");
			return 0L;
		}
	}
}
