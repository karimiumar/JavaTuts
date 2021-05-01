package com.umar.apps.java8;

import java.util.Collection;
import java.util.function.Supplier;

public class ReferenceConstructor {

    public static <T, SOURCE extends Collection<T>, DEST extends Collection<T>>
        DEST transferElements (SOURCE sourceCollection, Supplier<DEST> collectionFactory) {
        DEST result = collectionFactory.get();
        result.addAll(sourceCollection);
        return result;
    }
}
