package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import static ru.gvsmirnov.perv.labs.jit.ComparingRunner.runBenchmarks;

/**
 * TODO: link to the blog post
 */
@State(Scope.Benchmark)
public class DeadCode {

    private double value= Math.random() * Integer.MAX_VALUE;

    @Benchmark
    public double measureA() {
        double previous;
        double current = value / 2;

        do {
            previous = current;
            current = (previous + (value / previous)) / 2;
        } while ((previous - current) > 1e-15);

        return current;
    }

    @Benchmark
    public double measureB() {
        for (int i = 0; i < 1_000_000; i++);
        return Math.sqrt(value);
    }

    public static void main(String[] args) throws RunnerException {
        runBenchmarks(DeadCode.class);
    }
}
