package ru.gvsmirnov.perv.labs.rekt;

import java.util.stream.Stream;

public class ChecksumCalculator {

    private static native long calculateChecksum(String filename);

    public static void main(String[] args) {

        System.loadLibrary("main");

        Stream.of(args)
                .forEach(filename -> {
                    long checksum = calculateChecksum(filename);
                    System.out.println(filename + '\t' + checksum);
                });
    }
}
