package de.unileipzig.irpact.commons.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public class BasicPersistManager implements PersistManager {

    private static final Persistable PLACEHOLDER = new Persistable() {
        @Override
        public long getUID() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUIDString() {
            throw new UnsupportedOperationException();
        }
    };

    protected final Map<Holder, Persistable> persistableMap = new HashMap<>();
    protected final Map<Class<?>, Persister<?>> persisterMap = new HashMap<>();
    protected UIDManager uidManager = new SimpleUIDManager();

    public BasicPersistManager() {
    }

    public void setUidManager(UIDManager uidManager) {
        this.uidManager = uidManager;
    }

    public <T> boolean register(Persister<T> persister) {
        if(persisterMap.containsKey(persister.getType())) {
            return false;
        } else {
            persisterMap.put(persister.getType(), persister);
            return true;
        }
    }

    public <T> void ensureRegister(Persister<T> persister) {
        if(!register(persister)) {
            throw new IllegalArgumentException("class '" + persister.getType().getName() + "' already exists");
        }
    }

    protected boolean isNotPersisted(Holder holder) {
        return !persistableMap.containsKey(holder);
    }

    @SuppressWarnings("unchecked")
    protected <T> Persister<T> ensureGetPersister(T obj) {
        Persister<T> persister = (Persister<T>) persisterMap.get(obj.getClass());
        if(persister == null) {
            throw new IllegalArgumentException("missing persister for class '" + obj.getClass() + "'");
        }
        return persister;
    }
    
    protected Persistable ensureGetPersistable(Holder holder) throws NoSuchElementException {
        Persistable persistable = persistableMap.get(holder);
        if(persistable == null) {
            throw new NoSuchElementException("missing persistable for class '" + holder.getHoldedClass().getName() + "'");
        }
        return persistable;
    }

    @Override
    public <T> void persist(T object) {
        prepare(object);
        setup(object);
    }

    @Override
    public <T> void prepare(T object) {
        Holder holder = new Holder(object);
        if(isNotPersisted(holder)) {
            //reserve
            if(persistableMap.put(holder, PLACEHOLDER) != null) {
                throw new IllegalStateException("entry already exists: " + object.getClass());
            }
            Persister<T> persister = ensureGetPersister(object);
            Persistable persistable = persister.initalizePersist(object, this);
            //store
            if(persistableMap.put(holder, persistable) != PLACEHOLDER) {
                throw new IllegalStateException("entry already exists: " + object.getClass());
            }
        }
    }

    private <T> void setup(T object) {
        Holder holder = new Holder(object);
        if(isNotPersisted(holder)) {
            Persister<T> persister = ensureGetPersister(object);
            Persistable persistable = ensureGetPersistable(holder);
            if(persistable == PLACEHOLDER) {
                throw new IllegalStateException("placeholder found: " + object.getClass());
            }
            persister.setupPersist(object, persistable, this);
        } else {
            throw new IllegalStateException("not prepared: " + object.getClass());
        }
    }

    @Override
    public Collection<Persistable> getPersistables() {
        return persistableMap.values();
    }

    @Override
    public long newUID() {
        return uidManager.getUID();
    }

    @Override
    public <T> long ensureGetUID(T object) throws NoSuchElementException {
        Holder holder = new Holder(object);
        if(isNotPersisted(holder)) {
            throw new IllegalStateException("not prepared: " + object.getClass());
        }
        Persistable persistable = ensureGetPersistable(holder);
        return persistable.getUID();
    }

    /**
     * Helper class for system hash.
     *
     * @author Daniel Abitz
     */
    public static final class Holder {

        private final Object OBJ;

        private Holder(Object obj) {
            this.OBJ = obj;
        }

        @SuppressWarnings("unchecked")
        public <T> T getHoldedObject() {
            return (T) OBJ;
        }

        public Class<?> getHoldedClass() {
            return OBJ.getClass();
        }

        @Override
        public String toString() {
            return "Holder{" + OBJ + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Holder)) return false;
            Holder holder = (Holder) o;
            return OBJ == holder.OBJ;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(OBJ);
        }
    }
}
