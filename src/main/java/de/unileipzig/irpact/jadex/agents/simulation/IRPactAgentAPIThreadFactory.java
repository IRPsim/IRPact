package de.unileipzig.irpact.jadex.agents.simulation;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Daniel Abitz
 */
public class IRPactAgentAPIThreadFactory implements ThreadFactory {

    private static final IRPactAgentAPIThreadFactory INSTANCE = new IRPactAgentAPIThreadFactory();
    public static synchronized IRPactAgentAPIThreadFactory getInstance() {
        return INSTANCE;
    }

    private final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
    private final ThreadGroup GROUP;
    private final String PREFIX;

    private IRPactAgentAPIThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        GROUP = (s != null)
                ? s.getThreadGroup()
                : Thread.currentThread().getThreadGroup();
        PREFIX = "IRPactAgentThread-";
    }

    private String nextThreadName() {
        return PREFIX + THREAD_NUMBER.getAndIncrement();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(GROUP, r, nextThreadName(), 0);
        if(t.isDaemon()) {
            t.setDaemon(false);
        }
        if(t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
