package ru.gvsmirnov.perv.labs.bugs.jdk8043188;

public interface InterfaceWithFieldAndDefaultMethod {

    long id = Main.register("InterfaceWithFieldAndDefaultMethod");

    default long getTime() {
        return System.currentTimeMillis();
    }

}
