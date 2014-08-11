package ru.gvsmirnov.perv.labs.gc;

import javassist.CannotCompileException;
import javassist.ClassPool;

public class NoGcTrigger {

    public static void main(String[] args) throws CannotCompileException {
        leakMetaSpace();
    }

    private static void leakMetaSpace() throws CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        for(long l = 0; l < Long.MAX_VALUE; l++) {
            pool.makeClass("com.example.Kitty" + l).toClass();
        }
    }
}
