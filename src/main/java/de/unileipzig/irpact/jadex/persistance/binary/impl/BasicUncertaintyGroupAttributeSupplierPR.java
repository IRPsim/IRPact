package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.BasicUncertaintyGroupAttributeSupplier;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicUncertaintyGroupAttributeSupplierPR extends BinaryPRBase<BasicUncertaintyGroupAttributeSupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicUncertaintyGroupAttributeSupplierPR.class);

    public static final BasicUncertaintyGroupAttributeSupplierPR INSTANCE = new BasicUncertaintyGroupAttributeSupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicUncertaintyGroupAttributeSupplier> getType() {
        return BasicUncertaintyGroupAttributeSupplier.class;
    }

    @Override
    public Persistable initalizePersist(BasicUncertaintyGroupAttributeSupplier object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        //data.putText(object.getName());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicUncertaintyGroupAttributeSupplier object, Persistable persistable, PersistManager manager) {
        BinaryJsonData data = check(persistable);

        Map<Long, List<String>> namesMap = new HashMap<>();
        Map<Long, List<Long>> uncertMap = new HashMap<>();
        Map<Long, List<Long>> convMap = new HashMap<>();

        for(ConsumerAgentGroup cag: object.getConsumerAgentGroups()) {
            long cagId = manager.ensureGetUID(cag);

            List<String> attrNames = object.getAttributeNames(cag);
            List<UnivariateDoubleDistribution> uncerDists = object.getUncertaintyeDistributions(cag);
            List<UnivariateDoubleDistribution> convDists = object.getConvergenceeDistributions(cag);

            for(int i = 0; i < attrNames.size(); i++) {
                String name = attrNames.get(i);
                UnivariateDoubleDistribution uncertDist = uncerDists.get(i);
                UnivariateDoubleDistribution convDist = convDists.get(i);

                long uncertDistId = manager.ensureGetUID(uncertDist);
                long convDistId = manager.ensureGetUID(convDist);

                namesMap.computeIfAbsent(cagId, _cagId -> new ArrayList<>()).add(name);
                uncertMap.computeIfAbsent(cagId, _cagId -> new ArrayList<>()).add(uncertDistId);
                convMap.computeIfAbsent(cagId, _cagId -> new ArrayList<>()).add(convDistId);
            }
        }
        data.putLongMultiStringMap(namesMap);
        data.putLongMultiLongMap(uncertMap);
        data.putLongMultiLongMap(convMap);
        
        storeHash(object, data);
    }

    @Override
    public BasicUncertaintyGroupAttributeSupplier initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = check(persistable);

        BasicUncertaintyGroupAttributeSupplier object = new BasicUncertaintyGroupAttributeSupplier();
        //object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, BasicUncertaintyGroupAttributeSupplier object, RestoreManager manager) {
        BinaryJsonData data = check(persistable);

        Map<Long, List<String>> namesMap = data.getLongMultiStringMap();
        Map<Long, List<Long>> uncertMap = data.getLongMultiLongMap();
        Map<Long, List<Long>> convMap = data.getLongMultiLongMap();

        for(long cagId: namesMap.keySet()) {
            ConsumerAgentGroup cag = manager.ensureGet(cagId);

            List<String> names = namesMap.get(cagId);
            List<Long> uncerts = uncertMap.get(cagId);
            List<Long> convs = convMap.get(cagId);

            for(int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                long uncertId = uncerts.get(i);
                long convId = convs.get(i);

                UnivariateDoubleDistribution uncert = manager.ensureGet(uncertId);
                UnivariateDoubleDistribution conv = manager.ensureGet(convId);

                object.add(cag, name, uncert, conv);
            }
        }
    }
}
