package de.unileipzig.irpact.commons.logging;

import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public class PercentageProgressNotifier implements ProgressNotifier {

    protected Consumer<? super PercentageProgressNotifier> publisher;
    protected long totalWork;
    protected long finishedWork;
    protected double percentage;
    protected double delta = 0.01;

    public PercentageProgressNotifier() {
    }

    public void setTotalWork(long totalWork) {
        this.totalWork = totalWork;
    }

    public void setFinishedWork(long finishedWork) {
        this.finishedWork = finishedWork;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void setPublisher(Consumer<? super PercentageProgressNotifier> publisher) {
        this.publisher = publisher;
    }

    public long getTotalWork() {
        return totalWork;
    }

    public long getFinishedWork() {
        return finishedWork;
    }

    public double getPercentage() {
        return percentage;
    }

    public double getDelta() {
        return delta;
    }

    @Override
    public boolean isFinished() {
        return finishedWork == totalWork;
    }

    @Override
    public void update(long count) {
        if(isFinished()) return;
        finishedWork = Math.min(Math.max(0, finishedWork + count), totalWork);
        double newPercentage = (double) finishedWork / (double) totalWork;
        double currentDelta = Math.abs(percentage - newPercentage);
        if(currentDelta >= delta || isFinished()) {
            percentage = newPercentage;
            publish();
        }
    }

    protected void publish() {
        if(publisher != null) {
            publisher.accept(this);
        }
    }
}
