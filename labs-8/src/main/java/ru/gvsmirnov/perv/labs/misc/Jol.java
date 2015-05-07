package ru.gvsmirnov.perv.labs.misc;

import java.lang.ref.WeakReference;

import static org.openjdk.jol.info.ClassLayout.parseClass;
import static org.openjdk.jol.info.GraphLayout.parseInstance;
import static org.openjdk.jol.util.VMSupport.vmDetails;

public class Jol {

    public static void main(String[] args) {
        details();

        weakRef(object());
        weakRef(bytearray(1 << 16));
    }

    private static Object object() {
        final Object object = new Object();

        out(parseClass(Object.class).toPrintable());
        out(parseInstance(object).toPrintable());

        return object;
    }

    private static byte[] bytearray(int size) {
        final byte[] array = new byte[size];

        out(parseClass(byte[].class).toPrintable());
        out(parseInstance(array).toPrintable());

        return array;
    }

    private static WeakReference<Object> weakRef(Object to) {
        WeakReference<Object> ref = new WeakReference<>(to);

        out(parseClass(WeakReference.class).toPrintable());
        out(parseInstance(ref).toPrintable());

        return ref;
    }

    private static void details() {
        out(vmDetails());
    }

    private static void out(Object o) {
        System.out.println(o.toString());
    }

    private static void out(String format, Object... args) {
        out(String.format(format, args));
    }
}
