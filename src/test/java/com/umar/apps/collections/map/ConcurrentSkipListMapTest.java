package com.umar.apps.collections.map;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ConcurrentSkipListMap is sorted and provides
 *
 * expected average log(n) time cost for the containsKey, get, put and remove operations and their variants
 *
 * ConcurrentSkipListMap isn't so fast, but is useful when you need sorted thread-safe map
 */
public class ConcurrentSkipListMapTest {

    @Test
    public void givenThreadsProducingEvents_whenGetForEventsFromLastMinute_thenReturnThoseEventsInTheLockFreeWay() throws InterruptedException {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        EventWindowSort eventWindowSort = new EventWindowSort();
        int numberOfThreads = 2;
        //when
        Runnable producer = () -> IntStream
                .rangeClosed(0, 100)
                .forEach(index -> eventWindowSort.acceptEvent(new Event(ZonedDateTime
                        .now()
                        .minusSeconds(index), UUID
                        .randomUUID()
                        .toString())));
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(producer);
        }

        Thread.sleep(500);

        ConcurrentNavigableMap<ZonedDateTime, String> eventsFromLastMinute = eventWindowSort.getEventsFromLastMinute();

        long eventsOlderThanOneMinute = eventsFromLastMinute
                .entrySet()
                .stream()
                .filter(e -> e
                        .getKey()
                        .isBefore(ZonedDateTime
                                .now()
                                .minusMinutes(1)))
                .count();
        assertEquals(eventsOlderThanOneMinute, 0);

        long eventYoungerThanOneMinute = eventsFromLastMinute
                .entrySet()
                .stream()
                .filter(e -> e
                        .getKey()
                        .isAfter(ZonedDateTime
                                .now()
                                .minusMinutes(1)))
                .count();

        //then
        assertTrue(eventYoungerThanOneMinute > 0);

        executorService.awaitTermination(1, TimeUnit.SECONDS);
        executorService.shutdown();
    }

    @Test
    public void givenThreadsProducingEvents_whenGetForEventsOlderThanOneMinute_thenReturnThoseEventsInTheLockFreeWay() throws InterruptedException {
        //given
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        EventWindowSort eventWindowSort = new EventWindowSort();
        int numberOfThreads = 2;
        //when
        Runnable producer = () -> IntStream
                .rangeClosed(0, 100)
                .forEach(index -> eventWindowSort.acceptEvent(new Event(ZonedDateTime
                        .now()
                        .minusSeconds(index), UUID
                        .randomUUID()
                        .toString())));

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(producer);
        }

        Thread.sleep(500);

        ConcurrentNavigableMap<ZonedDateTime, String> eventsFromLastMinute = eventWindowSort.getEventsOlderThan1Minute();

        long eventsOlderThanOneMinute = eventsFromLastMinute
                .entrySet()
                .stream()
                .filter(e -> e
                        .getKey()
                        .isBefore(ZonedDateTime
                                .now()
                                .minusMinutes(1)))
                .count();
        assertTrue(eventsOlderThanOneMinute > 0);

        long eventYoungerThanOneMinute = eventsFromLastMinute
                .entrySet()
                .stream()
                .filter(e -> e
                        .getKey()
                        .isAfter(ZonedDateTime
                                .now()
                                .minusMinutes(1)))
                .count();

        //then
        assertEquals(eventYoungerThanOneMinute, 0);

        executorService.awaitTermination(1, TimeUnit.SECONDS);
        executorService.shutdown();
    }

}
