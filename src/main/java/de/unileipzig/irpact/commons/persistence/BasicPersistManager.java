package de.unileipzig.irpact.commons.persistence;

import java.util.*;

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

    protected final Map<Holder, Persistable> persistableMap = new LinkedHashMap<>();
    protected final Set<Holder> requiresSetupCache = new LinkedHashSet<>();
    protected final Map<Class<?>, Persister<?>> persisterMap = new LinkedHashMap<>();
    protected UIDManager uidManager;

    public BasicPersistManager() {
        setUidManager(new SimpleUIDManager(0L));
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

    protected boolean requiresSetup(Holder holder) {
        return requiresSetupCache.contains(holder);
    }

    @SuppressWarnings("unchecked")
    protected <T> Persister<T> ensureGetPersister(T obj) throws PersistException {
        Persister<T> persister = (Persister<T>) persisterMap.get(obj.getClass());
        if(persister == null) {
            throw new PersistException("missing persister for class '" + obj.getClass() + "'");
        }
        return persister;
    }

    protected Persistable ensureGetPersistable(Holder holder) throws PersistException {
        Persistable persistable = persistableMap.get(holder);
        if(persistable == null) {
            throw new PersistException("missing persistable for class '" + holder.getHoldedClass().getName() + "'");
        }
        return persistable;
    }

    @Override
    public <T> void persist(T object) throws PersistException {
        prepare(object);
        setup(object);
        finalizePersist();
    }

    protected void finalizePersist() throws PersistException {
        do {
            Set<Holder> temp = new LinkedHashSet<>(requiresSetupCache);
            for(Holder holder: temp) {
                setup(holder); //setup removes holder from requiresSetupCache
            }
            temp.clear();
        } while (requiresSetupCache.size() > 0);
    }

    @Override
    public <T> void prepare(T object) throws PersistException {
        Holder holder = new Holder(object);
        if(isNotPersisted(holder)) {
            //reserve
            if(persistableMap.put(holder, PLACEHOLDER) != null) {
                throw new PersistException("entry already exists: " + object.getClass());
            }
            Persister<T> persister = ensureGetPersister(object);
            Persistable persistable = persister.initalizePersist(object, this);
            if(persistable == null) {
                throw new PersistException("persistable is null: " + object.getClass());
            }
            //store
            if(persistableMap.put(holder, persistable) != PLACEHOLDER) {
                throw new PersistException("instance already exists: " + object.getClass());
            }
            requiresSetupCache.add(holder);
            //System.out.println("XXXX " + persistable.getUID() + " " + object.getClass()); //TODO
        }
    }

    private <T> void setup(T object) throws PersistException {
        Holder holder = new Holder(object);
        setup(holder);
    }

    private <T> void setup(Holder holder) throws PersistException {
        T object = holder.getHoldedObject();
        if(isNotPersisted(holder)) {
            throw new PersistException("not prepared: " + object.getClass());
        } else {
            if(!requiresSetup(holder)) {
                throw new PersistException("setup already called: " + object.getClass());
            }
            Persister<T> persister = ensureGetPersister(object);
            Persistable persistable = ensureGetPersistable(holder);
            if(persistable == PLACEHOLDER) {
                throw new PersistException("placeholder found: " + object.getClass());
            }
            persister.setupPersist(object, persistable, this);
            requiresSetupCache.remove(holder);
        }
    }

    @Override
    public Collection<Persistable> getPersistables() {
        return persistableMap.values();
    }

    @Override
    public long newUID() {
        return uidManager.nextUID();
    }

    @Override
    public <T> long ensureGetUID(T object) throws PersistException {
        Holder holder = new Holder(object);
        if(isNotPersisted(holder)) {
            throw new PersistException("not prepared: " + object.getClass());
        }
        Persistable persistable = ensureGetPersistable(holder);
        if(persistable == PLACEHOLDER) {
            throw new PersistException("placeholder found: " + object.getClass());
        }
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
