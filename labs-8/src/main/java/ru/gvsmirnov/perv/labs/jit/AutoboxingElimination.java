package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

@State(Scope.Benchmark)
public class AutoboxingElimination extends ComparingBenchmark {
    public int anInt = (int) (Math.random() * Integer.MAX_VALUE);
    public Integer anInteger = new Integer(anInt);


    @Benchmark
    public Integer baseline_returnObject() {
        return anInteger;
    }

    @Benchmark
    public int baseline_returnInt() {
        return anInt;
    }

    @Benchmark
    public int measure_autoUnboxed() {
        return new Integer(anInt);
    }

    @Benchmark
    public int measure_manualUnboxed() {
        return new Integer(anInt).intValue();
    }

    @Benchmark
    public Integer measure_Boxed() {
        return new Integer(anInt);
    }

    public static void main(String[] args) throws RunnerException {
        runBenchmarks_NoEscape(AutoboxingElimination.class);
    }

}
