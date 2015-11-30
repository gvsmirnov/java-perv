package ru.gvsmirnov.perv.labs.gc;

import java.lang.ref.SoftReference;
import java.util.Arrays;

public class SoftReferences {

    // This example shows how having soft references pointing to objects
    // May result in more frequent Full GC pauses
    //
    // There are two modes (controlled by soft.refs)
    //
    //  1. A lot of objects are created
    //  2. A lot of objects are created, and soft references are created
    //     for them. These references are held in a buffer until it's full
    //
    // The allocations made in both cases need to be exactly the same,
    // so in (1) soft references will be also created, but all of them
    // will be pointing to the same object


    // 1. Run with: -verbose:gc -Xmx24m -XX:NewSize=16m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are mostly young GCs
    //
    // 2. Run with: -Dsoft.refs=true -verbose:gc -Xmx24m -XX:NewSize=16m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are lots of full GCs
    //
    // 3. Run with: -Dsoft.refs=true -verbose:gc -Xmx64m -XX:NewSize=32m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are still many full GCs

    private static final int OBJECT_SIZE           = Integer.getInteger("obj.size", 192);
    private static final int BUFFER_SIZE           = Integer.getInteger("buf.size", 64 * 1024);
    private static final boolean SOFT_REFS_FOR_ALL = Boolean.getBoolean("soft.refs");

    private static Object makeObject() {
        return new byte[OBJECT_SIZE];
    }

    public static volatile Object sink;

    public static void main(String[] args) throws InterruptedException {

        System.out.printf("Buffer size: %d; Object size: %d; Soft refs for all: %s%n", BUFFER_SIZE, OBJECT_SIZE, SOFT_REFS_FOR_ALL);

        final Object substitute = makeObject(); // We want to create it in both scenarios so the footprint matches
        final Object[] refs = new Object[BUFFER_SIZE];

        System.gc(); // Clean up young gen

        for (int index = 0;;) {
            Object object = makeObject();
            sink = object; // Prevent Escape Analysis from optimizing the allocation away

            if (!SOFT_REFS_FOR_ALL) {
                object = substitute;
            }

            refs[index++] = new SoftReference<>(object);

            if (index == BUFFER_SIZE) {
                Arrays.fill(refs, null);
                index = 0;
            }
        }
    }
}