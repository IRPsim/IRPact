package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.util.IdManager;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class ClassManager {

    private final Map<String, Long> class2id = new LinkedHashMap<>();
    private final Map<Long, String> id2class = new LinkedHashMap<>();
    private final IdManager ids = new IdManager(0L);
    private boolean store;
    private boolean enabled;

    public ClassManager() {
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public Map<String, Long> getClass2Id() {
        return class2id;
    }

    public Map<Long, String> getId2Class() {
        return id2class;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setStoreMode() {
        store = true;
    }

    public void setReadMode() {
        store = false;
    }

    private void requiresStoreMode() {
        if(!store) {
            throw new IllegalStateException("read mode");
        }
    }

    private void requiresReadMode() {
        if(store) {
            throw new IllegalStateException("store mode");
        }
    }

    public void set(Class<?> c, long id) {
        requiresReadMode();
        set(c.getName(), id);
    }

    public void set(String c, long id) {
        requiresReadMode();
        class2id.put(c, id);
        id2class.put(id, c);
    }

    public long getId(Class<?> c) {
        requiresStoreMode();

        if(class2id.containsKey(c.getName())) {
            return class2id.get(c.getName());
        } else {
            long id = ids.nextId();
            class2id.put(c.getName(), id);
            id2class.put(id, c.getName());
            return id;
        }
    }

    public String getClass(long id) {
        requiresReadMode();

        String c = id2class.get(id);
        if(c == null) {
            throw new NoSuchElementException("missing class: " + id);
        }
        return c;
    }
}
