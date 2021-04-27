package com.umar.apps.collections.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class HashmapIsNotThreadSafeTest {

    /**
     * One thread adds new keys to the map. The other thread constantly checks the targetKey is present.
     *
     * If count those exceptions, I get around 200 000
     */
    public void hashMapIsNotThreadSafe() {
        final Map<Integer, String> map = new HashMap<>();
        final int targetKey = 0b1111_1111_1111_1111;//65535
        final String targetValue = "v";
        map.put(targetKey, targetValue);

        new Thread(() -> {
            IntStream.range(0, targetKey).forEach(key -> map.put(key, "someValue"));
        }).start();

        while (true) {
            if(!targetValue.equals(map.get(targetKey))){
                throw new RuntimeException("Hashmap is not Thread-safe");
            }
        }
    }

    @Test
    public void given_hashmap_when_resizes_in_a_then_throws_exception(){
        Assertions.assertThrows(RuntimeException.class, this::hashMapIsNotThreadSafe);
    }
}
