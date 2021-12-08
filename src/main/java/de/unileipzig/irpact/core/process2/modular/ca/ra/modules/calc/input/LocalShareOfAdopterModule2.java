package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.network.filter.NodeFilterScheme;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCACalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class LocalShareOfAdopterModule2
        extends AbstractCACalculationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(LocalShareOfAdopterModule2.class);

    protected NodeFilterScheme nodeFilterScheme;
    protected int maxToStore = 0;

    public void setNodeFilterScheme(NodeFilterScheme nodeFilterScheme) {
        this.nodeFilterScheme = nodeFilterScheme;
    }

    public NodeFilterScheme getNodeFilterScheme() {
        return nodeFilterScheme;
    }

    public void setMaxToStore(int maxToStore) {
        this.maxToStore = maxToStore;
    }

    public int getMaxToStore() {
        return maxToStore;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
        if(nodeFilterScheme == null) {
            throw new NullPointerException("missing ProcessPlanNodeFilterScheme");
        }
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        if(alreadyInitalized()) {
            return;
        }

        traceModuleInitalization();
        trace("use node filter: {}", nodeFilterScheme);
        setInitalized();
    }

    @Override
    public void initializeNewInput(ConsumerAgentData2 input) throws Throwable {
    }

    @Override
    public void setup(SimulationEnvironment environment) throws Throwable {
        if(alreadySetupCalled()) {
            return;
        }

        traceModuleSetup();
        setSetupCalled();
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        NodeFilter nodeFilter = getNodeFilter(input, nodeFilterScheme);
        double local = getShareOfAdopterInLocalNetwork(
                input,
                nodeFilter,
                maxToStore
        );
        getAgentDataState(input).rawLocalShare = local;
        return local;
    }
}
