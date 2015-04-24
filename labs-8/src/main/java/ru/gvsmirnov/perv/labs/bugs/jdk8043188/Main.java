package ru.gvsmirnov.perv.labs.bugs.jdk8043188;

import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private interface Child1 extends InterfaceWithField {
        long childId = register("Child1");
    }

    private interface Child2 extends InterfaceWithFieldAndDefaultMethod {
        long childId = register("Child2");
    }

    private static class ChildClass implements InterfaceWithFieldAndDefaultMethod {

    }

    public static void main(String[] args) {
//        println("Observed:   Child1 -> %d", Child1.childId);
//        println("Observed:   Child2 -> %d", Child2.childId);

        ChildClass c = new ChildClass();
        c.getTime();
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
