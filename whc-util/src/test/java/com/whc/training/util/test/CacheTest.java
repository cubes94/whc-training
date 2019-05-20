package com.whc.training.util.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.LinkedHashMap;

/**
 * 缓存测试
 *
 * @author whc
 * @version 1.0.0
 * @since 2019年05月06 11:55
 */
@Slf4j
public class CacheTest {

    @Test
    public void testLRUCache() throws Exception {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 1);
        cache.put(2, 2);
        log.info(cache.get(1) + "");
        cache.put(3, 3);
        log.info(cache.get(2) + "");
        cache.put(4, 4);
        log.info(cache.get(1) + "");
        log.info(cache.get(3) + "");
        log.info(cache.get(4) + "");
    }

}

/**
 * Least Recently Used
 *
 * LRU算法就是当缓存空间满了的时候，将最近最少使用的数据从缓存空间中删除以增加可用的缓存空间来缓存新数据
 */
class LRUCache {

    private LinkedHashMap<Integer, Integer> map;

    private int maxSize;

    public LRUCache(int capacity) {
        this.maxSize = capacity;
        this.map = new LinkedHashMap<>(0, 0.75f, true);
    }

    public int get(int key) {
        return map.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        if (map.size() == maxSize && !map.containsKey(key)) {
            map.remove(map.entrySet().iterator().next().getKey());
        }
        map.put(key, value);
    }
}
