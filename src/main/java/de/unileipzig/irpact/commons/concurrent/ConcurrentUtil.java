package de.unileipzig.irpact.commons.concurrent;

import de.unileipzig.irpact.commons.exception.UncheckedException;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public final class ConcurrentUtil {

    protected ConcurrentUtil() {
    }

    public static void sleep(long time, TimeUnit unit) throws InterruptedException {
        long ms = unit.toMillis(time);
        Thread.sleep(ms);
    }

    public static void sleepSilently(long time, TimeUnit unit) throws UncheckedException {
        try {
            sleep(time, unit);
        } catch (InterruptedException e) {
            throw new UncheckedException(e);
        }
    }

    public static void sleepSilently(long ms) throws UncheckedException {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new UncheckedException(e);
        }
    }

    public static void awaitSilently(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new UncheckedException(e);
        }
    }

    public static void startAll(Collection<? extends Thread> threads) {
        for(Thread t: threads) {
            t.start();
        }
    }

    public static void joinAll(Collection<? extends Thread> threads) throws InterruptedException {
        for(Thread t: threads) {
            t.join();
        }
    }

    public static void joinAllSilently(Collection<? extends Thread> threads)  {
        try {
            joinAll(threads);
        } catch (InterruptedException e) {
            throw new UncheckedException(e);
        }
    }
}
