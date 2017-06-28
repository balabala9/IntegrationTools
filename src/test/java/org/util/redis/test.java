package org.util.redis;

import org.junit.Test;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by li on 17-6-26.
 */
public class test {

    @Test
    public void redisConnct() {

        PropertiesStr propertiesStr = new PropertiesStr("config.properties");

        RedisUntil.initRedisPool();

        ShardedJedis redisObject = RedisUntil.getRedisObject();

        redisObject.set("ac","a");

        System.out.println(redisObject.get("ac"));

    }

    public void redisCluter() {

        PropertiesStr propertiesStr = new PropertiesStr("config.properties");

        RedisUntil.initRedisCluter();

        JedisCluster jedisCluster = RedisUntil.getJedisClusterObject();

        jedisCluster.set("ab","a");

        System.out.println(jedisCluster.get("ab"));

    }
}
