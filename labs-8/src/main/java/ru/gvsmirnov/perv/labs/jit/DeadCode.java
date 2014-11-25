package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.util.ClassUtils;

import java.util.Collection;

/**
 * TODO: link to the blog post
 */
@State(Scope.Benchmark)
public class DeadCode {

    private double value;

    @Setup
    public void init() {
        this.value = Math.random() * Integer.MAX_VALUE;
    }

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
        for (int i = 0; i < 1_000_000; i++) ;
        return Math.sqrt(value);
    }

    public static void main(String[] args) throws RunnerException {
        ChainedOptionsBuilder base = new OptionsBuilder()
                .include(DeadCode.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1);

        Collection<RunResult> jitResult = new Runner(base.build()).run();

        Collection<RunResult> intResult = new Runner(
                base.jvmArgsAppend("-Xint").build()
        ).run();

        System.out.println("");

        printResult("jit", intResult);
        printResult("int", jitResult);
    }

    private static void printResult(String label, Collection<RunResult> runResults) {
        runResults.stream()
                .map(r -> format(label, r))
                .forEach(System.out::println);
    }

    private static String format(String label, RunResult result) {
        String[] parts = result.getParams().getBenchmark().split("\\.");
        String benchmark = parts[parts.length - 1];

        return String.format("[%s] %s: %s", label, benchmark, result.getPrimaryResult());
    }
}
