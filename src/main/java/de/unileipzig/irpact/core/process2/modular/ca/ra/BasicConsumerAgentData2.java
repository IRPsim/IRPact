package de.unileipzig.irpact.core.process2.modular.ca.ra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.map.Map3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.ProcessPlanResult2;
import de.unileipzig.irpact.core.process2.modular.ca.CAModularProcessModel2;
import de.unileipzig.irpact.core.process2.modular.ca.CAModularProcessPlan2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentData2 implements CAModularProcessPlan2, ConsumerAgentData2 {

    protected final Map<Object, Object> cache = new ConcurrentHashMap<>();
    protected final Map3<Long, Object, Object> runCache = Map3.newConcurrentHashMap();
    protected SimulationEnvironment environment;
    protected CAModularProcessModel2 model;
    protected Rnd rnd;
    protected ConsumerAgent agent;
    protected Product product;
    protected Need need;
    protected RAStage2 stage = RAStage2.PRE_INITIALIZATION;
    protected boolean underConstruction;
    protected boolean underRenovation;
    protected long currentRun = -1L;
    protected boolean autoCleanUp = true;

    public BasicConsumerAgentData2(
            SimulationEnvironment environment,
            CAModularProcessModel2 model,
            Rnd rnd,
            ConsumerAgent agent,
            Product product,
            Need need) {
        this.environment = environment;
        this.model = model;
        this.rnd = rnd;
        this.agent = agent;
        this.product = product;
        this.need = need;
    }

    @Override
    public int getChecksum() {
        throw new UnsupportedOperationException();
    }

    //=========================
    //CAModularProcessPlan2
    //=========================

    @Override
    public ProcessPlanResult2 execute2(List<PostAction2> actions) throws Throwable {
        if(autoCleanUp) {
            cleanUpRun(currentRun);
        }
        currentRun++;
        return model.execute(this, actions);
    }

    protected void cleanUpRun(long run) {
        runCache.remove(run);
    }

    @Override
    public void cleanUp() {
        cache.clear();
        runCache.clear();
        model.remove(this);
    }

    //=========================
    //ConsumerAgentData2
    //=========================

    @Override
    public boolean has(Object id) {
        return cache.containsKey(id);
    }

    @Override
    public Object get(Object id) {
        return cache.get(id);
    }

    @Override
    public Object put(Object id, Object data) {
        return cache.put(id, data);
    }

    @Override
    public long currentRun() {
        return currentRun;
    }

    @Override
    public boolean has(long run, Object key) {
        return runCache.contains(run, key);
    }

    @Override
    public Object get(long run, Object key) {
        return runCache.get(run, key);
    }

    @Override
    public Object put(long run, Object key, Object data) {
        return runCache.put(run, key, data);
    }

    @Override
    public RAStage2 getStage() {
        return stage;
    }

    @Override
    public void setStage(RAStage2 stage) {
        this.stage = stage;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public ConsumerAgent getAgent() {
        return agent;
    }

    @Override
    public Need getNeed() {
        return need;
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
    public void setUnderConstruction(boolean value) {
        underConstruction = value;
    }

    @Override
    public boolean isUnderRenovation() {
        return underRenovation;
    }

    @Override
    public void setUnderRenovation(boolean value) {
        underRenovation = value;
    }

    @Override
    public CAModularProcessModel2 getModel() {
        return model;
    }

    @Override
    public Rnd rnd() {
        return rnd;
    }
}
