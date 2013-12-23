package ru.gvsmirnov.perv.labs.time;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public interface TimeKiller {

    /**
     * The contract of this method is that it takes <i>at least</i> <tt>nanos</tt> ns to execute.
     * It is okay to run longer, but not okay to run shorter.
     * @param nanos the minimum number of nanoseconds that this method should take to run
     */
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
                // Thread.sleep(millis, nanos) uses rounding mode HALF_UP, but we need CEIL

                long millis = TimeUnit.NANOSECONDS.toMillis(nanos);

                long remainder = nanos - TimeUnit.MILLISECONDS.toNanos(millis);
                if(remainder > 0)
                    millis ++;

                Thread.sleep(millis);

            } catch (InterruptedException ignored) {}
        }
    }

    /**
     * In effect estimates the resolution of System.nanoTime()
     */
    class Burner implements TimeKiller {
        @Override
        public void kill(long nanos) {
            long endTime = System.nanoTime() + nanos;
            while(System.nanoTime() < endTime);
        }
    }

    // TODO: BlackHole in fact does not perform linearly all the time
    // Nor, for that matter, is the time spent consuming the same number
    // of tokens the same betwen runs
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

            return ((double) A_LOT_OF_TOKENS) / (end - start) ;
        }
    }

}
