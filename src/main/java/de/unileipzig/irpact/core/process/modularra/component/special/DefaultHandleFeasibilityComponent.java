package de.unileipzig.irpact.core.process.modularra.component.special;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.modularra.AgentData;
import de.unileipzig.irpact.core.process.modularra.component.base.EvaluableComponent;
import de.unileipzig.irpact.core.process.modularra.component.generic.ComponentType;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultHandleFeasibilityComponent extends AbstractSingleMRAComponent implements EvaluableComponent {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultHandleFeasibilityComponent.class);

    protected DefaultHandleFeasibilityComponent(ComponentType type) {
        super(type);
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

        trace("[{}] handle feasibility", agent.getName());

        boolean isShare = isShareOf1Or2FamilyHouse(agent, data);
        boolean isOwner = isHouseOwner(agent, data);
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] share of 1 or 2 family house={}, house owner={}", agent.getName(), isShare, isOwner);

        if(isShare && isOwner) {
            doSelfActionAndAllowAttention(agent);
            data.updateStage(RAStage.DECISION_MAKING);
            return ProcessPlanResult.IN_PROCESS;
        }

        return doAction(agent, data);
    }
}
