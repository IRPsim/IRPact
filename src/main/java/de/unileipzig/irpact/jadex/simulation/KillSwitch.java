package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.simulation.LifeCycleControl;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Daniel Abitz
 */
public class KillSwitch implements Runnable {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(KillSwitch.class);

    protected static final String THREAD_NAME = "KillSwitch-Thread";

    protected final Lock LOCK = new ReentrantLock();
    protected final Condition COND = LOCK.newCondition();
    protected final AtomicBoolean KEEP_RUNNING = new AtomicBoolean(true);

    protected Thread killThread;
    protected boolean allowInterrupt = false;
    protected long cycles = 0L;
    protected long timeout = 0L;
    protected TimeUnit unit = TimeUnit.MILLISECONDS;
    protected LifeCycleControl control;

    public KillSwitch() {
    }

    public void setTimeout(long timeout) {
        this.timeout = Math.max(0L, timeout);
    }

    public void setTimeout(long timeout, TimeUnit unit) {
        setTimeout(timeout);
        setUnit(unit);
    }

    public long getTimeout() {
        return timeout;
    }

    public boolean isDisabled() {
        return isDisabled(getTimeout());
    }

    protected static boolean isDisabled(long timeout) {
        return timeout == 0L;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit == null ? TimeUnit.MILLISECONDS : unit;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setAllowInterrupt(boolean allowInterrupt) {
        this.allowInterrupt = allowInterrupt;
    }

    public boolean isAllowInterrupt() {
        return allowInterrupt;
    }

    public void setControl(LifeCycleControl control) {
        this.control = control;
    }

    public LifeCycleControl getControl() {
        return control;
    }

    public long getCycles() {
        return cycles;
    }

    public boolean start() {
        LOCK.lock();
        try {
            if(isRunning()) {
                return false;
            }

            if(isDisabled()) {
                return false;
            }

            killThread = new Thread(this, THREAD_NAME);
        } finally {
            LOCK.unlock();
        }

        cycles = 0L;
        killThread.start();
        return true;
    }

    public boolean isRunning() {
        return killThread != null;
    }

    protected boolean isNotSame(Thread t) {
        return killThread != t;
    }

    public boolean reset() {
        if(isRunning()) {
            KEEP_RUNNING.set(true);
            return true;
        } else {
            return false;
        }
    }

    public boolean cancel() {
        if(isRunning()) {
            LOGGER.trace("cancel KillSwitch...");
            LOCK.lock();
            try {
                COND.signalAll();
            } finally {
                LOCK.unlock();
            }
            LOGGER.trace("...canceled");
            return true;
        } else {
            return false;
        }
    }

    protected void handleTimeout() {
        LifeCycleControl control = getControl();
        if(control != null) {
            control.terminateTimeout();
        } else {
            LOGGER.warn("no life cycle control found");
        }
    }

    protected void cleanUpRun(long cycles) {
        LOGGER.trace("clean up (cycles={})", cycles);
        this.cycles = cycles;
        killThread = null;
    }

    protected static String getInfo(boolean expired, boolean interrupted) {
        if(expired) return "TIMEOUT";
        if(interrupted) return "INTERRUPTED";
        return "UNDEFINED";
    }

    @Override
    public void run() {
        if(isNotSame(Thread.currentThread())) {
            throw new IllegalStateException("running thread is not the same");
        }

        final long timeout = getTimeout();
        final TimeUnit unit = getUnit();

        if(isDisabled(timeout)) {
            LOGGER.warn("Cancel, illegal timeout detected: {} {}.", timeout, unit);
            cleanUpRun(-1L);
            return;
        }

        LOGGER.trace("start with {} {}.", timeout, unit);
        long cycles = 0L;
        boolean expired = false;
        boolean finished = false;
        boolean interrupted = false;
        while(KEEP_RUNNING.getAndSet(false) && !interrupted && !finished) {
            cycles++;
            LOCK.lock();
            try {
                finished = COND.await(timeout, unit);
                expired = !finished;
            } catch (InterruptedException e) {
                if(isAllowInterrupt()) {
                    interrupted = true;
                }
                LOGGER.info("await interrupted (keepRunning={},interrupted={})", KEEP_RUNNING.get(), interrupted);
            } finally {
                LOCK.unlock();
            }
        }

        if(expired || interrupted) {
            LOGGER.warn("{} - simulation will be terminated!", getInfo(expired, interrupted));
            cleanUpRun(cycles);
            handleTimeout();
        } else {
            LOGGER.trace("canceled");
            cleanUpRun(cycles);
        }
    }

    public TimeoutException createException() {
        return new TimeoutException("timeout after " + cycles + " cylces (timeout: " + timeout + " " + unit + ")");
    }
}
