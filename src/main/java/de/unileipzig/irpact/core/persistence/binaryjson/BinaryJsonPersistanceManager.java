package de.unileipzig.irpact.core.persistence.binaryjson;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.IRPactException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.core.persistence.binaryjson.meta.ClassManagerPR;
import de.unileipzig.irpact.core.persistence.binaryjson.meta.MetaPR;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.function.ToLongFunction;

/**
 * @author Daniel Abitz
 */
public class BinaryJsonPersistanceManager  extends NameableBase implements PersistManager {

    private static final Persistable PLACEHOLDER = new Persistable() {
        @Override
        public long getUID() {
            throw new PlaceholderException();
        }

        @Override
        public String getUIDString() {
            throw new PlaceholderException();
        }
    };

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BinaryJsonPersistanceManager.class);

    protected static final long FIRST_UID = 2L;
    protected final ToLongFunction<?> ENSURE_GET_UID = this::uncheckedEnsureGetUID;

    protected final Object metaDummy = new Object();
    protected final RestoreHelper restoreHelper = new RestoreHelper();
    protected final ClassManager classManager = new ClassManager();
    protected final Map<Holder, Persistable> persistableMap = new LinkedHashMap<>();
    protected final Set<Holder> requiresSetupCache = new LinkedHashSet<>();
    protected final Map<Class<?>, Persister<?>> persisterMap = new LinkedHashMap<>();

    protected UIDManager uidManager;
    protected Holder metaHolder;
    protected MetaPR metaPR;
    protected Holder classManagerHolder;
    protected ClassManagerPR classManagerPR;
    protected boolean useGeneric = false;

    public BinaryJsonPersistanceManager() {
        init();
    }

    private void init() {
        setUidManager(new SimpleUIDManager(FIRST_UID));
        BinaryJsonUtil.registerDefaults(this);
        classManager.enable();
        classManager.setStoreMode();
        restoreHelper.setClassManager(classManager);
        restoreHelper.setPrintLoggableOnPersist(false);

        metaHolder = newHolder(metaDummy);
        metaPR = new MetaPR(JsonUtil.SMILE.createObjectNode());
        persistableMap.put(metaHolder, metaPR);

        classManagerHolder = newHolder(classManager);
        classManagerPR = new ClassManagerPR(JsonUtil.SMILE.createObjectNode(), classManager);
        persistableMap.put(classManagerHolder, classManagerPR);
    }

    public void enableGeneric() {
        this.useGeneric = true;
    }

    public void disableGeneric() {
        this.useGeneric = false;
    }

    public boolean isGenericEnabled() {
        return useGeneric;
    }

    public void setUidManager(UIDManager uidManager) {
        this.uidManager = uidManager;
    }

    @Override
    public long newUID() {
        return uidManager.nextUID();
    }

    @Override
    public void persist(MetaData metaData) throws PersistException {
        metaPR.store(metaData);
    }

    @Override
    public <T> void persist(T object) throws PersistException {
        prepare(object);
        setup(object);
        finalizePersist();
    }

    @Override
    public <T> void persist(MetaData metaData, T object) throws PersistException {
        persist(metaData);
        persist(object);
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
            setupPersisterForInit(persister);
            Persistable persistable = persister.initalizePersist(object, this);
            if(persistable == null) {
                throw new PersistException("persistable is null: " + object.getClass());
            }
            //store
            if(persistableMap.put(holder, persistable) != PLACEHOLDER) {
                throw new PersistException("instance already exists: " + object.getClass());
            }
            requiresSetupCache.add(holder);
        }
    }

    protected <T> void setup(T object) throws PersistException {
        Holder holder = new Holder(object);
        setup(holder);
    }

    protected  <T> void setup(Holder holder) throws PersistException {
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
            setupPersisterForSetup(persister);
            persister.setupPersist(object, persistable, this);
            requiresSetupCache.remove(holder);
        }
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

    protected Holder newHolder(Object obj) {
        return new Holder(obj);
    }

    protected boolean isNotPersisted(Holder holder) {
        return !persistableMap.containsKey(holder);
    }

    protected boolean requiresSetup(Holder holder) {
        return requiresSetupCache.contains(holder);
    }

    protected <T> void setupPersisterForInit(Persister<T> persister) {
        ((BinaryPRBase<T>) persister).setRestoreHelper(restoreHelper);
    }

    protected <T> void setupPersisterForSetup(Persister<T> persister) {
        ((BinaryPRBase<T>) persister).setRestoreHelper(restoreHelper);
    }

    @Override
    public Collection<Persistable> getPersistables() {
        return persistableMap.values();
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> ToLongFunction<T> ensureGetUIDFunction() {
        return (ToLongFunction<T>) ENSURE_GET_UID;
    }

    public <T> boolean register(BinaryPersister<T> persister) {
        if(persisterMap.containsKey(persister.getType())) {
            return false;
        } else {
            persisterMap.put(persister.getType(), persister);
            return true;
        }
    }

    public <T> void ensureRegister(BinaryPersister<T> persister) {
        if(!register(persister)) {
            throw new IllegalArgumentException("class '" + persister.getType().getName() + "' already exists");
        }
    }

    protected <T> GenericPR<T> createGeneric(Class<T> c) throws PersistException {
        if(GenericPR.persistWith(c, this)) {
            try {
                LOGGER.trace(IRPSection.PERSIST, "create generic PR-handler for '{}'", c.getName());
                GenericPR<T> persister = new GenericPR<>(c);
                ensureRegister(persister);
                return persister;
            } catch (IRPactException e) {
                throw new PersistException(e);
            }
        } else {
            throw new PersistException("missing persister for class '" + c + "'");
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> BinaryPersister<T> ensureGetPersister(T obj) throws PersistException {
        BinaryPersister<T> persister = (BinaryPersister<T>) persisterMap.get(obj.getClass());
        if(persister == null) {
            if(useGeneric) {
                persister = (BinaryPersister<T>) createGeneric(obj.getClass());
            } else {
                throw new PersistException("missing persister for class '" + obj.getClass() + "'");
            }
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
