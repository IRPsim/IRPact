package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Lalaland {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Lalaland.class);

    @Test
    void oi() {
        IRPLogging.initConsole();

        Lock lock = new ReentrantLock();
        Condition con = lock.newCondition();

        for(int i = 0; i < 10; i++) {
            final int ii = i;
            Runnable r = () -> {
                LOGGER.info("START {}", ii);
                lock.lock();
                try {
                    try {
                        con.await();
                        LOGGER.info("FINISH {}", ii);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    lock.unlock();
                }
            };
            Thread t = new Thread(r);
            t.start();
        }

        ConcurrentUtil.sleepSilently(5000);
        lock.lock();
        try {
            con.signalAll();
        } finally {
            lock.unlock();
        }
        ConcurrentUtil.sleepSilently(1000);
    }

    @Test
    void x() {
        Pattern p = Pattern.compile("[A-Za-z0-9ÄÖÜäöüß_]+");
        System.out.println(p.matcher("").matches());
        System.out.println(p.matcher("Hallo1ß").matches());
        System.out.println(p.matcher("Hallo1 Welt1").matches());
        System.out.println(p.matcher("Hallo1_Welt1").matches());
        System.out.println(p.matcher("Hallo1__Welt1").matches());
        System.out.println(p.matcher("äüöß1ÄÖÜ").matches());
    }
}
