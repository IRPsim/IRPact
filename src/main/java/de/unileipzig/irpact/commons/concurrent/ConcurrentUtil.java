package de.unileipzig.irpact.commons.concurrent;

import de.unileipzig.irpact.commons.annotation.Experimental;
import de.unileipzig.irpact.commons.exception.UncheckedInterruptedException;

import java.util.concurrent.*;

/**
 * @author Daniel Abitz
 */
public final class ConcurrentUtil {

    private ConcurrentUtil() {
    }

    public static Thread start(long delay, Runnable task) {
        Thread t = new Thread(() -> {
            sleepSilently(delay);
            task.run();
        });
        t.start();
        return t;
    }

    public static Thread start(long delay, String name, Runnable task) {
        Thread t = new Thread(() -> {
            sleepSilently(delay);
            task.run();
        }, name);
        t.start();
        return t;
    }

    public static void sleep(long time, TimeUnit unit) throws InterruptedException {
        long ms = unit.toMillis(time);
        Thread.sleep(ms);
    }

    public static void sleepSilently(long time, TimeUnit unit) throws UncheckedInterruptedException {
        try {
            sleep(time, unit);
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
        }
    }

    public static void sleepSilently(long ms) throws UncheckedInterruptedException {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new UncheckedInterruptedException(e);
        }
    }

    @Experimental
    public static ExecutorService newCachedThreadPool(ThreadFactory factory) {
        return Executors.newCachedThreadPool(factory);
    }

    @Experimental
    public static ExecutorService newCachedThreadPool(int cores, int maximumPool, ThreadFactory factory) {
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(
                cores,
                maximumPool,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                factory
        );
        tpe.allowCoreThreadTimeOut(true);
        return tpe;

    }
}
