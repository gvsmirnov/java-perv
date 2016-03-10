package ru.gvsmirnov.perv.labs.agent;

import java.lang.instrument.Instrumentation;

public class BloatedAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer((loader, name, clazz, pd, originalBytes) -> originalBytes, true);
    }
}
