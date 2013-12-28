package ru.gvsmirnov.perv.labs.concurrency;

import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class VolatileBenchmark {

    @GenerateMicroBenchmark
    @Group("plainLocal")
    public int plainLocalRead(PlainLocal value) {
        return value.value;
    }

    @GenerateMicroBenchmark
    @Group("plainLocal")
    public int plainLocalIncrement(PlainLocal value) {
        return value.value++;
    }

    @GenerateMicroBenchmark
    @Group("plainShared")
    public int plainSharedRead(PlainShared value) {
        return value.value;
    }

    @GenerateMicroBenchmark
    @Group("plainShared")
    public int plainSharedIncrement(PlainShared value) {
        return value.value++;
    }

    @GenerateMicroBenchmark
    @Group("volatileLocal")
    public int plainVolatileRead(VolatileLocal value) {
        return value.value;
    }

    @GenerateMicroBenchmark
    @Group("volatileLocal")
    public int plainVolatileIncrement(VolatileLocal value) {
        return value.value++;
    }

    @GenerateMicroBenchmark
    @Group("volatileShared")
    public int plainVolatileRead(VolatileShared value) {
        return value.value;
    }

    @GenerateMicroBenchmark
    @Group("volatileShared")
    public int plainVolatileIncrement(VolatileShared value) {
        return value.value++;
    }


    @State(Scope.Benchmark)
    public static class PlainShared {
        private int value;
    }

    @State(Scope.Thread)
    public static class PlainLocal {
        private int value;
    }

    @State(Scope.Benchmark)
    public static class VolatileShared {
        private volatile int value;
    }

    @State(Scope.Thread)
    public static class VolatileLocal {
        private volatile int value;
    }

}
