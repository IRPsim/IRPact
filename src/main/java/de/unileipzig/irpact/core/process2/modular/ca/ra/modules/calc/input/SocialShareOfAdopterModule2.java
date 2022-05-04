package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.logging.LazyString;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.SpecialUtilityCsvValueLoggingModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCACalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class SocialShareOfAdopterModule2
        extends AbstractCACalculationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SocialShareOfAdopterModule2.class);

    protected boolean logSocialNetworkCount = false;
    protected int specialId = SpecialUtilityCsvValueLoggingModule2.UNSET_ID;

    public void setSpecialId(int specialId) {
        this.specialId = specialId;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public void initializeNewInput(ConsumerAgentData2 input) throws Throwable {
    }

    @Override
    public void setup(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        if(logSocialNetworkCount) {
            logSocialNetwork(input);
        }

        double value = getShareOfAdopterInSocialNetwork(input);
        setSpecialData(specialId, value, input);
        return value;
    }

    protected void logSocialNetwork(ConsumerAgentData2 input) {
        trace(
                "[{}]@[{}] logSocialNetwork={} all={} feasible={} adopter={}",
                getName(), input.getAgentName(),
                new LazyString(() -> countAgentsInSocialNetwork(input.getEnvironment(), input.getAgent())),
                new LazyString(() -> {
                    List<ConsumerAgent> list = listAgentsInSocialNetwork(input.getEnvironment(), input.getAgent());
                    List<String> names = list.stream().map(Nameable::getName).collect(Collectors.toList());
                    return "{size=" + names.size() + "; " + names + "}";
                }),
                new LazyString(() -> {
                    List<ConsumerAgent> list = streamFeasibleAndFinancialInSocialNetwork(input.getEnvironment(), input.getAgent(), input.getProduct()).collect(Collectors.toList());
                    List<String> names = list.stream().map(Nameable::getName).collect(Collectors.toList());
                    return "{size=" + names.size() + "; " + names + "}";
                }),
                new LazyString(() -> {
                    List<ConsumerAgent> list = streamFeasibleAndFinancialInSocialNetwork(input.getEnvironment(), input.getAgent(), input.getProduct()).filter(_agent -> _agent.hasAdopted(input.getProduct())).collect(Collectors.toList());
                    List<String> names = list.stream().map(Nameable::getName).collect(Collectors.toList());
                    return "{size=" + names.size() + "; " + names + "}";
                })
        );
    }
}
