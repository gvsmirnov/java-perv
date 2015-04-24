package ru.gvsmirnov.perv.labs.gc;

import javassist.CannotCompileException;
import javassist.ClassPool;

import java.util.ArrayList;
import java.util.Collection;

public class NoGcTrigger {

    public static void main(String[] args) throws CannotCompileException {
//        leakMetaSpace();
        leakObjects();
    }

    private static void leakMetaSpace() throws CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        for(long l = 0; l < Long.MAX_VALUE; l++) {
            pool.makeClass("com.example.Kitty" + l).toClass();
        }
    }

    public static void leakObjects() {
        Collection<Object> objects = new ArrayList<>();
        while(true) {
            objects.add(new byte[2048]);
        }
    }
}
