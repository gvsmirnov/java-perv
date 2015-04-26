package ru.gvsmirnov.perv.labs.safepoints;

public class Deoptimization {

    private interface Burner {
        void burn(long nanos);
    }

    // Run with: -XX:+PrintSafepointStatistics  -XX:PrintSafepointStatisticsCount=1 -XX:+PrintCompilation
    // Notice that there was no apparent safepoint triggered by deoptimization.
    // This is because the JVM triggers safepoints every second by default with
    // "no vm operation" specified as the vmop.

    // If the same is run with -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=0,
    // you will see the deoptimization-triggered safepoint

    public static void main(String[] args) throws InterruptedException {
        runManyTimes(new Burner1()); // JIT speculatively assumes a monomorphic callsite
        runManyTimes(new Burner2()); // Assumption fails
    }

    private static void burn(long nanos) {
        long endTime = System.nanoTime() + nanos;
        while (System.nanoTime() < endTime);
    }

    private static void runManyTimes(Burner burner) {
        for(int i = 0; i < 100_000; i++) {
            burner.burn(10);
        }
    }


    private static class Burner1 implements Burner {

        @Override
        public void burn(long nanos) {
            long endTime = System.nanoTime() + nanos;
            while(System.nanoTime() < endTime);
        }
    }

    private static class Burner2 implements Burner {
        @Override
        public void burn(long nanos) {

        }
    }
}
