package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ra.alg.RelativeAgreementAlgorithm;
import de.unileipzig.irpact.core.process.ra.uncert.BasicUncertaintyManager;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyCache;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyHandler;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyManager;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.initial.NewProductHandler;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.util.AttributeHelper;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class RAProcessModelBase extends NameableBase implements ProcessModel, LoggingHelper {

    protected static final long WEEK27 = 27;

    protected SimulationEnvironment environment;
    protected Rnd rnd;
    protected UncertaintyHandler uncertaintyHandler;
    protected double speedOfConvergence;
    protected RelativeAgreementAlgorithm raAlgorithm;
    protected Map<Integer, Timestamp> week27Map = new HashMap<>();
    protected boolean isYearChange = false;

    protected final List<NewProductHandler> newProductHandlers = new ArrayList<>();

    public RAProcessModelBase() {
        uncertaintyHandler = new UncertaintyHandler();
        uncertaintyHandler.setCache(new UncertaintyCache());
        uncertaintyHandler.setManager(new BasicUncertaintyManager());
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public final IRPSection getDefaultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    public void addNewProductHandler(NewProductHandler initialAdoptionHandler) {
        newProductHandlers.add(initialAdoptionHandler);
    }

    public List<NewProductHandler> getNewProductHandlers() {
        return newProductHandlers;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
        uncertaintyHandler.setEnvironment(environment);
    }
    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }

    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    public UncertaintyManager getUncertaintyManager() {
        return uncertaintyHandler.getManager();
    }

    public void setUncertaintyManager(UncertaintyManager uncertaintyManager) {
        uncertaintyHandler.setManager(uncertaintyManager);
    }

    public UncertaintyCache getUncertaintyCache() {
        return uncertaintyHandler.getCache();
    }

    public void setUncertaintyCache(UncertaintyCache uncertaintyCache) {
        uncertaintyHandler.setCache(uncertaintyCache);
    }

    public void setRelativeAgreementAlgorithm(RelativeAgreementAlgorithm raAlgorithm) {
        this.raAlgorithm = raAlgorithm;
    }

    public RelativeAgreementAlgorithm getRelativeAgreementAlgorithm() {
        return raAlgorithm;
    }

    private void setYearChange(boolean isYearChange) {
        this.isYearChange = isYearChange;
    }

    public boolean isYearChange() {
        return isYearChange;
    }

    public boolean isBeforeWeek27(Timestamp ts) {
        Timestamp currentYearWeek27 = getCurrentWeek27(ts);
        return ts.isBefore(currentYearWeek27);
    }

    private Timestamp getCurrentWeek27(Timestamp ts) {
        Timestamp w27 = week27Map.get(ts.getYear());
        if(w27 == null) {
            return syncGetCurrentWeek27(ts);
        } else {
            return w27;
        }
    }

    private synchronized Timestamp syncGetCurrentWeek27(Timestamp ts) {
        Timestamp w27 = week27Map.get(ts.getYear());
        if(w27 == null) {
            w27 = environment.getTimeModel().atWeek(ts.getYear(), WEEK27);
            week27Map.put(ts.getYear(), w27);
        }
        return w27;
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        initUncertainty();
    }

    protected void initUncertainty() {
        uncertaintyHandler.initalize();
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        setupTasks();
    }

    protected void setupTasks() {
        int firstYear = environment.getTimeModel().getFirstSimulationYear();
        int lastYear = environment.getTimeModel().getLastSimulationYear();

        trace("create syncronisation tasks for process model '{}' ", getName());

        SyncTask firstAnnualTask = createStartOfYearTask(getName() + "_FirstAnnual");
        trace("create first annual task '{}'", firstAnnualTask.getName());
        environment.getLifeCycleControl().registerSyncTaskAsFirstAnnualAction(firstAnnualTask);

        SyncTask lastAnnualTask = createEndOfYearTask(getName() + "_LastAnnual");
        trace("create last annual task '{}'", lastAnnualTask.getName());
        environment.getLifeCycleControl().registerSyncTaskAsLastAnnualAction(lastAnnualTask);

        for(int y = firstYear; y <= lastYear; y++) {
            Timestamp tsWeek27 = environment.getTimeModel().atWeek(y, WEEK27);
            SyncTask taskWeek27 = createWeek27Task(getName() + "_MidYear_" + y);
            trace("created mid year task (27th week) '{}'", taskWeek27.getName());
            environment.getLifeCycleControl().registerSyncTaskAsFirstAction(tsWeek27, taskWeek27);
        }
    }

    protected SyncTask createStartOfYearTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createStartOfYearTask'");

                for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                    for(ConsumerAgent ca: cag.getAgents()) {
                        for(ProcessPlan plan: ca.getPlans().values()) {
                            if(plan.isModel(RAProcessModelBase.this)) {
                                RAProcessPlanBase raPlan = (RAProcessPlanBase) plan;
                                raPlan.runAdjustmentAtStartOfYear();
                            }
                        }
                    }
                }
            }
        };
    }

    protected SyncTask createEndOfYearTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createEndOfYearTask'");

                setYearChange(true);
                for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                    for(ConsumerAgent ca: cag.getAgents()) {
                        for(ProcessPlan plan: ca.getPlans().values()) {
                            if(plan.isModel(RAProcessModelBase.this)) {
                                RAProcessPlanBase raPlan = (RAProcessPlanBase) plan;
                                raPlan.runEvaluationAtEndOfYear();
                            }
                        }
                    }
                }
                setYearChange(false);
            }
        };
    }

    protected SyncTask createWeek27Task(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createWeek27Task'");

                for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                    for(ConsumerAgent ca: cag.getAgents()) {
                        for(ProcessPlan plan: ca.getPlans().values()) {
                            if(plan.isModel(RAProcessModelBase.this)) {
                                RAProcessPlanBase raPlan = (RAProcessPlanBase) plan;
                                raPlan.runUpdateAtMidOfYear();
                            }
                        }
                    }
                }
            }
        };
    }

    //==================================================
    //attributes
    //==================================================

    protected AttributeHelper getAttributeHelper() {
        if(environment == null) {
            throw new NullPointerException("missing environment");
        }
        AttributeHelper helper = environment.getAttributeHelper();
        if(helper == null) {
            throw new NullPointerException("missing helper");
        }
        return helper;
    }

    public static double getFinancialPurchasePower(
            ConsumerAgent agent,
            AttributeHelper helper) {
//        double pp = getPurchasePower(agent);
//        double ns = getNoveltySeeking(agent);
//        return pp;
        return helper.findDoubleValue(agent, RAConstants.PURCHASE_POWER_EUR);
    }

    //=========================
    //find
    //=========================

    public long getId(ConsumerAgent agent) {
        SpatialInformation info = agent.getSpatialInformation();
        if(info.hasId()) {
            return info.getId();
        } else {
            return getAttributeHelper().findLongValue(agent, RAConstants.ID);
        }
    }

    public double getFinancialPurchasePower(ConsumerAgent agent) {
        return getFinancialPurchasePower(agent, getAttributeHelper());
    }

    public double getPurchasePower(ConsumerAgent agent) {
        return getAttributeHelper().findDoubleValue(agent, RAConstants.PURCHASE_POWER_EUR);
    }

    public boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        return getAttributeHelper().findBooleanValue(agent, RAConstants.SHARE_1_2_HOUSE);
    }

    public void setShareOf1Or2FamilyHouse(ConsumerAgent agent, boolean value) {
        getAttributeHelper().findAndSetBooleanValue(agent, RAConstants.SHARE_1_2_HOUSE, value);
    }

    public boolean isHouseOwner(ConsumerAgent agent) {
        return getAttributeHelper().findBooleanValue(agent, RAConstants.HOUSE_OWNER);
    }

    public void setHouseOwner(ConsumerAgent agent, boolean value) {
        getAttributeHelper().findAndSetBooleanValue(agent, RAConstants.HOUSE_OWNER, value);
    }

    //=========================
    //value
    //=========================

    public double getCommunicationFrequencySN(ConsumerAgent agent) {
        return getAttributeHelper().getDoubleValue(agent, RAConstants.COMMUNICATION_FREQUENCY_SN);
    }

    public double getRewiringRate(ConsumerAgent agent) {
        return getAttributeHelper().getDoubleValue(agent, RAConstants.REWIRING_RATE);
    }

    public double getNoveltySeeking(ConsumerAgent agent) {
        return getAttributeHelper().getDoubleValue(agent, RAConstants.NOVELTY_SEEKING);
    }

    public double getDependentJudgmentMaking(ConsumerAgent agent) {
        return getAttributeHelper().getDoubleValue(agent, RAConstants.DEPENDENT_JUDGMENT_MAKING);
    }

    public double getEnvironmentalConcern(ConsumerAgent agent) {
        return getAttributeHelper().getDoubleValue(agent, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    public double getConstructionRate(ConsumerAgent agent) {
        return getAttributeHelper().getDoubleValue(agent, RAConstants.CONSTRUCTION_RATE);
    }

    public double getRenovationRate(ConsumerAgent agent) {
        return getAttributeHelper().getDoubleValue(agent, RAConstants.RENOVATION_RATE);
    }

    //=========================
    //product
    //=========================

    public double getFinancialThreshold(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDoubleValue(agent, product, RAConstants.FINANCIAL_THRESHOLD);
    }

    public double getAdoptionThreshold(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDoubleValue(agent, product, RAConstants.ADOPTION_THRESHOLD);
    }

    public double getInitialProductAwareness(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDoubleValue(agent, product, RAConstants.INITIAL_PRODUCT_AWARENESS);
    }

    public double getInitialProductInterest(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDoubleValue(agent, product, RAConstants.INITIAL_PRODUCT_INTEREST);
    }

    public double getInitialAdopter(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDoubleValue(agent, product, RAConstants.INITIAL_ADOPTER);
    }
}
