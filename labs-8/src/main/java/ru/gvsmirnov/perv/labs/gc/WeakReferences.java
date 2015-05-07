package ru.gvsmirnov.perv.labs.gc;

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public class WeakReferences {

    // This example shows how having weak references pointing to objects
    // May result in more frequent Full GC pauses
    //
    // There will be two scenarios:
    //
    //  1. A lot of objects are created
    //  2. A lot of objects are created, and weak references are created
    //     for them. These weak references are held in a buffer and then
    //     cleaned up when it's full
    //
    // The allocations made in both cases need to be exactly the same,
    // so in (1) weak references will be also created, but all of them
    // will be pointing to the same object

    private static final int OBJECT_SIZE = Integer.getInteger("obj.size", 192 - 16);
    private static final int BUFFER_SIZE = Integer.getInteger("buf.size", 64 * 1024);
//    private static final boolean WEAK_REFS_FOR_ALL = Boolean.getBoolean("weak.refs");
//    public static volatile boolean WEAK_REFS_FOR_ALL = false;
    public static volatile boolean WEAK_REFS_FOR_ALL = true;

    // ------------------------------------------------------------------
    // 1. Run with: -verbose:gc -Xmx64m -XX:NewSize=16m
    //              -XX:MaxTenuringThreshold=0 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are mostly young GCs
    //
    // 2. Run with: -Dweak.refs=true -verbose:gc -Xmx64m -XX:NewSize=16m
    //              -XX:MaxTenuringThreshold=0 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are many full GCs
    // ------------------------------------------------------------------
    // 3. Run with: -verbose:gc -Xmx24m -XX:NewSize=16m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are mostly young GCs
    //
    // 4. Run with: -Dweak.refs=true -verbose:gc -Xmx24m -XX:NewSize=16m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are many full GCs
    //
    // 5. Run with: -Dweak.refs=true -verbose:gc -Xmx64m -XX:NewSize=32m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are mostly young GCs
    // ------------------------------------------------------------------

    private static Object makeObject() {
        return new byte[OBJECT_SIZE];
    }

    public static volatile Object sink;

    public static void main(String[] args) throws InterruptedException {
        final Object substitute = makeObject(); // We want to create it in both scenarios so the footprint matches
        final WeakReference[] weakRefs = new WeakReference[BUFFER_SIZE];

        System.gc(); // Clean up young gen

        for (int index = 0;;) {
            Object object = makeObject();
            sink = object; // Prevent Escape Analysis from optimizing the allocation away

            if (!WEAK_REFS_FOR_ALL) {
                object = substitute;
            }

            weakRefs[index++] = new WeakReference<>(object);

            if (index == BUFFER_SIZE) {
                Arrays.fill(weakRefs, null);
                index = 0;
            }
        }
    }
}