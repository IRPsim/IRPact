package de.unileipzig.irpact.core.process.modularra.component.inter;

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

    protected double evaluateWithoutWeight(Agent agent, AgentData data) {
        if(numberOfComponents() == 0) {
            return getIfEmpty();
        }

        if(numberOfComponents() == 1) {
            return components.get(0).evaluate(agent, data);
        }

        double sum = 0.0;
        for(ValueComponent component: getComponents()) {
            sum += component.evaluate(agent, data);
        }
        return sum;
    }

    @Override
    public double evaluate(Agent agent, AgentData data) {
        return evaluateWithoutWeight(agent, data) * getWeight();
    }
}
