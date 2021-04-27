package com.umar.apps.map;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*if the whole map is synchronized for get/put operations,
adding threads won't improve throughput because the bottleneck will be the synchronized blocks.
You can then write a piece of code with a synchronizedMap that shows that adding threads does not help
because the map uses several locks, and assuming you have more than one core on your machine,
adding threads will improve throughput

    The example below outputs the following:


    Synchronized one thread: 30
    Synchronized multiple threads: 96
    Concurrent one thread: 219
    Concurrent multiple threads: 142

    So you can see that the synchronized version is more than 3 times slower
    under high contention (16 threads) whereas the
    concurrent version is almost twice as fast with multiple threads as with a single thread.

    It is also interesting to note that the ConcurrentMap has a non-negligible overhead in a single threaded situation.

    This is a very contrived example, with all the possible problems due to micro-benchmarking
    (first results should be discarded anyway). But it gives a hint at what happens.

*/
public class MapTest {
    static final int SIZE = 1000000;
    static final int THREADS = 16;
    static final ExecutorService executor = Executors.newFixedThreadPool(THREADS);

    public static void main(String[] args) throws Exception{

        for (int i = 0; i < 10; i++) {
            System.out.println("Concurrent one thread");
            addSingleThread(new ConcurrentHashMap<>());
            System.out.println("Concurrent multiple threads");
            addMultipleThreads(new ConcurrentHashMap<>());
            System.out.println("Synchronized one thread");
            addSingleThread(Collections.synchronizedMap(new HashMap<>()));
            System.out.println("Synchronized multiple threads");
            addMultipleThreads(Collections.synchronizedMap(new HashMap<>()));
        }
        executor.shutdown();
    }

    private static void addSingleThread(Map<Integer, Integer> map) {
        long start = System.nanoTime();
        for (int i = 0; i < SIZE; i++) {
            map.put(i, i);
        }
        //System.out.println(map.size()); //use the result
        long end = System.nanoTime();
        System.out.println("time with single thread: " + (end - start) / 1000000);
    }

    private static void addMultipleThreads(final Map<Integer, Integer> map) throws Exception {
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < THREADS; i++) {
            final int start = i;
            runnables.add(() -> {
            //Trying to have one runnable by bucket
                for (int j = start; j < SIZE; j += THREADS) {
                    map.put(j, j);
                }
            });
        }
        List<Future<?>> futures = new ArrayList<> ();
        long start = System.nanoTime();
        for (Runnable r : runnables) {
            futures.add(executor.submit(r));
        }
        for (Future<?> f : futures) {
            f.get();
        }
        long end = System.nanoTime();
        System.out.println("time with multiple threads: " + (end - start) / 1000000);
    }
}
