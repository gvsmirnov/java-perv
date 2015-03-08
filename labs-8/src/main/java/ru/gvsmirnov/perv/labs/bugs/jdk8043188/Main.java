package ru.gvsmirnov.perv.labs.bugs.jdk8043188;

import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private static interface Child1 extends InterfaceWithField {
        long childId = register("Child1");
    }

    private static interface Child2 extends InterfaceWithFieldAndDefaultMethod {
        long childId = register("Child2");
    }

    public static void main(String[] args) {
        println("Observed:   Child1 -> %d", Child1.childId);
        println("Observed:   Child2 -> %d", Child2.childId);
    }

    private static final AtomicLong currentId = new AtomicLong(1);

    public static long register(String name) {
        final long id = currentId.getAndIncrement();

        println("Registered: %s -> %d", name, id);

        return id;
    }

    private static void println(String format, Object... args) {
        System.out.println(String.format(format, args));
    }
}
