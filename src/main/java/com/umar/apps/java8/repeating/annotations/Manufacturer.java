package com.umar.apps.java8.repeating.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Cars.class)
public @interface Manufacturer {
    String value();
}
