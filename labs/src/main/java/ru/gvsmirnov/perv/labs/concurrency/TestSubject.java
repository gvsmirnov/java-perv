package ru.gvsmirnov.perv.labs.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestSubject {

    private static final int DEFAULT_ITERATIONS = 10_000 * 2;
    private volatile boolean finished;
    private int value = 0;

    void executedOnCpu0() {
        value = 10;
        finished = true;
    }

    void executedOnCpu1() {
        while(!finished);
        assert value == 10;
    }

    public static void main(String[] args) throws InterruptedException {
        int iterations = args.length > 0 ? Integer.valueOf(args[0]) : DEFAULT_ITERATIONS;
        final ExecutorService executor = Executors.newFixedThreadPool(2);

        for(int i = 0; i < iterations; i ++) {
            final TestSubject poorGuy = new TestSubject();
            final CountDownLatch latch = new CountDownLatch(2);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    poorGuy.executedOnCpu1();
                    latch.countDown();
                }
            });

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    poorGuy.executedOnCpu0();
                    latch.countDown();
                }
            });

            latch.await();
        }

        executor.shutdown();
    }

}