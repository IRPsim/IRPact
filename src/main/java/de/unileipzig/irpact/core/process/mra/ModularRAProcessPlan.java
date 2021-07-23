package de.unileipzig.irpact.core.process.mra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.ra.RAProcessPlanBase;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ModularRAProcessPlan extends RAProcessPlanBase implements ProcessPlan, AgentData {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ModularRAProcessPlan.class);

    protected final Map<String, Object> DATA = new HashMap<>();

    public ModularRAProcessPlan(
            ModularRAProcessModel model,
            ConsumerAgent agent,
            Need need,
            Product product,
            Rnd rnd) {
        setModel(model);
        setAgent(agent);
        setNeed(need);
        setProduct(product);
        setRnd(rnd);
    }

    @Override
    public int getChecksum() {
        return Dev.throwException();
    }

    @Override
    public ModularRAProcessModel getModel() {
        return (ModularRAProcessModel) model;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected void doRunEvaluationAtEndOfYear() {
        getModel().getDecisionMakingComponent().evaluate(agent, this);
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
            setStage(RAStage.ADOPTED);
        } else {
            setStage(RAStage.AWARENESS);
        }
        trace("initial stage for '{}': {}", agent.getName(), getStage());
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

    @Override
    public void store(String key, Object value) {
        DATA.put(key, value);
    }

    @Override
    public Object get(String key) {
        return DATA.get(key);
    }

    @Override
    public boolean has(String key) {
        return DATA.containsKey(key);
    }
}
