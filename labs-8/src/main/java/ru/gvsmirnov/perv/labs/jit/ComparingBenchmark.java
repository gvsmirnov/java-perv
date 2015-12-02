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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@State(Scope.Benchmark)
public class ComparingBenchmark {

    // Too lazy to write my own formatter
    // Will use that of JMH
    // Also will pervert for that
    // Don't you judge me!

    @Param("default")
    public String mode;

    public static void runBenchmarks_JIT_vs_Interpreter(Class<?> clazz) throws RunnerException {
        runBenchmarks(clazz, new Mode("jit"), new Mode("int", "-Xint"));
    }

    public static void runBenchmarks_NoEscape(Class<?> clazz) throws RunnerException {
        runBenchmarks(clazz, new Mode("default"), new Mode("no-opt", "-XX:-DoEscapeAnalysis"));
    }

    public static void runBenchmarks(Class<?> clazz, Mode... modes) throws RunnerException {
        Collection<RunResult> allResults = new ArrayList<>();

        for(Mode mode : modes) {
            ChainedOptionsBuilder builder = makeOptionBuilder(clazz).param("mode", mode.name);

            for(String arg : mode.jvmArgs) {
                builder.jvmArgsAppend(arg);
            }

            allResults.addAll(new Runner(builder.build()).run());
        }

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

    public static class Mode {
        private final String name;
        private final Collection<String> jvmArgs;

        public Mode(String name, Collection<String> jvmArgs) {
            this.name = name;
            this.jvmArgs = jvmArgs;
        }

        public Mode(String name, String... jvmArgs) {
            this(name, Arrays.asList(jvmArgs));
        }
    }

}
