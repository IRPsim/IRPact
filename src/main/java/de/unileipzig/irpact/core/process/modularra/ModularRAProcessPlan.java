package de.unileipzig.irpact.core.process.modularra;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ModularRAProcessPlan implements ProcessPlan {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ModularRAProcessPlan.class);

    protected ConsumerAgent agent;
    protected AgentData data;

    public ModularRAProcessPlan(ConsumerAgent agent, AgentData data) {
        this.agent = agent;
        this.data = data;
    }

    @Override
    public int getChecksum() {
        return 0;
    }

    @Override
    public boolean isModel(ProcessModel model) {
        return data.getModel() == model;
    }

    @Override
    public ProcessPlanResult execute() {
        if(data.getStage() == RAStage.PRE_INITIALIZATION) {
            return initPlan();
        } else {
            return executePlan();
        }
    }

    protected ProcessPlanResult initPlan() {
        if(agent.hasAdopted(data.getProduct())) {
            data.updateStage(RAStage.ADOPTED);
        } else {
            data.updateStage(RAStage.AWARENESS);
        }
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "initial stage for '{}': {}", agent.getName(), data.getStage());
        return executePlan();
    }

    protected ProcessPlanResult executePlan() {
        switch (data.getStage()) {
            case AWARENESS:
                return data.getModel().getInterestComponent().evaluate(agent, data);

            case FEASIBILITY:
                return data.getModel().getFeasibilityComponent().evaluate(agent, data);

            case DECISION_MAKING:
                return data.getModel().getDecisionMakingComponent().evaluate(agent, data);

            case ADOPTED:
            case IMPEDED:
                return data.getModel().getActionComponent().evaluate(agent, data);

            default:
                throw new IllegalStateException("unknown phase: " + data.getStage());
        }
    }
}
