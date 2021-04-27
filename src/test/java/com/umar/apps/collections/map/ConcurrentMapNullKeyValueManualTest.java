package com.umar.apps.collections.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ConcurrentMapNullKeyValueManualTest {

    private ConcurrentMap<String, Object> concurrentMap;

    @BeforeEach
    public void setup() {
        concurrentMap = new ConcurrentHashMap<>();
    }

    @Test
    public void givenConcurrentHashMap_whenGetWithNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.get(null));
    }

    @Test
    public void givenConcurrentHashMap_whenGetOrDefaultWithNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.getOrDefault(null, new Object()));
    }

    @Test
    public void givenConcurrentHashMap_whenPutWithNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.put(null, new Object()));
    }

    @Test
    public void givenConcurrentHashMap_whenPutNullValue_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.put("test", null));
    }

    @Test
    public void givenConcurrentHashMapAndKeyAbsent_whenPutWithNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.putIfAbsent(null, new Object()));
    }

    @Test
    public void givenConcurrentHashMapAndMapWithNullKey_whenPutNullKeyMap_thenThrowsNPE() {
        Map<String, Object> nullKeyMap = new HashMap<>();
        nullKeyMap.put(null, new Object());
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.putAll(nullKeyMap));
    }

    @Test
    public void givenConcurrentHashMapAndMapWithNullValue_whenPutNullValueMap_thenThrowsNPE() {
        Map<String, Object> nullValueMap = new HashMap<>();
        nullValueMap.put("test", null);
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.putAll(nullValueMap));
    }

    @Test
    public void givenConcurrentHashMap_whenReplaceNullKeyWithValues_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.replace(null, new Object(), new Object()));
    }

    @Test
    public void givenConcurrentHashMap_whenReplaceWithNullNewValue_thenThrowsNPE() {
        Object o = new Object();
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.replace("test", o, null));
    }

    @Test
    public void givenConcurrentHashMap_whenReplaceOldNullValue_thenThrowsNPE() {
        Object o = new Object();
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.replace("test", null, o));
    }

    @Test
    public void givenConcurrentHashMap_whenReplaceWithNullValue_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.replace("test", null));
    }

    @Test
    public void givenConcurrentHashMap_whenReplaceNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.replace(null, "test"));
    }

    @Test
    public void givenConcurrentHashMap_whenReplaceAllMappingNull_thenThrowsNPE() {
        concurrentMap.put("test", new Object());
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.replaceAll((s, o) -> null));
    }

    @Test
    public void givenConcurrentHashMap_whenRemoveNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.remove(null));
    }

    @Test
    public void givenConcurrentHashMap_whenRemoveNullKeyWithValue_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class,() ->concurrentMap.remove(null, new Object()));
    }

    @Test
    public void givenConcurrentHashMap_whenMergeNullKeyWithValue_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.merge(null, new Object(), (o, o2) -> o2));
    }

    @Test
    public void givenConcurrentHashMap_whenMergeKeyWithNullValue_thenThrowsNPE() {
        concurrentMap.put("test", new Object());
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.merge("test", null, (o, o2) -> o2));
    }

    @Test
    public void givenConcurrentHashMapAndAssumeKeyAbsent_whenComputeWithNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.computeIfAbsent(null, s -> s));
    }

    @Test
    public void givenConcurrentHashMapAndAssumeKeyPresent_whenComputeWithNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () -> concurrentMap.computeIfPresent(null, (s, o) -> o));
    }

    @Test
    public void givenConcurrentHashMap_whenComputeWithNullKey_thenThrowsNPE() {
        Assertions.assertThrows(NullPointerException.class, () ->  concurrentMap.compute(null, (s, o) -> o));
    }

    @Test
    public void givenConcurrentHashMap_whenMergeKeyRemappingNull_thenRemovesMapping() {
        Object oldValue = new Object();
        concurrentMap.put("test", oldValue);
        concurrentMap.merge("test", new Object(), (o, o2) -> null);
        assertNull(concurrentMap.get("test"));
    }

    @Test
    public void givenConcurrentHashMapAndKeyAbsent_whenComputeWithKeyRemappingNull_thenRemainsAbsent() {
        concurrentMap.computeIfPresent("test", (s, o) -> null);
        assertNull(concurrentMap.get("test"));
    }

    @Test
    public void givenKeyPresent_whenComputeIfPresentRemappingNull_thenMappingRemoved() {
        Object oldValue = new Object();
        concurrentMap.put("test", oldValue);
        concurrentMap.computeIfPresent("test", (s, o) -> null);
        assertNull(concurrentMap.get("test"));
    }

    @Test
    public void givenKeyPresent_whenComputeRemappingNull_thenMappingRemoved() {
        Object oldValue = new Object();
        concurrentMap.put("test", oldValue);
        concurrentMap.compute("test", (s, o) -> null);
        assertNull(concurrentMap.get("test"));
    }
}
