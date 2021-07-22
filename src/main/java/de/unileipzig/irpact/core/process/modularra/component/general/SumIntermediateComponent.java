package de.unileipzig.irpact.core.process.modularra.component.general;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.process.modularra.AgentData;
import de.unileipzig.irpact.core.process.modularra.component.base.AbstractContainerComponent;
import de.unileipzig.irpact.core.process.modularra.component.base.ValueComponent;

/**
 * @author Daniel Abitz
 */
public class SumIntermediateComponent extends AbstractContainerComponent {

    public SumIntermediateComponent() {
    }

    protected double calculateWithoutWeight(Agent agent, AgentData data) {
        if(numberOfComponents() == 0) {
            return getIfEmpty();
        }

        if(numberOfComponents() == 1) {
            return components.get(0).calculate(agent, data);
        }

        double sum = 0.0;
        for(ValueComponent component: getComponents()) {
            sum += component.calculate(agent, data);
        }
        return sum;
    }

    @Override
    public double calculate(Agent agent, AgentData data) {
        return calculateWithoutWeight(agent, data) * getWeight();
    }
}
