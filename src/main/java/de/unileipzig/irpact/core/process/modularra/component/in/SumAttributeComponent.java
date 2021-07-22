package de.unileipzig.irpact.core.process.modularra.component.in;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.process.modularra.AgentData;
import de.unileipzig.irpact.core.process.modularra.component.base.AbstractAttributeComponent;

/**
 * @author Daniel Abitz
 */
public class SumAttributeComponent extends AbstractAttributeComponent {

    public SumAttributeComponent() {
    }

    protected double evaluateWithoutWeight(Agent agent, AgentData data) {
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
    public double evaluate(Agent agent, AgentData data) {
        return evaluateWithoutWeight(agent, data) * getWeight();
    }
}
