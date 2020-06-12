package de.unileipzig.irpact.experimental.tests.clockStepsTicks;

import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public interface ClockAgentService {

    IFuture<Void> scheduleAt(double tick, Task task);

    IFuture<Void> waitForTick(Task task);

    IFuture<Void> waitForTicks(double ticks, Task task);
}
