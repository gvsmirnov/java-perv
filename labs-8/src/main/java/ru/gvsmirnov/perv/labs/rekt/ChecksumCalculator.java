package ru.gvsmirnov.perv.labs.rekt;

import java.util.stream.Stream;

public class ChecksumCalculator {

    private static native long calculateChecksum(String filename);

    public static void main(String[] args) {

        System.loadLibrary("main");

        Thread calculator = new Thread(() -> {
            Stream.of(args).forEach(filename -> {
                long checksum = calculateChecksum(filename);
                System.out.println("Got checksum from native method: " + checksum);
            });
        });

        calculator.start();
        try {
            calculator.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished");
    }
}
