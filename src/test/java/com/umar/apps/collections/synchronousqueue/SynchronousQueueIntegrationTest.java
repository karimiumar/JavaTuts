package com.umar.apps.collections.synchronousqueue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SynchronousQueueIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(SynchronousQueueIntegrationTest.class);


    @Test
    public void givenTwoThreads_whenWantToExchangeUsingLockGuardedVariable_thenItSucceed() throws InterruptedException {
        //given
        ExecutorService executor = Executors.newFixedThreadPool(2);
        AtomicInteger sharedState = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable producer = () -> {
            int producedElement = ThreadLocalRandom.current().nextInt();
            LOG.debug("Saving an element: " + producedElement + " to the exchange point");
            sharedState.set(producedElement);
            countDownLatch.countDown();
        };

        Runnable consumer = () -> {
            try {
                countDownLatch.await();
                int consumedElement = sharedState.get();
                LOG.debug("consumed an element: " + consumedElement + " from the exchange point");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        //when
        executor.execute(producer);
        executor.execute(consumer);

        //then
        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
        assertEquals(countDownLatch.getCount(), 0);
    }

    @Test
    public void givenTwoThreads_whenWantToExchangeUsingSynchronousQueue_thenItSucceed() throws InterruptedException {
        //given
        ExecutorService executor = Executors.newFixedThreadPool(2);
        final SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        Runnable producer = () -> {
            Integer producedElement = ThreadLocalRandom.current().nextInt();
            try {
                LOG.debug("Saving an element: " + producedElement + " to the exchange point");
                queue.put(producedElement);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        Runnable consumer = () -> {
            try {
                Integer consumedElement = queue.take();
                LOG.debug("consumed an element: " + consumedElement + " from the exchange point");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        };

        //when
        executor.execute(producer);
        executor.execute(consumer);

        //then
        executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        executor.shutdown();
        assertEquals(queue.size(), 0);
    }
}
