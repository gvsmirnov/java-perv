package ru.gvsmirnov.perv.labs.time;

import ru.gvsmirnov.perv.labs.util.OrderedWrapper;

import java.util.concurrent.PriorityBlockingQueue;

class Savior implements Runnable {

    private final PriorityBlockingQueue<OrderedWrapper<ObsessedTimeKiller>> schedule = new PriorityBlockingQueue<>();

    public void schedule(long afterNanos, ObsessedTimeKiller whoToStop) {
        schedule.offer(new OrderedWrapper<>(afterNanos, whoToStop));
    }

    @Override
    public void run() {
        while(true) {
            OrderedWrapper<ObsessedTimeKiller> badGuy = null;
            try {
                badGuy = schedule.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            long targetTime = System.nanoTime() + badGuy.ordinal;
            while(System.nanoTime() < targetTime);

            // There could be a race between invoking killingSpree() and enoughDeaths(), hence the loop
            while(!badGuy.value.isStopped()) {
                badGuy.value.enoughDeaths();
            }
        }
    }

}
