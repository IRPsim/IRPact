package de.unileipzig.irpact.core.process.modularra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.modularra.component.base.EvaluableComponent;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ModularRAProcessModel extends RAProcessModel {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ModularRAProcessModel.class);

    protected EvaluableComponent interestComponent;
    protected EvaluableComponent feasibilityComponent;
    protected EvaluableComponent decisionMakingComponent;
    protected EvaluableComponent actionComponent;

    public ModularRAProcessModel() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public ModularRAProcessPlan newPlan(Agent agent, Need need, Product product) {
        ConsumerAgent cAgent = cast(agent);
        Uncertainty uncertainty = getUncertainty0(cAgent);
        Rnd rnd = environment.getSimulationRandom().deriveInstance();
        AgentData data = new AgentData(this, uncertainty, need, product, rnd);
        return new ModularRAProcessPlan(cAgent, data);
    }

    public void setInterestComponent(EvaluableComponent interestComponent) {
        this.interestComponent = interestComponent;
    }

    public EvaluableComponent getInterestComponent() {
        return interestComponent;
    }

    public void setFeasibilityComponent(EvaluableComponent feasibilityComponent) {
        this.feasibilityComponent = feasibilityComponent;
    }

    public EvaluableComponent getFeasibilityComponent() {
        return feasibilityComponent;
    }

    public void setDecisionMakingComponent(EvaluableComponent decisionMakingComponent) {
        this.decisionMakingComponent = decisionMakingComponent;
    }

    public EvaluableComponent getDecisionMakingComponent() {
        return decisionMakingComponent;
    }

    public void setActionComponent(EvaluableComponent actionComponent) {
        this.actionComponent = actionComponent;
    }

    public EvaluableComponent getActionComponent() {
        return actionComponent;
    }
}
