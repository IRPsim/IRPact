package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.agents.simulation.ProxySimulationAgent;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ProxySimulationAgentPR extends BinaryPRBase<ProxySimulationAgent> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProxySimulationAgentPR.class);

    public static final ProxySimulationAgentPR INSTANCE = new ProxySimulationAgentPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<ProxySimulationAgent> getType() {
        return ProxySimulationAgent.class;
    }

    @Override
    public Persistable initalizePersist(ProxySimulationAgent object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        storeHash(object, data);
        return data;
    }

    @Override
    public ProxySimulationAgent initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        ProxySimulationAgent object = new ProxySimulationAgent();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, ProxySimulationAgent object, RestoreManager manager) {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));
    }
}
