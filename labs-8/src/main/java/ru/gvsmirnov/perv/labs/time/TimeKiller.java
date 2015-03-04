package ru.gvsmirnov.perv.labs.time;

import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public abstract class TimeKiller {

    public final void kill(long nanos) {

        long endTime = System.nanoTime() + nanos;
        long remainingTime;

        while((remainingTime = endTime - System.nanoTime()) > 0) {
            tryKill(remainingTime);
        }
    }

    protected abstract void tryKill(long nanosToKill);

    public static class TimedParker extends TimeKiller {
        @Override
        public void tryKill(long nanosToKill) {
            LockSupport.parkNanos(nanosToKill);
        }
    }

    public static class Sleeper extends TimeKiller {
        @Override
        public void tryKill(long nanosToKill) {
            try {
                TimeUnit.NANOSECONDS.sleep(nanosToKill);
            } catch (InterruptedException ignored) {}
        }
    }

    public static class Burner extends TimeKiller {
        @Override
        public void tryKill(long l) {}
    }

    public static class Yielder extends TimeKiller {
        @Override
        public void tryKill(long l) {
            Thread.yield();
        }
    }

    public static class TimedWaiter extends TimeKiller {

        private final Object object = new Object();

        @Override
        public void tryKill(long nanosToKill) {
            long millis = TimeUnit.NANOSECONDS.toMillis(nanosToKill);
            long nanos = nanosToKill % TimeUnit.MILLISECONDS.toNanos(1);

            try {
                synchronized (object) {
                    object.wait(millis, (int) nanos);
                }
            } catch (InterruptedException ignored) {}
        }
    }

    // TODO: BlackHole in fact does not perform linearly all the time
    // Nor, for that matter, is the time spent consuming the same number
    // of tokens the same between runs
    public static class BlackHole extends TimeKiller {

        private final double tokensPerNano;

        public BlackHole(double tokensPerNano) {
            this.tokensPerNano = tokensPerNano;
        }

        @Override
        public void tryKill(long nanosToKill) {
            Blackhole.consumeCPU((long) (tokensPerNano * nanosToKill));
        }


        private static final long A_LOT_OF_TOKENS = TimeUnit.SECONDS.toNanos(20);

        public static double estimateTokensPerNano() {
            long start = System.nanoTime();
            Blackhole.consumeCPU(A_LOT_OF_TOKENS);
            long end = System.nanoTime();

            return ((double) A_LOT_OF_TOKENS) / (end - start) ;
        }
    }

}
