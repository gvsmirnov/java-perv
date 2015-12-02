package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;

@State(Scope.Benchmark)
public class EscapeAnalysis extends ComparingBenchmark {
    public int id = (int) (Math.random() * Integer.MAX_VALUE);

    public static class Convict {
        public final int id;

        public Convict(int id) {
            this.id = id;
        }
    }

    @Benchmark
    public Object baseline_returnObject() {
        return this;
    }

    @Benchmark
    public int baseline_returnInt() {
        // TODO: for some reason, this yields larger error
        // than measureNoEscape or measureDeadCode.
        return this.id;
    }

    @Benchmark
    public Object measureEscape() {
        return new Convict(id);
    }

    @Benchmark
    public int measureDeadCode() {
        Convict convict = new Convict(id);
        return this.id;
    }

    @Benchmark
    public int measureNoEscape() {
        return new Convict(id).id;
    }

    public static void main(String[] args) throws RunnerException {
        runBenchmarks_NoEscape(EscapeAnalysis.class);
    }

}
