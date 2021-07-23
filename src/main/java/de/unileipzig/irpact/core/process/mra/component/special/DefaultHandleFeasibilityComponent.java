package de.unileipzig.irpact.core.process.mra.component.special;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.mra.AgentData;
import de.unileipzig.irpact.core.process.mra.component.base.EvaluableComponent;
import de.unileipzig.irpact.core.process.mra.component.generic.ComponentType;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultHandleFeasibilityComponent extends AbstractSingleMRAComponent implements EvaluableComponent {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultHandleFeasibilityComponent.class);

    public DefaultHandleFeasibilityComponent() {
        super(ComponentType.OUTPUT);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                Checksums.SMART.getNamedChecksum(getModel()),
                getAdopterPoints(),
                getInterestedPoints(),
                getAwarePoints(),
                getUnknownPoints()
        );
    }

    @Override
    public ProcessPlanResult evaluate(Agent a, AgentData data) {
        ConsumerAgent agent = (ConsumerAgent) a;

        trace("[{}] handle feasibility", agent.getName());

        boolean isShare = isShareOf1Or2FamilyHouse(agent);
        boolean isOwner = isHouseOwner(agent);
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] share of 1 or 2 family house={}, house owner={}", agent.getName(), isShare, isOwner);

        if(isShare && isOwner) {
            doSelfActionAndAllowAttention(agent);
            data.setStage(RAStage.DECISION_MAKING);
            return ProcessPlanResult.IN_PROCESS;
        }

        return doAction(agent, data.getRnd(), data.getProduct());
    }
}
