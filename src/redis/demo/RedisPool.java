package redis.demo;

import java.io.IOException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    private static final RedisPool instance = new RedisPool();
    private static JedisPool pool = null;
    private RedisPool() {}
    public final static RedisPool getInstance() {
        return instance;
    }
        
    public void connect() {
        System.out.println("Redis pool init...");
        // Create and set a JedisPoolConfig
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // Maximum active connections to Redis instance
        poolConfig.setMaxTotal(10000);
        // Tests whether connection is dead when connection
        // retrieval method is called
        poolConfig.setTestOnBorrow(true);
        /* Some extra configuration */
        // Tests whether connection is dead when returning a
        // connection to the pool
        poolConfig.setTestOnReturn(true);
        // Number of connections to Redis that just sit there
        // and do nothing
        poolConfig.setMaxIdle(5000);
        // Minimum number of idle connections to Redis
        // These can be seen as always open and ready to serve
        poolConfig.setMinIdle(1);
        // Tests whether connections are dead during idle periods
        poolConfig.setTestWhileIdle(true);
        // Maximum number of connections to test in each idle check
        poolConfig.setNumTestsPerEvictionRun(10);
        // Idle connection checking period
        poolConfig.setTimeBetweenEvictionRunsMillis(60000);
        // Create the jedisPool
        pool = new JedisPool(poolConfig, "localhost", 6379);
        System.out.println("Pool connected..."+ pool);
    }
    
    public void release() {
        System.out.println("Pool destroy..."+ pool);
        pool.destroy();
    }
    public synchronized Jedis getJedis() {
        return pool.getResource();
    }
    public static synchronized void returnResource(Jedis jedis) {
        System.out.println("returnResource..."+ pool);
        pool.returnResource(jedis);
    }    
    public void put(String key, String value){
        getJedis().set(key, value);
    }
    public String get(String key){
        return getJedis().get(key);
    }
}
