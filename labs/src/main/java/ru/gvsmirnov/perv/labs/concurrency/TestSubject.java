package ru.gvsmirnov.perv.labs.concurrency;

public class TestSubject {

    private volatile boolean finished;
    private int value = 0;

    void executedOnCpu0() {
        value = 10;
        finished = true;
    }

    void executedOnCpu1() {
        while(!finished);
        assert value == 10;
    }

}