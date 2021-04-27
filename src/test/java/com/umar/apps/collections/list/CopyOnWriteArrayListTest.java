package com.umar.apps.collections.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.*;

/**
 * The design of the <code>CopyOnWriteArrayList</code> uses an interesting technique to make it thread-safe
 * without a need for synchronization. When we are using any of the modify methods – such as
 * <code>add()</code> or <code>remove()</code> – the whole content of the
 * <code>CopyOnWriteArrayList</code>is copied into the new internal copy.
 *
 * Due to this simple fact, we can iterate over the list in a safe way,
 * even when concurrent modification is happening.
 *
 * Its content is an exact copy of data that is inside an <code>ArrayList</code>
 * from the time when the Iterator was created.
 * Even if in the meantime some other thread adds or removes an element from the list,
 * that modification is making a fresh copy of the data that will be used
 * in any further data lookup from that list.
 */
public class CopyOnWriteArrayListTest {
    CopyOnWriteArrayList<Integer> numbers = new CopyOnWriteArrayList<>(new Integer[]{1, 3, 5, 8});

    @Test
    public void given_CopyOnWriteArrayList_after_Iterator_created_any_numberAddedTo_numbers_then_only_initial_values_present() {
        Iterator<Integer> iterator = numbers.iterator();//A copy of ArrayList is created at this point.
        numbers.add(10);
        List<Integer> result = new LinkedList<>();
        iterator.forEachRemaining(result::add);
        assertThat(result).containsOnly(1,3,5,8);
    }

    @Test
    public void given_CopyOnWriteArrayList_when_newIteratorCreated_afterAddingNumber_then_No_will_be_present(){
        numbers.add(10);
        Iterator<Integer> iterator = numbers.iterator();//iterator created after adding 10. A copy will be created here
        numbers.add(14);
        List<Integer> result = new LinkedList<>();
        iterator.forEachRemaining(result::add);
        assertThat(result).containsOnly(1,3,5,8,10);
    }

    /**
     * CopyOnWriteArrayList#COWIterator throws UnsupportedOperationException 
     * for set(), add() and remove() methods of COWIterator
     * 
     */
    @Test
    public void whenIterateOverItAndTryToRemoveAddOrSetElement_thenShouldThrowException() {
        ListIterator<Integer> iterator = (ListIterator<Integer>) numbers.iterator();
        iterator.next();
        Assertions.assertThrows(UnsupportedOperationException.class, iterator::remove);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> iterator.add(10));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> iterator.set(78));
    }
}
