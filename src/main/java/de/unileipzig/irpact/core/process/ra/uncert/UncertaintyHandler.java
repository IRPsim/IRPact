package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class UncertaintyHandler extends NameableBase {

    protected SimulationEnvironment environment;
    protected UncertaintyManager uncertaintyManager;
    protected UncertaintyCache uncertaintyCache;
    protected boolean initalized = false;

    public UncertaintyHandler() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    public void setManager(UncertaintyManager uncertaintyManager) {
        this.uncertaintyManager = uncertaintyManager;
    }

    public UncertaintyManager getManager() {
        return uncertaintyManager;
    }

    public void setCache(UncertaintyCache uncertaintyCache) {
        this.uncertaintyCache = uncertaintyCache;
    }

    public UncertaintyCache getCache() {
        return uncertaintyCache;
    }

    public void reset() {
        initalized = false;
    }

    public void initalize() {
        if(initalized) {
            return;
        }

        initalized = true;
        uncertaintyManager.initalize();
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca : cag.getAgents()) {
                Uncertainty uncertainty = uncertaintyManager.createFor(ca);
                uncertaintyCache.registerUncertainty(ca, uncertainty, false, environment.isRestored());
            }
        }
    }
}
