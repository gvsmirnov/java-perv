package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;

public class ComparingRunner {

    public static void runBenchmarks(Class<?> clazz) throws RunnerException {
        ChainedOptionsBuilder base = new OptionsBuilder()
                .include(clazz.getSimpleName())
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
