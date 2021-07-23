package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class RAProcessPlanBase implements ProcessPlan, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessPlanBase.class);

    protected SimulationEnvironment environment;
    protected RAProcessModelBase model;
    protected Need need;
    protected Product product;
    protected ConsumerAgent agent;
    protected Rnd rnd;
    protected RAStage stage = RAStage.PRE_INITIALIZATION;
    protected boolean underRenovation = false;
    protected boolean underConstruction = false;

    public RAProcessModelBase getModel() {
        return model;
    }
    public void setModel(RAProcessModelBase model) {
        this.model = model;
    }

    @Override
    public boolean isModel(ProcessModel model) {
        return getModel() == model;
    }

    public ConsumerAgent getAgent() {
        return agent;
    }
    public void setAgent(ConsumerAgent agent) {
        this.agent = agent;
    }

    public RAStage getStage() {
        return stage;
    }
    public void setStage(RAStage stage) {
        logStageUpdate(this.stage, stage);
        this.stage = stage;
    }
    protected void logStageUpdate(RAStage current, RAStage nextStage)  {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] stage update: {} -> {}", agent.getName(), current, nextStage);
    }

    public Need getNeed() {
        return need;
    }
    public void setNeed(Need need) {
        this.need = need;
    }

    public Rnd getRnd() {
        return rnd;
    }
    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
        rnd.enableSync();
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isUnderConstruction() {
        return underConstruction;
    }
    public void setUnderConstruction(boolean underConstruction) {
        this.underConstruction = underConstruction;
    }

    public boolean isUnderRenovation() {
        return underRenovation;
    }
    public void setUnderRenovation(boolean underRenovation) {
        this.underRenovation = underRenovation;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    public Uncertainty getUncertainty() {
        return getModel().getUncertaintyCache().getUncertainty(getAgent());
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }

    public void runAdjustmentAtStartOfYear() {
        if(stage == RAStage.IMPEDED) {
            trace("reset process stage '{}' to '{}' for agent '{}'", RAStage.IMPEDED, RAStage.DECISION_MAKING, agent.getName());
            stage = RAStage.DECISION_MAKING;
        }
    }

    public void runEvaluationAtEndOfYear() {
        if(stage == RAStage.IMPEDED) {
            trace("reset process stage '{}' to '{}' for agent '{}'", RAStage.IMPEDED, RAStage.DECISION_MAKING, agent.getName());
            stage = RAStage.DECISION_MAKING;
            doRunEvaluationAtEndOfYear();
        }
    }

    protected abstract void doRunEvaluationAtEndOfYear();

    public void runUpdateAtMidOfYear() {
        double renovationRate = getRenovationRate(agent);
        double renovationDraw = rnd.nextDouble();
        boolean doRenovation = renovationDraw < renovationRate;
        trace("agent '{}' now under renovation? {} ({} < {})", agent.getName(), doRenovation, renovationDraw, renovationRate);
        setUnderRenovation(doRenovation);

        double constructionRate = getConstructionRate(agent);
        double constructionDraw = rnd.nextDouble();
        boolean doConstruction = constructionDraw < constructionRate;
        trace("agent '{}' now under construction? {} ({} < {})", agent.getName(), doConstruction, constructionDraw, constructionRate);
        setUnderConstruction(doConstruction);

        if(doConstruction) {
            applyUnderConstruction(agent);
        }
    }

    protected void applyUnderConstruction(ConsumerAgent agent) {
        boolean isShare = isShareOf1Or2FamilyHouse(agent);
        boolean isOwner = isHouseOwner(agent);
        setShareOf1Or2FamilyHouse(agent, true);
        setHouseOwner(agent, true);

        if(!isShare) {
            trace("agent '{}' not share of 1 or 2 family house", agent.getName());
        }
        if(!isOwner) {
            trace("agent '{}' house owner", agent.getName());
        }
    }

    protected double getRenovationRate(ConsumerAgent agent) {
        return getModel().getRenovationRate(agent);
    }

    protected double getConstructionRate(ConsumerAgent agent) {
        return getModel().getConstructionRate(agent);
    }

    protected boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        return getModel().isShareOf1Or2FamilyHouse(agent);
    }

    @SuppressWarnings("SameParameterValue")
    protected void setShareOf1Or2FamilyHouse(ConsumerAgent agent, boolean value) {
        getModel().setShareOf1Or2FamilyHouse(agent, value);
    }

    protected boolean isHouseOwner(ConsumerAgent agent) {
        return getModel().isHouseOwner(agent);
    }

    @SuppressWarnings("SameParameterValue")
    protected void setHouseOwner(ConsumerAgent agent, boolean value) {
        getModel().setHouseOwner(agent, value);
    }
}
