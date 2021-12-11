package de.unileipzig.irpact.core.persistence.binaryjson2;

import de.unileipzig.irpact.commons.persistence.SimpleUIDManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ClassManager {

    protected SimpleUIDManager idManager = new SimpleUIDManager();
    protected Map<Class<?>, Long> ids = new HashMap<>();
    protected Map<Long, Class<?>> classes = new HashMap<>();

    public ClassManager() {
    }

    public void reset() {
        idManager.setNextUID(0);
    }

    public long getId(Class<?> c) {
        Long id = ids.get(c);
        if(id == null) {
            id = idManager.nextUID();
            ids.put(c, id);
            classes.put(id, c);
        }
        return id;
    }

    public Class<?> getClass(long id) {
        return classes.get(id);
    }
}
