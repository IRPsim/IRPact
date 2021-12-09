package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.logging.LazyString;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
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
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class LocalShareOfAdopterModule2
        extends AbstractCACalculationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(LocalShareOfAdopterModule2.class);

    protected boolean logLocalNetwork = false;
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

        if(logLocalNetwork) {
            logLocalNetwork(input, nodeFilter);
        }

        double local = getShareOfAdopterInLocalNetwork(
                input,
                nodeFilter,
                maxToStore
        );
        getAgentDataState(input).rawLocalShare = local;
        return local;
    }

    protected void logLocalNetwork(ConsumerAgentData2 input, NodeFilter nodeFilter) {
        trace(
                "[{}]@[{}] logLocalNetwork all={}, feasible={}, adopter={}",
                getName(), input.getAgentName(),
                new LazyString(() -> {
                    List<ConsumerAgent> list = streamNeighbours(input.getEnvironment(), input.getAgent(), nodeFilter).collect(Collectors.toList());
                    List<String> names = list.stream().map(Nameable::getName).collect(Collectors.toList());
                    return "{size=" + names.size() + "; " + names + "}";
                }),
                new LazyString(() -> {
                    List<ConsumerAgent> list = streamFeasibleAndFinancialNeighbours(input.getEnvironment(), input.getAgent(), input.getProduct(), nodeFilter).collect(Collectors.toList());
                    List<String> names = list.stream().map(Nameable::getName).collect(Collectors.toList());
                    return "{size=" + names.size() + "; " + names + "}";
                }),
                new LazyString(() -> {
                    List<ConsumerAgent> list = streamFeasibleAndFinancialNeighbours(input.getEnvironment(), input.getAgent(), input.getProduct(), nodeFilter).filter(_agent -> _agent.hasAdopted(input.getProduct())).collect(Collectors.toList());
                    List<String> names = list.stream().map(Nameable::getName).collect(Collectors.toList());
                    return "{size=" + names.size() + "; " + names + "}";
                })
        );
    }
}
