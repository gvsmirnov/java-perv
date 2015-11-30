package ru.gvsmirnov.perv.labs.gc;

import java.util.concurrent.locks.LockSupport;

public class FixedBoxing {

    private static volatile double sensorValue = Double.NaN;

    private static void readSensor() {
        while(true) {
            sensorValue = Math.random();
        }
    }

    private static void processSensorValue(double value) {
        if(!Double.isNaN(value)) {
            // Be warned: may take more than one usec on some machines, especially Windows
            LockSupport.parkNanos(1000);
        }
    }

    public static void main(String[] args) {
        int iterations = args.length > 0 ? Integer.parseInt(args[0]) : 1_000_000;

        initSensor();

        for(int i = 0; i < iterations; i ++) {
            processSensorValue(sensorValue);
        }
    }

    private static void initSensor() {
        Thread sensorReader = new Thread(FixedBoxing::readSensor);

        sensorReader.setDaemon(true);
        sensorReader.start();
    }
}
