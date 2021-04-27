package com.umar.apps.collections.map;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * In ConcurrentHashMap Retrievals reflect the results of the most
 * recently completed update operations holding upon their onset.
 * For aggregate operations such as putAll and clear, concurrent retrievals
 * may reflect insertion or removal of only some entries.
 *
 * it uses volatile semantics for get(key). In case when Thread1 calls put(key1, value1)
 * and right after that Thread2 calls get(key1), Thread2 wouldn't wait Thread1 to finish its put,
 * they are not synchronized with each other and Thread2 can get old associated value.
 * But if put(key1, value1) was finished in Thread1 before Thread2 tries to get(key1) then
 * Thread2 is guaranteed to get this update (value1).
 *
 * ConcurrentHashMap is the out-of-box ready ConcurrentMap implementation.
 *
 * For better performance, it consists of an array of nodes as table buckets
 * (used to be table segments prior to Java 8) under the hood, and mainly uses
 * Compare and Swap operations during updating.
 *
 * The table buckets are initialized lazily, upon the first insertion.
 * Each bucket can be independently locked by locking the very first node in the bucket.
 * Read operations do not block, and update contentions are minimized.
 *
 * The number of segments required is relative to the number of threads accessing
 * the table so that the update in progress per segment would be no more than one most of time.
 *
 * ConcurrentMap guarantees memory consistency on key/value operations
 * in a multi-threading environment.
 *
 * Actions in a thread prior to placing an object into a ConcurrentMap
 * as a key or value happen-before actions subsequent to the access or removal
 * of that object in another thread.
 */
public class ConcurrentMapTest {

    @Test
    public void givenHashMap_whenSumParallel_then_error() throws Exception {
        Map<String, Integer> map = new HashMap<>();
        List<Integer> sumList = parallelSum(map, 100);
        assertNotEquals(1, sumList.stream().distinct().count());
        long wrongResultCount = sumList.stream().filter(num -> num != 100).count();
        assertTrue(wrongResultCount > 0);
    }

    @Test
    public void givenConcurrentMap_whenSumParallel_thenCorrect()
            throws Exception {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        List<Integer> sumList = parallelSum(map, 1000);

        assertEquals(1, sumList
                .stream()
                .distinct()
                .count());
        long wrongResultCount = sumList
                .stream()
                .filter(num -> num != 100)
                .count();

        assertEquals(0, wrongResultCount);
    }

    @Test
    public void givenMaps_whenGetPut500KTimes_thenConcurrentMapFaster()
            throws Exception {
        Map<String, Object> hashtable = new Hashtable<>();
        Map<String, Object> synchronizedHashMap =
                Collections.synchronizedMap(new HashMap<>());
        Map<String, Object> concurrentHashMap = new ConcurrentHashMap<>();

        long hashtableAvgRuntime = timeElapseForGetPut(hashtable);
        long syncHashMapAvgRuntime =
                timeElapseForGetPut(synchronizedHashMap);
        long concurrentHashMapAvgRuntime =
                timeElapseForGetPut(concurrentHashMap);

        assertTrue(hashtableAvgRuntime > concurrentHashMapAvgRuntime);
        assertTrue(syncHashMapAvgRuntime > concurrentHashMapAvgRuntime);
    }

    @Test
    public void givenConcurrentHashMap_whenPutWithNullKey_thenThrowsNPE() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        assertThrows(NullPointerException.class, ()->map.put(null, 1234)) ;
    }

    @Test
    public void givenConcurrentHashMap_whenPutNullValue_thenThrowsNPE() {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        assertThrows(NullPointerException.class, () -> map.put("test", null));
    }

    @Test
    public void givenKeyPresent_whenComputeRemappingNull_thenMappingRemoved() {
        Map<String, Object> concurrentMap = new ConcurrentHashMap<>();
        Object oldValue = new Object();
        concurrentMap.put("test", oldValue);
        concurrentMap.compute("test", (s, o) -> null);

        assertNull(concurrentMap.get("test"));
    }



    private long timeElapseForGetPut(Map<String, Object> map)
            throws InterruptedException {
        ExecutorService executorService =
                Executors.newFixedThreadPool(4);
        long startTime = System.nanoTime();
        for (int i = 0; i < 4; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 500_000; j++) {
                    int value = ThreadLocalRandom
                            .current()
                            .nextInt(10000);
                    String key = String.valueOf(value);
                    map.put(key, value);
                    map.get(key);
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        return (System.nanoTime() - startTime) / 500_000;
    }

    private List<Integer> parallelSum(Map<String, Integer> map, int executionTimes) throws InterruptedException {
        List<Integer> sumList = new ArrayList<>(1000);
        for (int i = 0; i < executionTimes; i++) {
            map.put("test", 0);
            ExecutorService executorService =
                    Executors.newFixedThreadPool(4);
            for (int j = 0; j < 10; j++) {
                executorService.execute(() -> {
                    for (int k = 0; k < 10; k++)
                        map.computeIfPresent(
                                "test",
                                (key, value) -> value + 1
                        );
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
            sumList.add(map.get("test"));
        }
        return sumList;
    }
}
