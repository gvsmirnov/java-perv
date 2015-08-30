package ru.gvsmirnov.perv.labs.rekt;

import java.util.stream.Stream;

public class ChecksumCalculator {

    private static native long calculateChecksum(String filename);

    public static void main(String[] args) {

        System.loadLibrary("main");

        Stream.of(args)
                .map(ChecksumCalculator::calculateChecksum)
                .forEach(System.out::println);
    }
}
