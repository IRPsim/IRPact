package de.unileipzig.irpact.v2.commons.concurrent;

import de.unileipzig.irpact.v2.commons.exception.UncheckedException;

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
}
