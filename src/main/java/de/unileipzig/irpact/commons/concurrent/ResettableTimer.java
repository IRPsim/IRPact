package de.unileipzig.irpact.commons.concurrent;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementierung eines sehr einfachen und naiven Timers, welcher aber ohne das erstellen neuer Threads auskommt.
 * Created by daniel on 11.03.2020.
 * @since 0.0
 */
public final class ResettableTimer {

    private final Runnable task = this::runIt;
    private final AtomicBoolean resettet = new AtomicBoolean(true);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Runnable runOnTimeout;
    private final Runnable runOnInterrupt;
    private long timeout;
    private TimeUnit unit;

    public ResettableTimer(
            Runnable runOnTimeout,
            Runnable runOnInterrupt,
            long timeout,
            TimeUnit unit) {
        this.runOnTimeout = Objects.requireNonNull(runOnTimeout, "runOnTimeout");
        this.runOnInterrupt = Objects.requireNonNull(runOnInterrupt, "runOnInterrupt");
        this.timeout = timeout;
        this.unit = Objects.requireNonNull(unit, "unit");
    }

    public Thread start() {
        return start(Thread::new);
    }

    public Thread start(ThreadFactory factory) {
        checkRunning();
        Thread t = factory.newThread(task);
        t.start();
        return t;
    }

    public void execute(Executor exec) {
        checkRunning();
        exec.execute(task);
    }

    public Future<?> submit(ExecutorService exec) {
        checkRunning();
        return exec.submit(task);
    }

    private void checkRunning() {
        if(running.get()) {
            throw new IllegalStateException("running");
        }
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setTimeout(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
    }

    public void reset() {
        resettet.set(true);
    }

    private void runIt() {
        running.set(true);
        boolean interrupted = false;
        while(resettet.getAndSet(false)) {
            long millis = unit.toMillis(timeout);
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                interrupted = true;
                break;
            }
        }
        if(interrupted) {
            runOnInterrupt.run();
        } else {
            runOnTimeout.run();
        }
    }
}