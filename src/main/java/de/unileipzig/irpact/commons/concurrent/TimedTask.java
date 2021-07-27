package de.unileipzig.irpact.commons.concurrent;

/**
 * @author Daniel Abitz
 */
public class TimedTask implements Runnable {

    protected Thread runningThread;
    protected boolean canceled = false;
    protected long delay;
    protected Runnable task;

    public TimedTask(long delay, Runnable task) {
        this.delay = delay;
        this.task = task;
    }

    @SuppressWarnings({"WhileLoopSpinsOnField", "StatementWithEmptyBody"})
    public void start() {
        if(!isRunning()) {
            new Thread(this).start();
            //spin
            while(runningThread == null) {}
        }
    }

    public boolean isRunning() {
        return runningThread != null;
    }

    public void cancel() {
        if(runningThread != null) {
            canceled = true;
            runningThread.interrupt();
        }
    }

    public boolean wasCanceled() {
        return canceled;
    }

    @Override
    public void run() {
        if(runningThread != null) {
            throw new IllegalStateException("already running");
        }

        canceled = false;
        runningThread = Thread.currentThread();

        try {
            if(delay > 0L) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    if(canceled) {
                        return;
                    }
                }
            }

            if(task != null) {
                task.run();
            }
        } finally {
            runningThread = null;
        }
    }
}
