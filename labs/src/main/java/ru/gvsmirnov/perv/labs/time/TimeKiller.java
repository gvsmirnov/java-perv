package ru.gvsmirnov.perv.labs.time;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public interface TimeKiller {

    void kill(long nanos);

    class Parker implements TimeKiller {
        @Override
        public void kill(long nanos) {
            LockSupport.parkNanos(nanos);
        }
    }

    class Sleeper implements TimeKiller {
        @Override
        public void kill(long nanos) {
            try {
                TimeUnit.NANOSECONDS.sleep(nanos);
            } catch (InterruptedException ignored) {}
        }
    }

    class Burner implements TimeKiller {
        @Override
        public void kill(long nanos) {
            long endTime = System.nanoTime() + nanos;
            while(System.nanoTime() < endTime); // TODO: check if DCE is really sparing this loop thanks to the syscall
        }
    }

    class BlackHole implements TimeKiller {

        private final double tokensPerNano;

        public BlackHole(double tokensPerNano) {
            this.tokensPerNano = tokensPerNano;
        }

        @Override
        public void kill(long nanos) {
            org.openjdk.jmh.logic.BlackHole.consumeCPU((long) (tokensPerNano * nanos));
        }


        private static final long A_LOT_OF_TOKENS = TimeUnit.SECONDS.toNanos(20);

        public static double estimateTokensPerNano() {
            long start = System.nanoTime();
            org.openjdk.jmh.logic.BlackHole.consumeCPU(A_LOT_OF_TOKENS);
            long end = System.nanoTime();

            double ratio = ((double) A_LOT_OF_TOKENS) / (end - start) ;
            return ratio;
        }
    }



}
