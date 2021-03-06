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
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.handler.NewProductHandler;
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
    protected double speedOfConvergence;
    protected Map<Integer, Timestamp> week27Map = new HashMap<>();
    protected boolean isYearChange = false;
    protected boolean skipAwareness = false;
    protected boolean skipFeasibility = false;
    protected boolean forceEvaluate = false;
    protected double adoptionCertaintyBase = 1.0;
    protected double adoptionCertaintyFactor = 1.0;

    protected final List<NewProductHandler> newProductHandlers = new ArrayList<>();

    public RAProcessModelBase() {
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public final IRPSection getDefaultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    protected int getYearDelta() {
        return environment.getTimeModel().getCurrentYear() - environment.getTimeModel().getFirstSimulationYear();
    }

    public boolean hasAdoptionCertainty() {
        return !(adoptionCertaintyBase == 1.0 && adoptionCertaintyFactor == 1.0);
    }

    protected double getAdoptionCertainty() {
        return adoptionCertaintyBase * Math.pow(adoptionCertaintyFactor, getYearDelta());
    }

    public void addNewProductHandler(NewProductHandler initialAdoptionHandler) {
        newProductHandlers.add(initialAdoptionHandler);
    }

    public List<NewProductHandler> getNewProductHandlers() {
        return newProductHandlers;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
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

    public void setSkipAwareness(boolean skipAwareness) {
        this.skipAwareness = skipAwareness;
    }

    public boolean isSkipAwareness() {
        return skipAwareness;
    }

    public void setSkipFeasibility(boolean skipFeasibility) {
        this.skipFeasibility = skipFeasibility;
    }

    public boolean isSkipFeasibility() {
        return skipFeasibility;
    }

    public void setForceEvaluate(boolean forceEvaluate) {
        this.forceEvaluate = forceEvaluate;
    }

    public boolean isForceEvaluate() {
        return forceEvaluate;
    }

    public boolean isNotForceEvaluate() {
        return !isForceEvaluate();
    }

    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }

    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    public void setAdoptionCertaintyBase(double adoptionCertaintyBase) {
        this.adoptionCertaintyBase = adoptionCertaintyBase;
    }

    public double getAdoptionCertaintyBase() {
        return adoptionCertaintyBase;
    }

    public void setAdoptionCertaintyFactor(double adoptionCertaintyFactor) {
        this.adoptionCertaintyFactor = adoptionCertaintyFactor;
    }

    public double getAdoptionCertaintyFactor() {
        return adoptionCertaintyFactor;
    }
//
//    public UncertaintyManager getUncertaintyManager() {
//        return uncertaintyHandler.getManager();
//    }
//
//    public void setUncertaintyManager(UncertaintyManager uncertaintyManager) {
//        uncertaintyHandler.setManager(uncertaintyManager);
//    }
//
//    public UncertaintyCache getUncertaintyCache() {
//        return uncertaintyHandler.getCache();
//    }
//
//    public void setUncertaintyCache(UncertaintyCache uncertaintyCache) {
//        uncertaintyHandler.setCache(uncertaintyCache);
//    }
//
//    public void setRelativeAgreementAlgorithm(RelativeAgreementAlgorithm raAlgorithm) {
//        this.raAlgorithm = raAlgorithm;
//    }
//
//    public RelativeAgreementAlgorithm getRelativeAgreementAlgorithm() {
//        return raAlgorithm;
//    }

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
//        uncertaintyHandler.initalize();
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
                        for(ProcessPlan plan: ca.getRunningPlans()) {
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
                        for(ProcessPlan plan: ca.getRunningPlans()) {
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
                        for(ProcessPlan plan: ca.getRunningPlans()) {
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
        return helper.getDouble(agent, RAConstants.PURCHASE_POWER_EUR, true);
    }

    //=========================
    //find
    //=========================

    public long getId(ConsumerAgent agent) {
        SpatialInformation info = agent.getSpatialInformation();
        if(info.hasId()) {
            return info.getId();
        } else {
            return getAttributeHelper().getLong(agent, RAConstants.ID, true);
        }
    }

    public double getFinancialPurchasePower(ConsumerAgent agent) {
        return getFinancialPurchasePower(agent, getAttributeHelper());
    }

    public double getPurchasePower(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.PURCHASE_POWER_EUR, true);
    }

    public boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        return getAttributeHelper().getBoolean(agent, RAConstants.SHARE_1_2_HOUSE, true);
    }

    public void setShareOf1Or2FamilyHouse(ConsumerAgent agent, boolean value) {
        getAttributeHelper().setBoolean(agent, RAConstants.SHARE_1_2_HOUSE, value, true);
    }

    public boolean isHouseOwner(ConsumerAgent agent) {
        return getAttributeHelper().getBoolean(agent, RAConstants.HOUSE_OWNER, true);
    }

    public void setHouseOwner(ConsumerAgent agent, boolean value) {
        getAttributeHelper().setBoolean(agent, RAConstants.HOUSE_OWNER, value, true);
    }

    //=========================
    //value
    //=========================

    public double getCommunicationFrequencySN(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.COMMUNICATION_FREQUENCY_SN, true);
    }

    public double getRewiringRate(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.REWIRING_RATE, true);
    }

    public double getNoveltySeeking(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.NOVELTY_SEEKING, true);
    }

    public double getDependentJudgmentMaking(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.DEPENDENT_JUDGMENT_MAKING, true);
    }

    public double getEnvironmentalConcern(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.ENVIRONMENTAL_CONCERN, true);
    }

    public double getConstructionRate(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.CONSTRUCTION_RATE, true);
    }

    public double getRenovationRate(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.RENOVATION_RATE, true);
    }

    //=========================
    //product
    //=========================

    public double getFinancialThreshold(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.FINANCIAL_THRESHOLD, true);
    }

    public double getAdoptionThreshold(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.ADOPTION_THRESHOLD, true);
    }

    public double getInitialProductAwareness(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.INITIAL_PRODUCT_AWARENESS, true);
    }

    public double getInitialProductInterest(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.INITIAL_PRODUCT_INTEREST, true);
    }

    public double getInitialAdopter(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.INITIAL_ADOPTER, true);
    }
}
