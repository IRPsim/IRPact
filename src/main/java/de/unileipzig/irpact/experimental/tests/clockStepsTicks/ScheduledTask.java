package de.unileipzig.irpact.experimental.tests.clockStepsTicks;

import jadex.commons.future.Future;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public final class ScheduledTask<T> extends Future<T> {

    private final Task task;

    public ScheduledTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setRealFuture(IFuture<T> other) {
        //other.addResultListener(this);
    }

    @Override
    public void setResult(T result) {
        super.setResult(result);
    }
}
