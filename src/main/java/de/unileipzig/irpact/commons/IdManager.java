package de.unileipzig.irpact.commons;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class IdManager {

    private long startId;
    private long nextId;

    public IdManager(long startId) {
        this.startId = startId;
        this.nextId = startId;
    }

    public void reset() {
        nextId = startId;
    }

    public void reset(long startId) {
        this.startId = startId;
        reset();
    }

    public long lastId() {
        if(nextId == startId) {
            throw new NoSuchElementException();
        }
        return nextId - 1;
    }

    public synchronized long nextId() {
        long next = nextId;
        nextId++;
        if(nextId == startId) {
            throw new IllegalStateException("id cycle");
        }
        return next;
    }
}
