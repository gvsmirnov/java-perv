package ru.gvsmirnov.perv.labs.rekt;

public class ChecksumCalculator {

    private static native long calculateChecksum(String filename);

    public static void main(String[] args) {

        System.loadLibrary("main");
        long checksum = calculateChecksum(args[0]);
        System.out.println("Got checksum from native method: " + checksum);
    }
}
