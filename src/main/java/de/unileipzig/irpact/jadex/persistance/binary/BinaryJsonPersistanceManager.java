package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.persistence.BasicPersistManager;
import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.Persister;
import de.unileipzig.irpact.commons.persistence.SimpleUIDManager;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.jadex.persistance.binary.data.BinaryPRBase;
import de.unileipzig.irpact.jadex.persistance.binary.meta.ClassManagerPR;
import de.unileipzig.irpact.jadex.persistance.binary.meta.MetaPR;

/**
 * @author Daniel Abitz
 */
public class BinaryJsonPersistanceManager extends BasicPersistManager {

    protected static final long FIRST_UID = 2L;

    protected final Object settingsDummy = new Object();
    protected final RestoreHelper restoreHelper = new RestoreHelper();
    protected final ClassManager classManager = new ClassManager();

    protected Holder metaHolder;
    protected MetaPR metaPR;
    protected Holder classManagerHolder;
    protected ClassManagerPR classManagerPR;

    public BinaryJsonPersistanceManager() {
        init();
    }

    @Override
    protected void initUIDManager() {
        setUidManager(new SimpleUIDManager(FIRST_UID));
    }

    private void init() {
        setUidManager(new SimpleUIDManager(FIRST_UID));
        BinaryJsonUtil.registerDefaults(this);
        classManager.enable();
        classManager.setStoreMode();
        restoreHelper.setClassManager(classManager);
        restoreHelper.setPrintLoggableOnPersist(false);

        metaHolder = newHolder(settingsDummy);
        metaPR = new MetaPR(JsonUtil.SMILE.createObjectNode());
        persistableMap.put(metaHolder, metaPR);

        classManagerHolder = newHolder(classManager);
        classManagerPR = new ClassManagerPR(JsonUtil.SMILE.createObjectNode(), classManager);
        persistableMap.put(classManagerHolder, classManagerPR);
    }

    @Override
    public void handle(MetaData metaData) throws PersistException {
        metaPR.store(metaData);
    }

    @Override
    protected <T> void setupPersisterForInit(Persister<T> persister) {
        ((BinaryPRBase<T>) persister).setRestoreHelper(restoreHelper);
    }

    @Override
    protected <T> void setupPersisterForSetup(Persister<T> persister) {
        ((BinaryPRBase<T>) persister).setRestoreHelper(restoreHelper);
    }

    @Override
    public <T> boolean register(Persister<T> persister) {
        if(persister instanceof BinaryPersister) {
            return super.register(persister);
        } else {
            throw new IllegalArgumentException("requires: " + BinaryPersister.class.getName());
        }
    }

    public <T> boolean register(BinaryPersister<T> persister) {
        return super.register(persister);
    }

    @Override
    public <T> void ensureRegister(Persister<T> persister) {
        if(persister instanceof BinaryPersister) {
            super.ensureRegister(persister);
        } else {
            throw new IllegalArgumentException("requires: " + BinaryPersister.class.getName());
        }
    }

    public <T> void ensureRegister(BinaryPersister<T> persister) {
        super.ensureRegister(persister);
    }
}
