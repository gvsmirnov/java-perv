package ru.gvsmirnov.perv.labs.gc;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

public class WeakReferences {

    private static final boolean DO_WEAK_REFERENCES  = Boolean.getBoolean("weak.refs");

    // Run with: -verbose:gc -Xmx64m -XX:MaxTenuringThreshold=0
    // Observe that there are only young GCs

    // Run with: -verbose:gc -Xmx64m -XX:MaxTenuringThreshold=0 -Dweak.refs=true
    // Observe that there are many full GCs, even though the allocation pattern is the same

    private static Object makeObject() {
        return new byte[64 * 1024];
    }

    public static void main(String[] args) throws InterruptedException {
        ReferenceCleaner.setup();

        while(true) {
            createWeakReference(makeObject());
        }
    }

    private static void createWeakReference(Object object) {
        if(!DO_WEAK_REFERENCES) {
            object = SUBSTITUTE_OBJECT;
        }

        weakRefs.offer(new WeakReference<>(object));
    }

    private static final Object SUBSTITUTE_OBJECT = makeObject();
    private static final Queue<WeakReference<Object>> weakRefs = new ArrayBlockingQueue<>(1 << 16);

    private static class ReferenceCleaner extends TimerTask {

        private static final Timer timer = new Timer(true);

        @Override
        public void run() {
            int size = weakRefs.size();
            for(int i = 0; i < size; i ++) {
                weakRefs.poll();
            }
        }

        private static void setup() {
            timer.scheduleAtFixedRate(new ReferenceCleaner(), 0, 1000);
        }
    }
}
