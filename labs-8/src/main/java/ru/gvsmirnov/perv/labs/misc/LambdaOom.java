package ru.gvsmirnov.perv.labs.misc;

import java.util.ArrayList;
import java.util.Collection;

public class LambdaOom {
    public static void main(String[] args) {
        Collection<Runnable> leak = new ArrayList<>();
        while(true) {
            leak.add(() -> {});
        }
    }
}
