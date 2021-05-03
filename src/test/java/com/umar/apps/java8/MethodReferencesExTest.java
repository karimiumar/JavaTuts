package com.umar.apps.java8;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.umar.apps.java8.MethodReferencesEx.merge;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodReferencesExTest {

    @Test
    void given_TwoStrings_when_merge_using_lambda_then_StringsMerged() {
        String merged = merge("Hello", " World!", (a, b) -> a + b);
        assertEquals("Hello World!", merged);
    }

    @Test
    void given_TwoStrings_when_merge_using_staticMethodReference_then_StringsMerged() {
        String merged = merge("Hello", " World!", MethodReferencesEx::appendStrings);
        assertEquals("Hello World!", merged);
    }

    @Test
    void given_TwoStrings_And_Object_when_merge_using_objectMethodReference_then_Strings_Merged() {
        MethodReferencesEx obj = new MethodReferencesEx();
        String merged = merge("Hello", " World!", obj::appendString);
        assertEquals("Hello World!", merged);
    }

    @Test
    void given_TwoStrings_when_merge_using_Arbitrary_Object_then_Strings_Merged() {
        String merged = merge("Hello", " World!", String::concat);
        assertEquals("Hello World!", merged);
    }

    @Test
    void given_List_of_Person_when_sorted_using_ComparisonProvider$compareByAge_then_sorted_By_Age() {
        List<Person> roster = Person.createRoster();
        Person [] unsortedPeople = roster.toArray(Person[]::new);
        Arrays.sort(unsortedPeople, Person.ComparisonProvider::compareByAge);
        System.out.println(Arrays.toString(unsortedPeople));
    }

    @Test
    void given_List_of_Person_when_sorted_using_ComparisonProvider$compareByName_then_sorted_By_Name() {
        List<Person> roster = Person.createRoster();
        Person [] unsortedPeople = roster.toArray(Person[]::new);
        Arrays.sort(unsortedPeople, Person.ComparisonProvider::compareByName);
        System.out.println(Arrays.toString(unsortedPeople));
    }

    @Test
    void given_List_of_Person_when_sorted_using_lambda_then_sorted_By_Age() {
        List<Person> roster = Person.createRoster();
        Person [] unsortedPeople = roster.toArray(Person[]::new);
        Arrays.sort(unsortedPeople, (p1, p2) -> Person.compareByAge(p1, p2));
        System.out.println(Arrays.toString(unsortedPeople));
    }

    @Test
    void given_List_of_Person_when_sorted_using_methodReference_then_sorted_By_Age() {
        List<Person> roster = Person.createRoster();
        Person [] unsortedPeople = roster.toArray(Person[]::new);
        Arrays.sort(unsortedPeople, Person::compareByAge);
        System.out.println(Arrays.toString(unsortedPeople));
    }
}
