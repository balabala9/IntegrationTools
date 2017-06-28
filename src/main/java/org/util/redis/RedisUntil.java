package org.util.redis;


import redis.clients.jedis.*;

import java.util.*;

/**
 * Created by li on 17-6-26.
 */
public class RedisUntil {


    private static Map<Integer, JedisPool> singleRedisMap = new HashMap<>();

    private static ShardedJedisPool shardedJedisPool;

    private static JedisCluster jedisCluster;

    private static JedisPoolConfig initJedisPoolConfig() {

        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxIdle(PropertiesStr.maxIdle);
        config.setMaxTotal(PropertiesStr.maxActive);
        config.setMaxWaitMillis(PropertiesStr.maxWait);


        return config;

    }

    //集群redis

    public static void initRedisCluter() {

        HashSet<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();

        for (Map.Entry<String, Integer> entry : PropertiesStr.cluterHostMap.entrySet()) {

            HostAndPort hostAndPort = new HostAndPort(entry.getKey().split(":")[0], entry.getValue());
            jedisClusterNodes.add(hostAndPort);
        }

        jedisCluster = new JedisCluster(jedisClusterNodes);
    }

    public static JedisCluster getJedisClusterObject() {
        return jedisCluster;
    }

    //分布式redis
    public static void initRedisPool() {

        List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();

        for (Map.Entry<String, Integer> entry : PropertiesStr.cluterHostMap.entrySet()) {

            JedisShardInfo jedisShardInfo = new JedisShardInfo(entry.getKey().split(":")[0], entry.getValue());

            list.add(jedisShardInfo);
        }
        shardedJedisPool = new ShardedJedisPool(initJedisPoolConfig(), list);
    }

    public static ShardedJedis getRedisObject() {
        return shardedJedisPool.getResource();
    }

    public static void returnRedisObject(ShardedJedis shardedJedis) {
        shardedJedisPool.returnResourceObject(shardedJedis);
    }

    //单机redis
    public static void initSingleRedis() {

        int counter = 0;

        for (Map.Entry<String, Integer> entry : PropertiesStr.singleHostMmap.entrySet()) {

            JedisPool jedisPool = new JedisPool(initJedisPoolConfig(), entry.getKey().split(":")[0], entry.getValue());

            singleRedisMap.put(counter, jedisPool);

            counter++;

        }
    }

    public static Jedis getRedisObject(int hostCounter) {
        return singleRedisMap.get(hostCounter).getResource();
    }

    public static void returnRedisOject(Jedis jedis, Integer counter) {

        singleRedisMap.get(counter).returnResourceObject(jedis);
    }

    public static JedisPool getJedisPool(int hostCounter) {
        return singleRedisMap.get(hostCounter);
    }

    public static Map<Integer, JedisPool> getJedisPoolMap() {
        return singleRedisMap;
    }

    public static void jedisPoolDestory() {

        for (Integer i : singleRedisMap.keySet()) {
            singleRedisMap.get(i).destroy();
        }
    }
}
