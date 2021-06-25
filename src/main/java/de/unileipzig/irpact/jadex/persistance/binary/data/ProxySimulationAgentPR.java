package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.agents.simulation.ProxySimulationAgent;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
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

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(ProxySimulationAgent object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected ProxySimulationAgent doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        ProxySimulationAgent object = new ProxySimulationAgent();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, ProxySimulationAgent object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(SimulationEnvironment.class));
    }
}
