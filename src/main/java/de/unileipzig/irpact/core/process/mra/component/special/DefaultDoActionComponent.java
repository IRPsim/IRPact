package de.unileipzig.irpact.core.process.mra.component.special;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.mra.AgentData;
import de.unileipzig.irpact.core.process.mra.component.base.EvaluableComponent;
import de.unileipzig.irpact.core.process.mra.component.generic.ComponentType;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultDoActionComponent extends AbstractSingleMRAComponent implements EvaluableComponent {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultDoActionComponent.class);

    public DefaultDoActionComponent() {
        super(ComponentType.OUTPUT);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Dev.throwException();
    }

    @Override
    public ProcessPlanResult evaluate(Agent a, AgentData data) {
        ConsumerAgent agent = (ConsumerAgent) a;
        return doAction(agent, data.getRnd(), data.getProduct());
    }
}
