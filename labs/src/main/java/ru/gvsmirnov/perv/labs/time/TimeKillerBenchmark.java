package ru.gvsmirnov.perv.labs.time;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
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
    private final TimeKiller.Parker parker = new TimeKiller.Parker();
    private final TimeKiller.Burner burner = new TimeKiller.Burner();
    //TODO: add black hole

    @GenerateMicroBenchmark
    public void sleepOneMs() {
        sleeper.kill(TimeUnit.MILLISECONDS.toNanos(1));
    }

    @GenerateMicroBenchmark
    public void sleepTwoMs() {
        sleeper.kill(TimeUnit.MILLISECONDS.toNanos(2));
    }

    @GenerateMicroBenchmark
    public void sleepOneNs() {
        sleeper.kill(TimeUnit.NANOSECONDS.toNanos(1));
    }

    @GenerateMicroBenchmark
    public void parkOneMs() {
        parker.kill(TimeUnit.MILLISECONDS.toNanos(1));
    }

    @GenerateMicroBenchmark
    public void parkTwoMs() {
        parker.kill(TimeUnit.MILLISECONDS.toNanos(2));
    }

    @GenerateMicroBenchmark
    public void parkOneNs() {
        parker.kill(TimeUnit.NANOSECONDS.toNanos(1));
    }

    @GenerateMicroBenchmark
    public void burnOneMs() {
        burner.kill(TimeUnit.MILLISECONDS.toNanos(1));
    }

    @GenerateMicroBenchmark
    public void burnTwoMs() {
        burner.kill(TimeUnit.MILLISECONDS.toNanos(2));
    }

    @GenerateMicroBenchmark
    public void burnOneNs() {
        burner.kill(TimeUnit.NANOSECONDS.toNanos(1));
    }

    @GenerateMicroBenchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void getNanoTime() {
        System.nanoTime(); // being a syscall, spared by DCE
    }

}
