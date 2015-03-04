package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.LinkedList;

@State(Scope.Benchmark)
public class LoopConditionals {
    public static final int ITERATIONS = 100_000;

    volatile boolean condition = true;

    // The two methods below have roughly the same performance

    @Benchmark
    @OperationsPerInvocation(ITERATIONS)
    public void measureInternal(Blackhole bh) {
        final boolean condition = this.condition;

        for(int i = 0; i < ITERATIONS; i ++) {
            if(condition) {
                bh.consume(new ArrayList<>());
            } else {
                bh.consume(new LinkedList<>());
            }
        }
    }

    @Benchmark
    @OperationsPerInvocation(ITERATIONS)
    public void measureExternal(Blackhole bh) {
        final boolean condition = this.condition;

        if(condition) {
            for (int i = 0; i < ITERATIONS; i++) {
                bh.consume(new ArrayList<>());
            }
        } else {
            for (int i = 0; i < ITERATIONS; i++) {
                bh.consume(new LinkedList<>());
            }
        }
    }
}
