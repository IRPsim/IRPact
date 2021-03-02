package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.LifeCycleControl;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class KillSwitch implements Runnable {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(KillSwitch.class);

    protected Thread killThread;
    protected boolean running = true;
    protected boolean finished = false;

    protected long timeout;
    protected LifeCycleControl control;

    public KillSwitch() {
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setControl(LifeCycleControl control) {
        this.control = control;
    }

    public void start() {
        if(killThread != null) {
            return;
        }
        if(timeout < 1L) {
            LOGGER.info("KillSwitch disabled, timeout: {}", timeout);
            return;
        }
        killThread = new Thread(this);
        killThread.start();
    }

    public boolean isRunning() {
        return killThread != null;
    }

    public void reset() {
        running = true;
    }

    public void finished() {
        finished = true;
        killThread.interrupt();
    }

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        LOGGER.debug("Start KillSwitch with " + timeout + "ms.");
        while(running) {
            running = false;
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                //ignore
            }
        }
        if(finished) {
            LOGGER.debug("KillSwitch disabled");
        } else {
            LOGGER.warn("TIMEOUT - Simulation will be terminated!");
            control.terminateTimeout();
        }
        killThread = null;
    }
}
