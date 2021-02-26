package de.unileipzig.irpact.jadex.persistance.binary;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.persistence.Restorer;
import de.unileipzig.irpact.jadex.persistance.binary.impl.*;

import java.util.*;
import java.util.function.IntFunction;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public class BinaryJsonRestoreManager implements RestoreManager {

    protected final Map<BinaryJsonData, Object> restoredMap = new HashMap<>();
    protected final Map<Long, BinaryJsonData> uidData = new HashMap<>();
    protected final Map<String, Restorer<?>> restorerMap = new HashMap<>();
    protected Object initalInstance;
    protected Object restoredInstance;
    protected Integer hash;

    public BinaryJsonRestoreManager() {
        init();
    }

    private void init() {
        ensureRegister(BasicAdoptedProductPR.INSTANCE);
        ensureRegister(BasicConsumerAgentAttributePR.INSTANCE);
        ensureRegister(BasicConsumerAgentGroupAffinityMappingPR.INSTANCE);
        ensureRegister(BasicConsumerAgentGroupAttributePR.INSTANCE);
        ensureRegister(BasicConsumerAgentGroupAttributeSupplierPR.INSTANCE);
        ensureRegister(BasicConsumerAgentSpatialAttributeSupplierPR.INSTANCE);
        ensureRegister(BasicDistanceEvaluatorPR.INSTANCE);
        ensureRegister(BasicEdgePR.INSTANCE);
        ensureRegister(BasicJadexLifeCycleControlPR.INSTANCE);
        ensureRegister(BasicJadexSimulationEnvironmentPR.INSTANCE);
        ensureRegister(BasicNeedPR.INSTANCE);
        ensureRegister(BasicPoint2DPR.INSTANCE);
        ensureRegister(BasicProductAttributePR.INSTANCE);
        ensureRegister(BasicProductGroupAttributePR.INSTANCE);
        ensureRegister(BasicProductGroupPR.INSTANCE);
        ensureRegister(BasicProductPR.INSTANCE);
        ensureRegister(BasicSocialGraphPR.INSTANCE);
        ensureRegister(BasicVersionPR.INSTANCE);
        ensureRegister(BooleanDistributionPR.INSTANCE);
        ensureRegister(CompleteGraphTopologyPR.INSTANCE);
        ensureRegister(ConstantUnivariateDoubleDistributionPR.INSTANCE);
        ensureRegister(DiscreteTimeModelPR.INSTANCE);
        ensureRegister(FixProcessModelFindingSchemePR.INSTANCE);
        ensureRegister(FixProductFindingSchemePR.INSTANCE);
        ensureRegister(FreeNetworkTopologyPR.INSTANCE);
        ensureRegister(InversePR.INSTANCE);
        ensureRegister(JadexConsumerAgentGroupPR.INSTANCE);
        ensureRegister(NoDistancePR.INSTANCE);
        ensureRegister(ProductThresholdInterestPR.INSTANCE);
        ensureRegister(ProductThresholdInterestSupplySchemePR.INSTANCE);
        ensureRegister(ProxyConsumerAgentPR.INSTANCE);
        ensureRegister(ProxySimulationAgentPR.INSTANCE);
        ensureRegister(RADataSupplierPR.INSTANCE);
        ensureRegister(RAModelDataPR.INSTANCE);
        ensureRegister(RandomBoundedIntegerDistributionPR.INSTANCE);
        ensureRegister(RAProcessModelPR.INSTANCE);
        ensureRegister(RAProcessPlanPR.INSTANCE);
        ensureRegister(RndPR.INSTANCE);
        ensureRegister(Space2DPR.INSTANCE);
        ensureRegister(SpatialDoubleAttributeBasePR.INSTANCE);
        ensureRegister(SpatialStringAttributeBasePR.INSTANCE);
        ensureRegister(UncertaintyAttributePR.INSTANCE);
        ensureRegister(UncertaintyGroupAttributePR.INSTANCE);
        ensureRegister(UnlinkedGraphTopologyPR.INSTANCE);
        ensureRegister(WeightedDiscreteSpatialDistributionPR.INSTANCE);
    }

    public <T> boolean register(Restorer<T> restorer) {
        if(restorerMap.containsKey(restorer.getType().getName())) {
            return false;
        } else {
            restorerMap.put(restorer.getType().getName(), restorer);
            return true;
        }
    }

    public <T> void ensureRegister(Restorer<T> restorer) {
        if(!register(restorer)) {
            throw new IllegalArgumentException("class '" + restorer.getType().getName() + "' already exists");
        }
    }

    protected boolean isNotRestored(BinaryJsonData data) {
        return !restoredMap.containsKey(data);
    }

    protected boolean isRestored(BinaryJsonData data) {
        return restoredMap.containsKey(data);
    }

    protected BinaryJsonData ensureGetData(long uid) {
        BinaryJsonData data = uidData.get(uid);
        if(data == null) {
            throw new NoSuchElementException("missing data for uid: " + uid);
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    protected <T> T ensureGetObject(BinaryJsonData data) {
        T obj = (T) restoredMap.get(data);
        if(obj == null) {
            throw new NoSuchElementException("missing object");
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    protected <T> Restorer<T> ensureGetRestorer(String type) {
        Restorer<T> restorer = (Restorer<T>) restorerMap.get(type);
        if(restorer == null) {
            throw new NoSuchElementException("missing restorer for '" + type + "'");
        }
        return restorer;
    }

    @Override
    public void restore(Collection<? extends Persistable> coll) throws RestoreException {
        restoredInstance = null;

        //phase 1
        for(Persistable persistable: coll) {
            initalize(persistable);
        }
        //phase 2
        for(Persistable persistable: coll) {
            setup(persistable);
        }
        //phase 3
        for(Persistable persistable: coll) {
            finalize(persistable);
        }
    }

    protected void initalize(Persistable persistable) throws RestoreException {
        BinaryJsonData data = check(persistable);
        if(isRestored(data)) {
            return;
        }
        String type = data.ensureGetType();
        Restorer<?> restorer = ensureGetRestorer(type);
        Object object = restorer.initalize(data, this);
        restoredMap.put(data, object);
        uidData.put(data.getUID(), data);
    }

    protected <T> void setup(Persistable persistable) throws RestoreException {
        BinaryJsonData data = check(persistable);
        String type = data.ensureGetType();
        Restorer<T> restorer = ensureGetRestorer(type);
        T object = ensureGetObject(data);
        restorer.setup(data, object, this);
    }

    protected <T> void finalize(Persistable persistable) throws RestoreException {
        BinaryJsonData data = check(persistable);
        String type = data.ensureGetType();
        Restorer<T> restorer = ensureGetRestorer(type);
        T object = ensureGetObject(data);
        restorer.finalize(data, object, this);
    }

    @Override
    public void setInitialInstance(Object initial) {
        this.initalInstance = initial;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInitialInstance() {
        if(initalInstance == null) {
            throw new NoSuchElementException("initial instance");
        }
        return (T) initalInstance;
    }

    @Override
    public void setRestoredInstance(Object restored) {
        if(restoredInstance != null) {
            throw new IllegalStateException("restored instance already set");
        }
        this.restoredInstance = restored;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getRestoredInstance() {
        if(restoredInstance == null) {
            throw new NoSuchElementException("restored instance");
        }
        return (T) restoredInstance;
    }

    @Override
    public void setValidationHash(int hash) {
        if(this.hash != null) {
            throw new IllegalStateException("validation hash already set");
        }
        this.hash = hash;
    }

    @Override
    public int getValidationHash() {
        if(hash == null) {
            throw new NoSuchElementException("hash");
        }
        return hash;
    }

    @Override
    public <T> T ensureGet(long uid) throws NoSuchElementException {
        BinaryJsonData data = ensureGetData(uid);
        return ensureGetObject(data);
    }

    @Override
    public <T> T[] ensureGetAll(long[] uids, IntFunction<T[]> arrCreator) throws NoSuchElementException {
        T[] out = arrCreator.apply(uids.length);
        for(int i = 0; i < uids.length; i++) {
            out[i] = ensureGet(uids[i]);
        }
        return out;
    }

    @Override
    public <K, V> Map<K, V> ensureGetAll(Map<Long, Long> idMap) throws NoSuchElementException {
        Map<K, V> out = new LinkedHashMap<>();
        for(Map.Entry<Long, Long> entry: idMap.entrySet()) {
            K key = ensureGet(entry.getKey());
            V value = ensureGet(entry.getValue());
            if(out.containsKey(key)) {
                throw new IllegalArgumentException("key '" + key + "' already exists");
            }
            out.put(key, value);
        }
        return out;
    }

    @Override
    public <T> T ensureGetSameClass(Class<T> c) throws NoSuchElementException {
        for(Object value: restoredMap.values()) {
            if(value.getClass() == c) {
                return c.cast(value);
            }
        }
        throw new NoSuchElementException(c.getName());
    }

    //=========================
    //util
    //=========================

    public static BinaryJsonData check(Persistable persistable) {
        if(persistable instanceof BinaryJsonData) {
            return (BinaryJsonData) persistable;
        } else {
            throw new IllegalArgumentException("no BinaryJsonData");
        }
    }
}
