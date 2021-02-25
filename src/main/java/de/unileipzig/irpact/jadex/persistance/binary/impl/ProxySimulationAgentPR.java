package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.jadex.agents.simulation.ProxySimulationAgent;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonRestoreManager;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class ProxySimulationAgentPR implements Persister<ProxySimulationAgent>, Restorer<ProxySimulationAgent> {

    public static final ProxySimulationAgentPR INSTANCE = new ProxySimulationAgentPR();

    @Override
    public Class<ProxySimulationAgent> getType() {
        return ProxySimulationAgent.class;
    }

    @Override
    public Persistable persist(ProxySimulationAgent object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        return data;
    }

    @Override
    public ProxySimulationAgent initalize(Persistable persistable) {
        return new ProxySimulationAgent();
    }

    @Override
    public void setup(Persistable persistable, ProxySimulationAgent object, RestoreManager manager) {
        BinaryJsonData data = BinaryJsonRestoreManager.check(persistable);
        object.setEnvironment(manager.ensureGetSameClass(BasicJadexSimulationEnvironment.class));
        object.setName(data.getText());
    }

    @Override
    public void finalize(Persistable persistable, ProxySimulationAgent object, RestoreManager manager) {
    }
}
