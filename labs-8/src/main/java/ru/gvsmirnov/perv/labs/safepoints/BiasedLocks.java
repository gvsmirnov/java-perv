package ru.gvsmirnov.perv.labs.safepoints;

import java.util.concurrent.locks.LockSupport;
import java.util.stream.Stream;

public class BiasedLocks {

    private static synchronized void contend() {
        LockSupport.parkNanos(100_000);
    }

    // Run with: -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCDetails
    // Notice that there are a lot of stop the world pauses, but no actual garbage collections
    // This is because PrintGCApplicationStoppedTime actually shows all the STW pauses

    // To see what's happening here, you may use the following arguments:
    // -XX:+PrintSafepointStatistics  -XX:PrintSafepointStatisticsCount=1
    // It will reveal that all the safepoints are due to biased lock revocations.

    // Biased locks are on by default, but you can disable them by -XX:-UseBiasedLocking
    // It is quite possible that in the modern massively parallel world, they should be
    // turned back off by default

    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(5_000); // Because of BiasedLockingStartupDelay

        Stream.generate(() -> new Thread(BiasedLocks::contend))
                .limit(10)
                .forEach(Thread::start);
    }

}
