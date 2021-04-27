package com.umar.apps.collections.map;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * We want to keep our events sorted using the eventTime field.
 * To achieve this using the ConcurrentSkipListMap, we need to pass a Comparator
 * to its constructor while creating an instance of it
 */
public class EventWindowSort {

    private final ConcurrentSkipListMap<ZonedDateTime, String> events
            = new ConcurrentSkipListMap<>(Comparator.comparingLong(value -> value.toInstant().toEpochMilli()));

    public void acceptEvent(Event event) {
        events.put(event.getEventTime(), event.getContent());
    }

    /**
     * The most notable pros of the ConcurrentSkipListMap are the methods
     * that can make an immutable snapshot of its data in a lock-free way.
     * Other writing threads can add new events to the ConcurrentSkipListMap
     * without any need to do explicit locking.
     *
     * To get all events that arrived within the past minute,
     * we can use the tailMap() method and pass the time from which we want to get elements
     *
     */
    ConcurrentNavigableMap<ZonedDateTime, String> getEventsFromLastMinute() {
        return events.tailMap(ZonedDateTime.now().minusMinutes(1));
    }

    ConcurrentNavigableMap<ZonedDateTime, String> getEventsOlderThan1Minute() {
        return events.headMap(ZonedDateTime.now().minusMinutes(1));
    }
}
