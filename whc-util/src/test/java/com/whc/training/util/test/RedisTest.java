package com.whc.training.util.test;

import com.alibaba.fastjson.JSON;
import com.whc.training.util.test.properties.LocalProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * redis单元测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年05月12 18:29
 */
@Slf4j
public class RedisTest {

    private static List<HostAndPort> redisClusterNodes;

    private static String redisPassword;

    static {
        final LocalProperties localProperties = LocalProperties.getLocalProperties();
        redisClusterNodes = Arrays.stream(localProperties.getProperty("redis.clusterNodes").split(",")).map(s ->
                new HostAndPort(s.split(":")[0], Integer.parseInt(s.split(":")[1]))
        ).collect(Collectors.toList());
        redisPassword = localProperties.getProperty("redis.password");
    }

    /**
     * redis五大类型：String：字符串；Hash：哈希，类似java里的Map；List：列表；Set：集合；Zset：sorted set，有序集合
     */
    @Test
    public void testJedis() throws Exception {
        Jedis jedis = new Jedis(redisClusterNodes.get(0));
        jedis.auth(redisPassword);
        // key
        Set<String> keySet = jedis.keys("*");
        log.info("keys * : {}", keySet);

        // String
        String v1 = jedis.getSet("k1", "v1");
        log.info("old v1: {}", v1);
        // 同时为多个键设置值
        jedis.mset("k1", "v1", "k2", "v2", "k3", "v3");

        for (int i = 2; i < 100; i++) {
            jedis.set("k" + i, "v" + i);
        }

        // Hash
        

        // List
        String listKey = "mylist";
        if (!jedis.lrange(listKey, 0, 0).isEmpty()) {
            // 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
            jedis.ltrim(listKey, -1, 0);
        }
        // 将一个或多个值插入到列表的表头
        jedis.lpush(listKey, "v1", "v2", "v3", "v4", "v5");
        // 返回列表key中指定区间内的元素，区间以偏移量start和stop指定
        List<String> list = jedis.lrange(listKey, 0, -1);
        log.info("list: {}", JSON.toJSONString(list));

        // Set
        String setKey = "orders";
        // 返回集合 key 的基数(集合中元素的数量)
        long setSize = jedis.scard(setKey);
        if (setSize > 0) {
            // 移除并返回集合中的n个随机元素
            Set<String> popSet = jedis.spop(setKey, setSize);
            log.info("popSet: {}", popSet);
        }
        // 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略
        long addedSetSize = jedis.sadd(setKey, "od001", "od002");
        log.info("addedSetSize: {}", addedSetSize);
        // 返回集合 key 中的所有成员
        log.info("smembers: {}", jedis.smembers(setKey));

        // Zset
    }
}
