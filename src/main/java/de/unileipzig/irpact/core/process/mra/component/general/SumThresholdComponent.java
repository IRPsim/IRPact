package de.unileipzig.irpact.core.process.mra.component.general;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.mra.AgentData;
import de.unileipzig.irpact.core.process.mra.component.base.AbstractThresholdComponent;
import de.unileipzig.irpact.core.process.mra.component.base.ValueComponent;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SumThresholdComponent extends AbstractThresholdComponent {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SumThresholdComponent.class);

    public SumThresholdComponent() {
    }

    protected double calculateWithoutWeight(Agent agent, AgentData data) {
        if(numberOfComponents() == 0) {
            return getIfEmpty() * getWeight();
        }

        if(numberOfComponents() == 1) {
            return components.get(0).calculate(agent, data) * getWeight();
        }

        double sum = 0.0;
        for(ValueComponent component: getComponents()) {
            sum += component.calculate(agent, data);
        }
        return sum;
    }

    public double calculate(Agent agent, AgentData data) {
        return calculateWithoutWeight(agent, data) * getWeight();
    }

    @Override
    public ProcessPlanResult evaluate(Agent agent, AgentData data) {
        double value = calculate(agent, data);
        boolean success = value < getThreshold();
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{},{}] success: {} ({} < {})", agent.getName(), getName(), success, value, getThreshold());
        if(success) {
            ConsumerAgent cAgent = (ConsumerAgent) agent;
            cAgent.adopt(data.getNeed(), data.getProduct(), agent.getEnvironment().getTimeModel().now(), AdoptionPhase.END_START);
            return ProcessPlanResult.ADOPTED;
        } else {
            return ProcessPlanResult.IMPEDED;
        }
    }
}
