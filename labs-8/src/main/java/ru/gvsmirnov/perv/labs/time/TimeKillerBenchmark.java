package ru.gvsmirnov.perv.labs.time;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

/**
 * Used to determine how much overhead invocation of various time-killing methods give
 * Helps differentiate between overhead and resolution
 */

@State(Scope.Benchmark)
public class TimeKillerBenchmark {

    private final TimeKiller.Sleeper sleeper = new TimeKiller.Sleeper();
    private final TimeKiller.TimedParker timedParker = new TimeKiller.TimedParker();
    private final TimeKiller.Burner burner = new TimeKiller.Burner();
    //TODO: add black hole

    @Benchmark
    public void sleepOneMs() {
        sleeper.kill(TimeUnit.MILLISECONDS.toNanos(1));
    }

    @Benchmark
    public void sleepTwoMs() {
        sleeper.kill(TimeUnit.MILLISECONDS.toNanos(2));
    }

    @Benchmark
    public void sleepOneNs() {
        sleeper.kill(TimeUnit.NANOSECONDS.toNanos(1));
    }

    @Benchmark
    public void parkOneMs() {
        timedParker.kill(TimeUnit.MILLISECONDS.toNanos(1));
    }

    @Benchmark
    public void parkTwoMs() {
        timedParker.kill(TimeUnit.MILLISECONDS.toNanos(2));
    }

    @Benchmark
    public void parkOneNs() {
        timedParker.kill(TimeUnit.NANOSECONDS.toNanos(1));
    }

    @Benchmark
    public void burnOneMs() {
        burner.kill(TimeUnit.MILLISECONDS.toNanos(1));
    }

    @Benchmark
    public void burnTwoMs() {
        burner.kill(TimeUnit.MILLISECONDS.toNanos(2));
    }

    @Benchmark
    public void burnOneNs() {
        burner.kill(TimeUnit.NANOSECONDS.toNanos(1));
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void getNanoTime() {
        System.nanoTime(); // being a syscall, spared by DCE
    }

}
