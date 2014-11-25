package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormat;
import org.openjdk.jmh.results.format.ResultFormatFactory;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.PrintWriter;
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

        // Too lazy to write my own formatter
        // Will use that of JMH
        // Also will pervert for that
        // Don't you judge me!

        PrintWriter writer = makeWriter(label);
        ResultFormat resultFormat = ResultFormatFactory.getInstance(ResultFormatType.TEXT, writer);

        resultFormat.writeOut(runResults);
        writer.flush();
    }

    private static PrintWriter makeWriter(final String label) {
        return new PrintWriter(System.out) {

            private StringBuilder builder = makeBuilder();

            @Override
            public void print(String s) {
                builder.append(s);
            }

            @Override
            public void println() {
                this.println("");
            }

            @Override
            public void println(String s) {
                builder.append(s);
                super.print(builder.toString());
                super.println();
                this.builder = makeBuilder();
            }

            private StringBuilder makeBuilder() {
                return new StringBuilder("[" + label + "]\t");
            }

        };
    }

}
