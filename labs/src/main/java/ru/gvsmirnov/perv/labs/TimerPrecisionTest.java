package ru.gvsmirnov.perv.labs;

import org.openjdk.jmh.logic.BlackHole;
 
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
 
public class TimerPrecisionTest {
 
 
    private static final long MIN_STEP = 10;
    private static final long DECREASE_STEP = 10;
    private static final long TOTAL_SLEEP_DURATION = TimeUnit.SECONDS.toNanos(5);
    private static final long MAX_ALLOWED_DIFFERENCE = TimeUnit.SECONDS.toNanos(5);
 
    public static void main(String[] args) {
 
        estimatePrecision(new Pauser.Sleeper());
 
        estimatePrecision(new Pauser.Burner(Pauser.Burner.estimateTokensPerNano()));
 
    }
 
    private static void estimatePrecision(Pauser pauser) {
 
        out("Estimating precision of %s", pauser);
 
        long step = TimeUnit.SECONDS.toNanos(1);
 
        while(step >= MIN_STEP) {
            if(!checkPrecision(step, pauser)) {
                break;
            }
 
            step /= DECREASE_STEP;
        }
 
        out("Approximate precision of %s: %s", pauser, annotate(step * DECREASE_STEP));
    }
 
    private static boolean checkPrecision(long step, Pauser pauser) {
 
        out("Testing resolution: %s...", annotate(step));
 
        long startMillis = System.currentTimeMillis();
        long elapsedNanoTime = 0;
        long shouldHaveElapsed = 0;
 
        for(long slept = 0; slept < TOTAL_SLEEP_DURATION; slept += step) {
            long startNanos  = System.nanoTime();
            pauser.pause(step);
            long endNanos  = System.nanoTime();
 
            elapsedNanoTime += (endNanos - startNanos);
            shouldHaveElapsed += step;
 
            long diff = Math.abs(shouldHaveElapsed - elapsedNanoTime);
            if(diff > MAX_ALLOWED_DIFFERENCE) {
                out("Cumulative error has exceeded allowed threshold, aborting." +
                    "\n\tElapsed time: %s" +
                    "\n\tExpected time: %s",
                    annotate(elapsedNanoTime), annotate(shouldHaveElapsed)
                );
                return false;
            }
        }
 
        long endMillis = System.currentTimeMillis();
 
 
        long elapsedMilliTime = TimeUnit.MILLISECONDS.toNanos(endMillis - startMillis);
 
        out("Elapsed time:" +
                "\n\tAccording to currentTimeMillis(): %s" +
                "\n\tAccording to nanoTime(): %s" +
                "\n\tExpected: %s\n",
                annotate(elapsedMilliTime), annotate(elapsedNanoTime), annotate(shouldHaveElapsed)
        );
 
        return true;
    }
 
    private static interface Pauser {
        void pause(long nanos);
 
        class Sleeper implements Pauser {
 
            @Override
            public void pause(long nanos) {
                LockSupport.parkNanos(nanos);
            }
 
            @Override
            public String toString() {
                return "parking";
            }
        }
 
        class Burner implements Pauser {
 
            private final double tokensPerNano;
 
            public Burner(double tokensPerNano) {
                this.tokensPerNano = tokensPerNano;
            }
 
            @Override
            public void pause(long nanos) {
                BlackHole.consumeCPU((long) (tokensPerNano * nanos));
            }
 
            @Override
            public String toString() {
                return "the system timer)";
            }
 
            private static final long TRUSTED_TOKENS_NUMBER = TimeUnit.SECONDS.toNanos(20);
 
            public static double estimateTokensPerNano() {
                out("Estimating tokens/nano (could take a while)...");
                long start = System.nanoTime();
                BlackHole.consumeCPU(TRUSTED_TOKENS_NUMBER);
                long end = System.nanoTime();
 
                double ratio = (double) TRUSTED_TOKENS_NUMBER / ((double) end - (double) start) ;
                out("Tokens/nano: %.3f", ratio);
                return ratio;
            }
        }
    }
 
    public static TimeUnit getUnit(long nanos) {
 
        for(TimeUnit unit : TimeUnit.values()) {
            long current = unit.convert(nanos, TimeUnit.NANOSECONDS);
            if(current < 1000) {
                return unit;
            }
        }
 
        return TimeUnit.NANOSECONDS;
    }
 
    public static String annotate(long nanos) {
        TimeUnit unit = getUnit(nanos);
 
        long fullPart = unit.convert(nanos, TimeUnit.NANOSECONDS);
 
        long remainder = nanos - unit.toNanos(fullPart);
        long one = unit.toNanos(1);
 
        int fractionalPart = (int) (1000.0 * (double) remainder / (double) one);
 
        return fullPart + "." + fractionalPart + " " + unit;
    }
 
    private static void out(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
 
}
