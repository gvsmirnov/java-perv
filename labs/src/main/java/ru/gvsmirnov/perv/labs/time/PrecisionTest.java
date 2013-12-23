package ru.gvsmirnov.perv.labs.time;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.concurrent.TimeUnit;

import static ru.gvsmirnov.perv.labs.util.Util.*;
 
public class PrecisionTest {

    private static final long DEFAULT_MAX_RESOLUTION = TimeUnit.SECONDS.toNanos(2);
    private static final long DEFAULT_MIN_RESOLUTION = TimeUnit.NANOSECONDS.toNanos(100);

    @Option(name = "--max", usage = "Max resolution to be tested")
    private long maxResolution = DEFAULT_MAX_RESOLUTION;

    @Option(name = "--min", usage = "Min resolution to be tested")
    private long minResolution = DEFAULT_MIN_RESOLUTION;

    @Option(name = "-v", usage = "Verbose")
    private boolean verbose = false;

    private long estimatePrecision(TimeKiller killer, String name) {
        out("Estimating the precision of %s...", name);

        long left = minResolution, right = maxResolution;
        long lastSuccessfulResolution = -1;


        while(right - left >= minResolution) {
            if(verbose) {
                out("Current interval: (%d, %d)", left, right);
            }

            long currentResolution = (left + right) / 2;
            boolean precise = isPrecise(killer, currentResolution, (int) (maxResolution / currentResolution));

            if(precise) {
                right = currentResolution - 1;
                lastSuccessfulResolution= currentResolution;
            } else {
                left = currentResolution + 1;
            }

        }

        out("Precision of %s is %s", name, annotate(lastSuccessfulResolution));

        return lastSuccessfulResolution;
    }
 
    private boolean isPrecise(TimeKiller timeKiller, long resolution, int iterations) {
 
        if(verbose) {
            out("Testing resolution: %s in %d iterations", annotate(resolution), iterations);
        }

        final long timeToKill = resolution * iterations;
        final long startMillis = System.currentTimeMillis();

        long elapsedNanoTime = 0;
 
        for(long killed = 0; killed < timeToKill; killed += resolution) {

            long startNanos  = System.nanoTime();
            timeKiller.kill(resolution);
            long endNanos  = System.nanoTime();
 
            elapsedNanoTime += (endNanos - startNanos);

            long shouldHaveElapsed = killed + resolution;
            long diff = Math.abs(shouldHaveElapsed - elapsedNanoTime);

            if(diff > resolution) {
                if(verbose) {
                    out("Cumulative error has exceeded allowed threshold, aborting." +
                        "\n\tElapsed time: %s" +
                        "\n\tExpected time: %s\n",
                        annotate(elapsedNanoTime), annotate(shouldHaveElapsed)
                    );
                }
                return false;
            }
        }

        if(verbose) {
            long endMillis = System.currentTimeMillis();

            long elapsedMilliTime = TimeUnit.MILLISECONDS.toNanos(endMillis - startMillis);

            out("Elapsed time:" +
                    "\n\tAccording to currentTimeMillis(): %s" +
                    "\n\tAccording to nanoTime(): %s" +
                    "\n\tExpected: %s\n",
                    annotate(elapsedMilliTime), annotate(elapsedNanoTime), annotate(timeToKill)
            );
        }
 
        return true;
    }

    public static void main(String[] args) throws CmdLineException {

        PrecisionTest precisionTest = new PrecisionTest();
        new CmdLineParser(precisionTest).parseArgument(args);

        //FIXME: deal with warmup

        precisionTest.estimatePrecision(new TimeKiller.Sleeper(), "Thread.sleep()");
        precisionTest.estimatePrecision(new TimeKiller.Parker(), "LockSupport.parkNanos()");
        precisionTest.estimatePrecision(new TimeKiller.Burner(), "System.currentNanos()");


        out("Estimating the number of BlackHole tokens per nano...");
        double tokensPerNano = TimeKiller.BlackHole.estimateTokensPerNano();
        out("BlackHole tokens per nano: %.4f", tokensPerNano);


        //FIXME: deal with BlackHole's stability and linearity
        precisionTest.estimatePrecision(new TimeKiller.BlackHole(tokensPerNano), "BlackHole.consumeCPU()");

    }
}
