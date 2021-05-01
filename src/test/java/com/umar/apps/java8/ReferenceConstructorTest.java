package com.umar.apps.java8;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.umar.apps.java8.ReferenceConstructor.transferElements;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReferenceConstructorTest {

    @Test
    void given_listOfPeople_when_transferElements_using_lambda_then_Set_of_People_created() {
        List<Person> roster = Person.createRoster();
        Set<Person> rosterSet = transferElements(roster, () -> {return new HashSet<>();});
        assertEquals(4, rosterSet.size());
    }

    @Test
    void given_listOfPeople_when_transferElements_using_constructorRef_then_Set_of_People_created() {
        List<Person> roster = Person.createRoster();
        Set<Person> rosterSet = transferElements(roster, HashSet::new);
        assertEquals(4, rosterSet.size());
    }
}
