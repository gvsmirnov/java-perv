package ru.gvsmirnov.perv.labs.jit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@State(Scope.Benchmark)
public class EnumValues {

    volatile int target = 2;

    @Benchmark
    public Bool baseline() {
        return Bool.TRUE;
    }

    @Benchmark
    public Bool measureBuiltin() {
        return lookup(Bool.values(), target);
    }

    @Benchmark
    public Bool measureManualArray() {
        return lookup(Bool.manualValues(), target);
    }

    @Benchmark
    public Bool measureCached() {
        return lookup(Bool.valuesCache, target);
    }

    @Benchmark
    public Bool measureCachedMap() {
        return Bool.valuesMap.get(target);
    }

    private static Bool lookup(Bool[] array, int target) {
        for(Bool bool : array) {
            if(bool.ordinal() == target) {
                return bool;
            }
        }

        return null;
    }

    private static enum Bool {
        TRUE, FALSE, FILE_NOT_FOUND;

        private static final Bool[] valuesCache = values();
        private static final Map<Integer, Bool> valuesMap = new ConcurrentHashMap<>();

        private static Bool[] manualValues() {
            return new Bool[] {TRUE, FALSE, FILE_NOT_FOUND};
        }

        static {
            for(Bool bool : values()) {
                valuesMap.put(bool.ordinal(), bool);
            }
        }
    }
}
