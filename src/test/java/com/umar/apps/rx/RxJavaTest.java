package com.umar.apps.rx;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

public class RxJavaTest {

    @Test
    public void printHelloWorld(){
        Flowable.just("Hello World").subscribe(System.out::println);
    }

    @Test
    public void when_just_then_produces_Observable() {
        AtomicReference<String> result = new AtomicReference<>("");
        Observable<String> observable = Observable.just("Hello");
        observable.subscribe(result::set);
        Assertions.assertEquals(result.get(), "Hello");
    }
}
