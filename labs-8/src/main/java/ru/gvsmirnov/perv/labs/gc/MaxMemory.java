package ru.gvsmirnov.perv.labs.gc;

public class MaxMemory {

    // Run with -Xmx512m
    // Run with -Xmx512m -Xms512m
    // Run with -Xmx512m -Xms512m -XX:+PrintGCDetails, look at survivor space sizes
    // Run with -Xmx512m -XX:+UseG1GC
    // Run with -Xms512m -Xmx512m -XX:SurvivorRatio=100

    public static void main(final String[] args) {
        final long maxMemory = Runtime.getRuntime().maxMemory();

        System.out.printf(
                "Max memory: %d MB", maxMemory / 1024 / 1024
        );
    }
}