package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.develop.Todo;

import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
@Todo("schnoener implementieren, siehe auskommentierte sachen")
public class IdManager implements ChecksumComparable {

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

//    public void restore(long startId, long nextId) {
//        this.startId = startId;
//        this.nextId = nextId;
//    }
//
//    public long getStartIdForPersist() {
//        return startId;
//    }
//
//    public long getNextIdForPersist() {
//        return nextId;
//    }

    public long lastId() {
        if(nextId == startId) {
            throw new NoSuchElementException();
        }
        return nextId - 1;
    }

    public long peekId() {
        return nextId;
    }

    public synchronized long nextId() {
        long next = nextId;
        nextId++;
        if(nextId == startId) {
            throw new IllegalStateException("id cycle");
        }
        return next;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(nextId);
    }
}
