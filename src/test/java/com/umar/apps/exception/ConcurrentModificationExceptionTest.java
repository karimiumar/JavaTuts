package com.umar.apps.exception;


import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConcurrentModificationExceptionTest {

    /**
     * Essentially, the ConcurrentModificationException is used to fail-fast
     * when something we are iterating on is modified.
     */
    @Test
    public void given_list_when_modified_during_iteration_then_throws_exception() {
        List<Integer> integers = new ArrayList<>();
        integers.add(2);
        integers.add(4);
        integers.add(6);
        assertThrows(ConcurrentModificationException.class, () -> integers.forEach(integers::remove));
    }


    @Test
    public void givenIterating_whenUsingIteratorRemove_thenNoError()  {
        List<Integer> integers = newArrayList(1, 2, 3);
        integers.removeIf(integer -> integer == 2);
        assertThat(integers).containsExactly(1, 3);
    }

    @Test
    public void givenIterating_whenUsingRemovalList_thenNoError()  {
        List<Integer> integers = newArrayList(1, 2, 3);
        List<Integer> toRemove = newArrayList();
        for (Integer integer : integers) {
            if (integer == 2) {
                toRemove.add(integer);
            }
        }
        integers.removeAll(toRemove);
        assertThat(integers).containsExactly(1, 3);
    }

    @Test
    public void whenUsingRemoveIf_thenRemoveElements() {
        Collection<Integer> integers = newArrayList(1, 2, 3);
        integers.removeIf(i -> i == 2);
        assertThat(integers).containsExactly(1, 3);
    }

    @Test
    public void whenUsingStream_thenRemoveElements() {
        Collection<Integer> integers = newArrayList(1, 2, 3);
        List<String> collected = integers
                .stream()
                .filter(i -> i != 2)
                .map(Object::toString)
                .collect(toList());
        assertThat(collected).containsExactly("1", "3");
    }
}
