package ru.gvsmirnov.perv.labs.bugs.jdk8058847;

public class Main {

    public static void main(String[] args) {
        crashJvm();
    }

    private static final int[] values = new int[256];

    private static void crashJvm() {
        byte[] bytes = new byte[] {-1};
        while (true) {
            for (Byte b : bytes) {
                values[b & 0xff]++;
            }
        }
    }

}
