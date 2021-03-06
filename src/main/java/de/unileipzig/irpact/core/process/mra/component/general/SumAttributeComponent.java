package de.unileipzig.irpact.core.process.mra.component.general;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.process.mra.AgentData;
import de.unileipzig.irpact.core.process.mra.component.base.AbstractAttributeComponent;

/**
 * @author Daniel Abitz
 */
public class SumAttributeComponent extends AbstractAttributeComponent {

    public SumAttributeComponent() {
    }

    protected double calculateWithoutWeight(Agent agent, AgentData data) {
        if(numberOfAttributeNames() == 0) {
            return getIfEmpty();
        }

        if(numberOfAttributeNames() == 1) {
            return findDoubleValue(agent, attributeNames.get(0));
        }

        double sum = 0.0;
        for(String attributeName: getAttributeNames()) {
            sum += findDoubleValue(agent, attributeName);
        }
        return sum;
    }

    @Override
    public double calculate(Agent agent, AgentData data) {
        return calculateWithoutWeight(agent, data) * getWeight();
    }
}
