package com.umar.apps.lambdas;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class LambdaVariables {

    private volatile boolean run = true;
    private int start = 0;

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) {
        new LambdaVariables().workaroundMultithreading();
        //System.out.println(new LambdaVariables().incrementer().get());
        new LambdaVariables().workaroundSingleThread();
    }

    Supplier<Integer> incrementer(int start) {
        return  () -> start; //can't modify local variable inside lambda
    }

    Supplier<Integer> incrementer() {
        return () -> ++start; //instance variables can be modified inside lambda
    }

    public void localVariableMultithreading() {
        executorService.execute(() -> {
            while (run) {
                System.out.println("Running");
            }
        });
        run = false;
    }

    // Always Avoid this workaround
    public void workaroundSingleThread() {
        int [] holder = new int[]{2};
        IntStream sums = IntStream.of(1, 2, 3).map( val -> val + holder[0]);
        holder[0] = 0;//On commenting this line the sum is 12. Otherwise, it's 6
        System.out.println(sums.sum());
    }

    // Always Avoid this workaround
    // What value are we summing here? It depends on how long our simulated processing takes.
    // If it's short enough to let the execution of the method terminate before the other
    // thread is executed it'll print 6, otherwise, it'll print 12.
    public void workaroundMultithreading() {
        int [] holder = new int[] {2} ;
        Runnable runnable = () -> System.out.println(
                IntStream.of(1, 2, 3).map(val -> val + holder[0]).sum()
        );

        new Thread(runnable).start();
        //simulate some processing
        try{
            Thread.sleep(new Random().nextInt(3) * 1000L);
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        holder[0] = 0;
    }

}
