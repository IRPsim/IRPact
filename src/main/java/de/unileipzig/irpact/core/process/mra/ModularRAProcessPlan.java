package de.unileipzig.irpact.core.process.mra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ModularRAProcessPlan implements ProcessPlan, AgentData {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ModularRAProcessPlan.class);

    protected ModularRAProcessModel model;
    protected ConsumerAgent agent;

    protected Need need;
    protected Product product;
    protected Rnd rnd;

    protected RAStage stage = RAStage.PRE_INITIALIZATION;
    protected boolean underRenovation = false;
    protected boolean underConstruction = false;

    public ModularRAProcessPlan(
            ModularRAProcessModel model,
            ConsumerAgent agent,
            Need need,
            Product product,
            Rnd rnd) {
        this.model = model;
        this.agent = agent;
        this.need = need;
        this.product = product;
        this.rnd = rnd;
    }

    @Override
    public int getChecksum() {
        return 0;
    }

    public ModularRAProcessModel getModel() {
        return model;
    }

    @Override
    public RAStage getStage() {
        return stage;
    }

    @Override
    public Need getNeed() {
        return need;
    }

    @Override
    public void updateStage(RAStage stage) {
        this.stage = stage;
    }

    @Override
    public Rnd getRnd() {
        return rnd;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public boolean isUnderConstruction() {
        return underConstruction;
    }

    @Override
    public boolean isUnderRenovation() {
        return underRenovation;
    }

    @Override
    public boolean isModel(ProcessModel model) {
        return getModel() == model;
    }

    @Override
    public ProcessPlanResult execute() {
        if(getStage() == RAStage.PRE_INITIALIZATION) {
            return initPlan();
        } else {
            return executePlan();
        }
    }

    protected ProcessPlanResult initPlan() {
        if(agent.hasAdopted(getProduct())) {
            updateStage(RAStage.ADOPTED);
        } else {
            updateStage(RAStage.AWARENESS);
        }
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "initial stage for '{}': {}", agent.getName(), getStage());
        return executePlan();
    }

    protected ProcessPlanResult executePlan() {
        switch (getStage()) {
            case AWARENESS:
                return getModel().getInterestComponent().evaluate(agent, this);

            case FEASIBILITY:
                return getModel().getFeasibilityComponent().evaluate(agent, this);

            case DECISION_MAKING:
                return getModel().getDecisionMakingComponent().evaluate(agent, this);

            case ADOPTED:
            case IMPEDED:
                return getModel().getActionComponent().evaluate(agent, this);

            default:
                throw new IllegalStateException("unknown phase: " + getStage());
        }
    }
}
