package com.umar.apps.java8;

import java.util.function.BiFunction;

public class MethodReferencesEx {
    public static <T> T merge(T a, T b, BiFunction<T,T,T> merger) {
        return merger.apply(a, b);
    }

    public static String appendStrings(String a, String b) {
        return a + b;
    }

    public String appendString(String a, String b) {
        return a + b;
    }
}
