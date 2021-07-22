package de.unileipzig.irpact.core.process.modularra.component.special;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
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
public class DefaultHandleInterestComponent extends AbstractSingleMRAComponent implements EvaluableComponent {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultHandleInterestComponent.class);

    protected DefaultHandleInterestComponent(ComponentType type) {
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

        trace("[{}] handle interest", agent.getName());

        trace("[{}] current interest for '{}': {}", agent.getName(), data.getProductName(), getInterest(agent, data.getProduct()));

        if(isInterested(agent, data.getProduct())) {
            doSelfActionAndAllowAttention(agent);
            trace("[{}] is interested in '{}'", agent.getName(), data.getProductName());
            data.updateStage(RAStage.FEASIBILITY);
            return ProcessPlanResult.IN_PROCESS;
        }

        if(isAware(agent, data.getProduct())) {
            if(data.isUnderConstruction() || data.isUnderRenovation()) {
                if(data.isUnderConstruction()) {
                    trace("[{}] under construction, interest for '{}' set to maximum", agent.getName(), data.getProductName());
                }
                if(data.isUnderRenovation()) {
                    trace("[{}] under renovation, interest for '{}' set to maximum", agent.getName(), data.getProductName());
                }
                makeInterested(agent, data.getProduct());
                doSelfActionAndAllowAttention(agent);
                return ProcessPlanResult.IN_PROCESS;
            }
        }
        return doAction(agent, data);
    }
}
