package de.unileipzig.irpact.core.process.modularra.component.general;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.modularra.AgentData;
import de.unileipzig.irpact.core.process.modularra.component.base.AbstractThresholdComponent;
import de.unileipzig.irpact.core.process.modularra.component.base.ValueComponent;

/**
 * @author Daniel Abitz
 */
public class SumThresholdComponent extends AbstractThresholdComponent {

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
        if(value < getThreshold()) {
            return ProcessPlanResult.ADOPTED;
        } else {
            return ProcessPlanResult.IMPEDED;
        }
    }
}
