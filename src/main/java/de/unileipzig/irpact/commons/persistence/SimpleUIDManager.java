package de.unileipzig.irpact.commons.persistence;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Creates unique long ids. This implementation is thread safe.
 *
 * @author Daniel Abitz
 */
public class SimpleUIDManager implements UIDManager {

    protected final Lock LOCK = new ReentrantLock();
    protected long nextId;

    public SimpleUIDManager() {
        this(0L);
    }

    public SimpleUIDManager(long nextId) {
        setNextUID(nextId);
    }

    public void setNextUID(long nextId) {
        LOCK.lock();
        try {
            this.nextId = nextId;
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public long peekUID() {
        return nextId;
    }

    @Override
    public long getUID() {
        LOCK.lock();
        try {
            return nextId++;
        } finally {
            LOCK.unlock();
        }
    }
}
