package com.whc.training.util.test;

import com.whc.training.util.properties.LocalProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;
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

    @Test
    public void testJedis() throws Exception {
        Jedis jedis = new Jedis(redisClusterNodes.get(0));
        jedis.auth(redisPassword);
        log.info("1");
    }
}
