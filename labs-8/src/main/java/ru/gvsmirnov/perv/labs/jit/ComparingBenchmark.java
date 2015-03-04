package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormat;
import org.openjdk.jmh.results.format.ResultFormatFactory;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

@State(Scope.Benchmark)
public class ComparingBenchmark {

    // Too lazy to write my own formatter
    // Will use that of JMH
    // Also will pervert for that
    // Don't you judge me!

    @Param("jit")
    public String mode;

    public static void runBenchmarks(Class<?> clazz) throws RunnerException {

        Collection<RunResult> jitResult = new Runner(
                makeOptionBuilder(clazz).param("mode", "jit").build()
        ).run();

        Collection<RunResult> intResult = new Runner(
                makeOptionBuilder(clazz).param("mode", "int").jvmArgsAppend("-Xint").build()
        ).run();

        Collection<RunResult> allResults = new ArrayList<>();
        allResults.addAll(jitResult);
        allResults.addAll(intResult);

        printResult(allResults);
    }

    private static ChainedOptionsBuilder makeOptionBuilder(Class<?> clazz) {
        return new OptionsBuilder()
                .include(clazz.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1);
    }

    private static void printResult(Collection<RunResult> runResults) {
        System.out.println();

        ResultFormat resultFormat = ResultFormatFactory.getInstance(ResultFormatType.TEXT, System.out);
        resultFormat.writeOut(runResults);
    }

}
