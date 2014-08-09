package ru.gvsmirnov.perv.labs.time;

import java.util.concurrent.locks.LockSupport;

public abstract class ObsessedTimeKiller extends TimeKiller {
    private final Savior savior;
    private volatile boolean stopped;


    protected ObsessedTimeKiller() {
        savior = new Savior();
        Thread thread = new Thread(savior);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void tryKill(long nanosToKill) {
        stopped = false;

        savior.schedule(nanosToKill, this);
        killingSpree();

        stopped = true;
    }

    public final boolean isStopped() {
        return stopped;
    }

    protected abstract void killingSpree();
    protected abstract void enoughDeaths();

    public static class UntimedParker extends ObsessedTimeKiller {

        private volatile Thread parkedThread;

        @Override
        protected void killingSpree() {
            this.parkedThread = Thread.currentThread();
            LockSupport.park();
        }

        @Override
        protected void enoughDeaths() {
            LockSupport.unpark(parkedThread);
        }
    }

    public static class UntimedWaiter extends ObsessedTimeKiller {

        private final Object object = new Object();


        @Override
        protected void killingSpree() {
            if(!isStopped()) {
                try {
                    synchronized (object) {
                        object.wait();
                    }
                } catch (InterruptedException ignored) {}
            }
        }

        @Override
        protected void enoughDeaths() {
            if(!isStopped()) {
                synchronized (object) {
                    object.notifyAll();
                }
            }
        }
    }
}
