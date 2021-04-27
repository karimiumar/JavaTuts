package com.umar.apps.collections.map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class ConcurrentHashmapIsThreadSafeTest {

    private static final Logger LOG = LogManager.getLogger(ConcurrentHashmapIsThreadSafeTest.class);

    public void concurrentHashMapIsThreadSafe() throws InterruptedException {
        final Map<Integer, String> map = new ConcurrentHashMap<>();
        final int targetKey = 0b1111_1111_1111_1111;//65535
        final String targetValue = "v";
        map.put(targetKey, targetValue);

        new Thread(() -> {
            IntStream.range(0, targetKey).forEach(key -> map.put(key, "someValue"));
        }).start();
        Thread.sleep(2000);
        do {
            LOG.warn("Size of map: {}", map.size());
        } while (!targetValue.equals(map.get(targetKey)));
    }

    @Test
    public void given_map_when_resizes_does_not_throw_exception() throws InterruptedException{
        concurrentHashMapIsThreadSafe();
        Assertions.assertTrue(true);
    }
}
