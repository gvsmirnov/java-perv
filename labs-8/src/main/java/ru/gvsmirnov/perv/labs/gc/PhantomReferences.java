package ru.gvsmirnov.perv.labs.gc;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Arrays;

public class PhantomReferences {

    // This example shows how having phantom references pointing to objects
    // May result in an OutOfMemoryError
    //
    // There are two modes (controlled by phantom.refs)
    //
    //  1. A lot of objects are created
    //  2. A lot of objects are created, and phantom references are created
    //     for them. These references are held in a buffer until it's full.
    //     The reference queue is traversed and cleared on each iteration
    //  3. A lot of objects are created, and phantom references are created
    //     for them. These references are held in a buffer until it's full.
    //     The reference queue is not cleared.
    //
    // The allocations made in both cases need to be exactly the same,
    // so in (1) phantom references will be also created, but all of them
    // will be pointing to the same object


    // 1. Run with: -verbose:gc -Xmx24m -XX:NewSize=16m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are mostly young GCs
    //
    // 2. Run with: -Dphantom.refs=true -verbose:gc -Xmx24m -XX:NewSize=16m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are lots of full GCs
    //
    // 3. Run with: -Dphantom.refs=true -verbose:gc -Xmx64m -XX:NewSize=32m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe that there are few full GCs
    //
    // 4. Run with: -Dphantom.refs=true -Dno.ref.clearing=true -verbose:gc -Xmx64m -XX:NewSize=32m
    //              -XX:MaxTenuringThreshold=1 -XX:-UseAdaptiveSizePolicy
    //
    //    Observe lots of Full GCs and an OutOfMemoryError

    private static final int OBJECT_SIZE           = Integer.getInteger("obj.size", 192);
    private static final int BUFFER_SIZE           = Integer.getInteger("buf.size", 24 * 1024);
    private static final boolean PHANTOM_REFS_FOR_ALL = Boolean.getBoolean("phantom.refs");
    private static final boolean CLEAR_REFS = !Boolean.getBoolean("no.ref.clearing");

    private static Object makeObject() {
        return new byte[OBJECT_SIZE];
    }

    public static volatile Object sink;
    private static final ReferenceQueue<Object> queue = new ReferenceQueue<>();

    public static void main(String[] args) throws InterruptedException {

        System.out.printf("Buffer size: %d; Object size: %d; Phantom refs for all: %s; Clearing refs: %s%n", BUFFER_SIZE, OBJECT_SIZE, PHANTOM_REFS_FOR_ALL, CLEAR_REFS);

        final Object substitute = makeObject(); // We want to create it in both scenarios so the footprint matches
        final Object[] refs = new Object[BUFFER_SIZE];

        System.gc(); // Clean up young gen

        for (int index = 0;;) {
            Object object = makeObject();
            sink = object; // Prevent Escape Analysis from optimizing the allocation away

            if (!PHANTOM_REFS_FOR_ALL) {
                object = substitute;
            }

            refs[index++] = new PhantomReference<>(object, queue);

            if (index == BUFFER_SIZE) {
                Arrays.fill(refs, null);
                index = 0;
            }

            if(CLEAR_REFS) {
                Reference ref;
                while((ref = queue.poll()) != null) {
                    ref.clear();
                }
            }
        }
    }
}