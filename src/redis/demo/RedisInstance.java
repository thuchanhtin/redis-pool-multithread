package redis.demo;

import java.io.IOException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisInstance {
private static JedisPool pool = null;

public static synchronized Jedis getJedis() throws NumberFormatException, IOException {
    if (pool == null) {
        JedisPoolConfig config = new JedisPoolConfig();        
        pool =  new JedisPool(config, "localhost", 6379, 60*1000);
        System.out.println("RedisInstance init..."+ pool);
    }
    return pool.getResource();
}

public static synchronized void returnResource(Jedis jedis) {
    if (jedis != null) {
        pool.returnResource(jedis);
        System.out.println("RedisInstance returnResource..."+ pool);
    }
}

}
