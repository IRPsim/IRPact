package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public class Counter {

    protected long initial;
    protected boolean setZeroOnUpdateIfNegative;

    public Counter() {
        this(0L);
    }

    public Counter(long initial) {
        this(initial, false);
    }

    public Counter(long initial, boolean setZeroOnUpdateIfNegative) {
        this.initial = initial;
        this.setZeroOnUpdateIfNegative = setZeroOnUpdateIfNegative;
    }

    public void reset(long value) {
        this.initial = value;
    }

    public void setZeroOnUpdateIfNegative(boolean setZeroIfNegative) {
        this.setZeroOnUpdateIfNegative = setZeroIfNegative;
    }

    public boolean shouldSetZeroOnUpdateIfNegative() {
        return setZeroOnUpdateIfNegative;
    }

    public void inc() {
        inc(1L);
    }

    public void inc(long delta) {
        if(setZeroOnUpdateIfNegative && initial < 0L) {
            initial = delta;
        } else {
            initial += delta;
        }
    }

    public synchronized void syncInc() {
        inc();
    }

    public synchronized void syncInc(long delta) {
        inc(delta);
    }

    public long get() {
        return initial;
    }
}
