package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.BasicUncertaintyGroupAttributeSupplier;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicUncertaintyGroupAttributeSupplier object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);

        for(ConsumerAgentGroup cag: object.getConsumerAgentGroups()) {
            manager.prepare(cag);

            List<UnivariateDoubleDistribution> uncerDists = object.getUncertaintyeDistributions(cag);
            List<UnivariateDoubleDistribution> convDists = object.getConvergenceeDistributions(cag);

            for(int i = 0; i < uncerDists.size(); i++) {
                UnivariateDoubleDistribution uncertDist = uncerDists.get(i);
                UnivariateDoubleDistribution convDist = convDists.get(i);

                manager.prepare(uncertDist);
                manager.prepare(convDist);
            }
        }

        return data;
    }

    @Override
    protected void doSetupPersist(BasicUncertaintyGroupAttributeSupplier object, BinaryJsonData data, PersistManager manager) {
        Map<Long, List<String>> namesMap = new LinkedHashMap<>();
        Map<Long, List<Long>> uncertMap = new LinkedHashMap<>();
        Map<Long, List<Long>> convMap = new LinkedHashMap<>();

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
    }

    //=========================
    //restore
    //=========================


    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected BasicUncertaintyGroupAttributeSupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicUncertaintyGroupAttributeSupplier object = new BasicUncertaintyGroupAttributeSupplier();
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicUncertaintyGroupAttributeSupplier object, RestoreManager manager) throws RestoreException {
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
