package ru.gvsmirnov.perv.labs.gc;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;

import javax.management.NotificationEmitter;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Stream;

import static com.sun.management.GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION;
import static com.sun.management.GarbageCollectionNotificationInfo.from;

public class G1Demo {

    private static final int PAYLOAD_SIZE = Integer.getInteger("payload.size", 1024);
    private static final int INITIAL_OBJECT_COUNT = Integer.getInteger("initial.obj.count", 1_000_000);
    private static final int MAX_BRANCH_COUNT = Integer.getInteger("max.branch.count", 3);

    private static final long ITERATIONS = Long.getLong("iterations", 1_000_000);

    private static final double DISCARDING_RATE = Double.parseDouble(System.getProperty("discarding.rate", "0.1"));
    private static final double DISCARDING_RATIO = Double.parseDouble(System.getProperty("discarding.ratio", "0.5"));

    private static final int REVIVED_OBJECT_COUNT = Integer.getInteger("revived.obj.count", 10_000);


    public static volatile Object sink;

    public static void main(String[] args) {

        subscribeForGc();

        Data root = makeData(INITIAL_OBJECT_COUNT);


        for(long l = 0; l < ITERATIONS; l ++) {
            root = maybeDiscardSome(root);

            // Force some garbage unto the heap
            sink = new Object();
            LockSupport.parkNanos(1_000_000);
        }
    }

    private static Data maybeDiscardSome(Data root) {
        boolean shouldDiscard = ThreadLocalRandom.current().nextDouble() < DISCARDING_RATE;

        if(shouldDiscard) {
            long toDiscard = (long) (root.subTreeSize * DISCARDING_RATIO);

            return discard(root, toDiscard);
        } else {
            return append(root, REVIVED_OBJECT_COUNT);
        }
    }

    private static Data discard(Data root, long howManyToDiscard) {
        while(root.subTreeSize > 0 && howManyToDiscard > 0) {
//            System.err.print("Will prune " + howManyToDiscard + " leaves from a tree of " + root.subTreeSize);
            howManyToDiscard -= pruneLeaves(root, howManyToDiscard);
//            System.err.println("-> " + root.subTreeSize);
        }

        return root;
    }

    private static Data append(Data root, long howManyToAppend) {
        while(howManyToAppend > 0) {
//            System.err.print("Will append " + howManyToAppend + " leaves to a tree of " + root.subTreeSize);
            howManyToAppend -= appendLeaves(root, howManyToAppend);
//            System.err.println("-> " + root.subTreeSize);
        }

        return root;
    }

    private static long pruneLeaves(Data root, long maxLeavesToPrune) {
        if(root.subTreeSize == 0) {
            throw new AssertionError();
        }

        long pruned = 0;

        for(int i = 0; i < root.children.length && pruned < maxLeavesToPrune; i ++) {
            Data child = root.children[i];

            if(child == null) {
                continue;
            }

            if(child.subTreeSize == 0) {
                root.children[i] = null;
                pruned++;
            } else {
                pruned += pruneLeaves(child, maxLeavesToPrune - pruned);
            }
        }

        root.calculateSubTreeSize();

        return pruned;
    }

    private static long appendLeaves(Data root, long maxLeavesToAppend) {
        long appended = 0;

        for(int i = 0; i < root.children.length && appended < maxLeavesToAppend; i ++) {
            Data child = root.children[i];

            if(child == null) {
                int instanceCount = (int) Math.min(maxLeavesToAppend - 1, MAX_BRANCH_COUNT);
                root.children[i] = makeData(instanceCount);
                appended += instanceCount + 1;
            } else {
                appended += appendLeaves(child, maxLeavesToAppend - appended);
            }
        }

        root.calculateSubTreeSize();

        return appended;
    }

    private static Data makeData(int instanceCount) {
        if(instanceCount <= 1) {
            return new Data();
        }

        if(instanceCount <= MAX_BRANCH_COUNT) {
            Data[] children = new Data[MAX_BRANCH_COUNT];
            for(int i = 0; i < instanceCount - 1; i ++) {
                children[i] = new Data();
            }

            return new Data(children);
        }

        int childCount = (int) (Math.random() * MAX_BRANCH_COUNT) + 1;

        Data[] children = new Data[MAX_BRANCH_COUNT];

        for(int i = 0; i < childCount; i ++) {
            if(i < childCount - 1) {
                int childSize = (int) (Math.random() * instanceCount);
                instanceCount -= childSize;
                children[i] = makeData(childSize);
            } else {
                // -1 for the governing parent Data that we return
                children[i] = makeData(instanceCount - 1);
            }
        }

        return new Data(children);
    }

    private static class Data {
        private final byte[] payload = new byte[PAYLOAD_SIZE];
        private long subTreeSize;
        private Data[] children;

        public Data(Data... children) {
            this.children = children;
            calculateSubTreeSize();
        }

        private void calculateSubTreeSize() {
            this.subTreeSize = calculateSize(children);
        }

        private static long calculateSize(Data... subTree) {
            return Stream.of(subTree).filter(Objects::nonNull).mapToLong(data -> data.subTreeSize + 1).sum();
        }
    }

    private static void subscribeForGc() {
        ManagementFactory.getGarbageCollectorMXBeans().stream().filter(bean -> bean instanceof NotificationEmitter).forEach(bean -> {
            ((NotificationEmitter) bean).addNotificationListener((notification, handback) -> {
//                System.err.println("Notified");
                if (GARBAGE_COLLECTION_NOTIFICATION.equals(notification.getType())) {
                    GarbageCollectionNotificationInfo info = from((CompositeData) notification.getUserData());
                    com.sun.management.GarbageCollectorMXBean mxBean = (com.sun.management.GarbageCollectorMXBean) handback;

//                    System.err.println("GC notification");

                    GcInfo gcInfo = info.getGcInfo();
                    if (gcInfo != null) {

                        System.out.println(info.getGcName() + ", " + info.getGcAction() + ", " + mxBean.getName() + ", " + info.getGcCause() + ", " + gcInfo.getMemoryUsageBeforeGc() + ", " + gcInfo.getMemoryUsageAfterGc());
                    }
                }
            }, null, bean);
        });
    }
}
