package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.annotations.*;

import java.util.Collection;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class BusinessLogicBenchmark {

    private static final BusinessLogic NAIVE  = new DirectCalculator();
    private static final BusinessLogic CLEVER = new CleverCalculator();

    @State(Scope.Thread)
    public static class BusinessData {
        @Param({"1048576", "2097152", "4194304"})
        private int size;

        private Collection<Number> collection;

        @Setup(Level.Invocation)
        public void setup() {
            collection = IntStream.range(0, size).boxed().collect(toList());
        }
    }

    @Benchmark
    public double measureBaseline(BusinessData data) {
        return 1.0;
    }

    @Benchmark
    public double measureNaive(BusinessData data) {
        return NAIVE.calculateStuff(data.collection);
    }

    @Benchmark
    public double measureClever(BusinessData data) {
        return CLEVER.calculateStuff(data.collection);
    }


/*

Regular results:

Benchmark                                           (size)   Mode   Samples        Score  Score error    Units
r.g.p.l.j.BusinessLogicBenchmark.measureBaseline   1048576  thrpt         3  1097954.128  8403426.409    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureBaseline   2097152  thrpt         3   713299.663  1393427.953    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureBaseline   4194304  thrpt         3   434523.810  1144104.436    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureClever     1048576  thrpt         3      389.726     1163.772    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureClever     2097152  thrpt         3      270.435       20.277    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureClever     4194304  thrpt         3      138.692      152.268    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureNaive      1048576  thrpt         3      427.969      230.737    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureNaive      2097152  thrpt         3      204.767       62.994    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureNaive      4194304  thrpt         3       83.321      142.696    ops/s



Results with -jvmArgsAppend "-Xint":

Benchmark                                           (size)   Mode   Samples        Score  Score error    Units
r.g.p.l.j.BusinessLogicBenchmark.measureBaseline   1048576  thrpt         3   500000.000        0.000    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureBaseline   2097152  thrpt         3   666666.667  5266509.096    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureBaseline   4194304  thrpt         3   500000.000        0.000    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureClever     1048576  thrpt         3        7.654        0.633    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureClever     2097152  thrpt         3        3.895        0.742    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureClever     4194304  thrpt         3        1.947        0.734    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureNaive      1048576  thrpt         3        2.052        0.928    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureNaive      2097152  thrpt         3        1.034        0.140    ops/s
r.g.p.l.j.BusinessLogicBenchmark.measureNaive      4194304  thrpt         3        0.508        0.213    ops/s
 */

}
