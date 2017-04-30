package ru.gvsmirnov.perv.labs.gc;

import java.util.ArrayList;
import java.util.List;

public class G1MemoryExhauster {

    public static final List<byte[]> sink = new ArrayList<>();

    // Run with: -Xmx256m -XX:+UseG1GC -XX:+PrintGCDetails
    // Pay attention to the memory usage at the time of the OOME.
    // See http://hg.openjdk.java.net/jdk9/jdk9/hotspot/file/efe1782aad5c/src/share/vm/gc/g1/g1CollectedHeap.cpp#l475

    public static void main(String[] args) {
        try {
            while (true) {
                sink.add(new byte[1024]);
            }
        } catch (OutOfMemoryError err) {
            sink.clear();
            err.printStackTrace();
        }
    }

    // Bonus questions: what changes if we remove/add `sink.clear();` and why?

}
