package de.unileipzig.irpact.core.process.mra.component.base;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.mra.AgentData;
import de.unileipzig.irpact.core.process.mra.component.generic.AbstractComponent;
import de.unileipzig.irpact.core.process.mra.component.generic.Component;
import de.unileipzig.irpact.core.process.mra.component.generic.ComponentType;

import java.util.Collections;

/**
 * @author Daniel Abitz
 */
public class DoNothingComponent extends AbstractComponent implements EvaluableComponent {

    public DoNothingComponent() {
        super(ComponentType.OUTPUT);
    }

    @Override
    public Iterable<? extends Component> iterateComponents() {
        return Collections.emptyList();
    }

    @Override
    public final boolean isSupported(Component component) {
        return false;
    }

    @Override
    public final boolean add(Component component) {
        return false;
    }

    @Override
    public final boolean remove(Component component) {
        return false;
    }

    @Override
    public final boolean has(Component component) {
        return false;
    }

    @Override
    public ProcessPlanResult evaluate(Agent agent, AgentData data) {
        return ProcessPlanResult.IN_PROCESS;
    }
}
