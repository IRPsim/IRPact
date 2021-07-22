package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.LoggableChecksum;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.ra.alg.RelativeAgreementAlgorithm;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.process.ra.uncert.BasicUncertaintyManager;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyManager;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.util.AttributeHelper;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class RAProcessModel extends NameableBase implements ProcessModel, LoggableChecksum, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessModel.class);

    protected static final long WEEK27 = 27;

    protected static boolean globalRAProcessInitCalled = false;

    protected final AttributeHelper ATTR_HELPER = new AttributeHelper();
    protected SimulationEnvironment environment;

    protected RelativeAgreementAlgorithm raAlgorithm;
    protected double speedOfConvergence;
    protected UncertaintyManager uncertaintyManager = new BasicUncertaintyManager();
    protected Map<ConsumerAgent, Uncertainty> uncertaintyCache = new HashMap<>();

    protected ProcessPlanNodeFilterScheme nodeFilterScheme;

    protected NPVData npvData;
    protected NPVCalculator npvCalculator;
    protected RAModelData modelData;
    protected Rnd rnd;

    protected Map<Integer, Timestamp> week27Map = new HashMap<>();
    protected boolean isYearChange = false;

    public RAProcessModel() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public final IRPSection getDefaultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                modelData,
                rnd,
                uncertaintyManager
        );
    }

    private void logHash(String msg, int storedHash) {
        warn(
                "hash @ '{}': stored={}",
                msg,
                Integer.toHexString(storedHash)
        );
    }

    @Override
    public void logChecksums() {
        logHash("name", ChecksumComparable.getChecksum(getName()));
        logHash("model data", ChecksumComparable.getChecksum(modelData));
        logHash("rnd", ChecksumComparable.getChecksum(rnd));
        logHash("uncertainty manager", ChecksumComparable.getChecksum(uncertaintyManager));
    }

    public AttributeHelper getAttributeHelper() {
        return ATTR_HELPER;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
        ATTR_HELPER.setEnvironment(environment);
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    public void setModelData(RAModelData modelData) {
        this.modelData = modelData;
        modelData.setAttributeHelper(ATTR_HELPER);
    }

    public RAModelData getModelData() {
        return modelData;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    public NPVData getNpvData() {
        return npvData;
    }

    public void setNpvData(NPVData npvData) {
        this.npvData = npvData;
    }

    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }

    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    public void setRelativeAgreementAlgorithm(RelativeAgreementAlgorithm raAlgorithm) {
        this.raAlgorithm = raAlgorithm;
    }

    public RelativeAgreementAlgorithm getRelativeAgreementAlgorithm() {
        return raAlgorithm;
    }

    public UncertaintyManager getUncertaintyManager() {
        return uncertaintyManager;
    }

    public void setUncertaintyManager(UncertaintyManager uncertaintyManager) {
        this.uncertaintyManager = uncertaintyManager;
    }

    public void setNodeFilterScheme(ProcessPlanNodeFilterScheme nodeFilterScheme) {
        this.nodeFilterScheme = nodeFilterScheme;
    }

    public ProcessPlanNodeFilterScheme getNodeFilterScheme() {
        return nodeFilterScheme;
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        if(npvData != null) {
            initNPVMatrixWithFile();
        }
    }

    private void initNPVMatrixWithFile() {
        npvCalculator = new NPVCalculator();
        npvCalculator.setData(npvData);

        int firstYear = environment.getTimeModel()
                .getFirstSimulationYear();
        int lastYear = environment.getTimeModel()
                .getLastSimulationYear();

        trace("calculating npv matrix from '{}' to '{}'", firstYear, lastYear);
        for(int y = firstYear; y <= lastYear; y++) {
            trace("calculate year '{}'", y);
            NPVMatrix matrix = new NPVMatrix();
            matrix.calculate(npvCalculator, y);
            modelData.put(y, matrix);
        }
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        if(npvData == null) {
            trace("no npv data found, test existence of attribute '{}'", RAConstants.NET_PRESENT_VALUE);
            validateNPVAttributeExistenceInCags();
        }

        checkGroupAttributExistance();
    }

    private void validateNPVAttributeExistenceInCags() throws ValidationException {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            if(!cag.hasGroupAttribute(RAConstants.NET_PRESENT_VALUE)) {
                throw ExceptionUtil.create(ValidationException::new, "cag '{}' has no '{}'", cag.getName(), RAConstants.NET_PRESENT_VALUE);
            }
            ConsumerAgentGroupAttribute cagAttr = cag.getGroupAttribute(RAConstants.NET_PRESENT_VALUE);
            if(!cagAttr.isAnnualGroupAttribute()) {
                throw ExceptionUtil.create(ValidationException::new, "cag '{}' has no annual data '{}'", cag.getName(), RAConstants.NET_PRESENT_VALUE);
            }
            ConsumerAgentAnnualGroupAttribute aCagAttr = cagAttr.asAnnualGroupAttribute();

            int firstYear = environment.getTimeModel()
                    .getFirstSimulationYear();
            int lastYear = environment.getTimeModel()
                    .getLastSimulationYear();

            for(int y = firstYear; y <= lastYear; y++) {
                if(!aCagAttr.hasYear(y)) {
                    throw ExceptionUtil.create(ValidationException::new, "cag '{}' has no annual data '{}' for year '{}'", cag.getName(), RAConstants.NET_PRESENT_VALUE, y);
                }
            }
        }
        trace("annual attribute '{}' for all cags and years found", RAConstants.NET_PRESENT_VALUE);
    }

    private void checkGroupAttributExistance() throws ValidationException {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            checkHasAttribute(cag, RAConstants.NOVELTY_SEEKING);
            checkHasAttribute(cag, RAConstants.DEPENDENT_JUDGMENT_MAKING);
            checkHasAttribute(cag, RAConstants.ENVIRONMENTAL_CONCERN);
            checkHasAttribute(cag, RAConstants.CONSTRUCTION_RATE);
            checkHasAttribute(cag, RAConstants.RENOVATION_RATE);
            checkHasAttribute(cag, RAConstants.REWIRING_RATE);
            checkHasAttribute(cag, RAConstants.COMMUNICATION_FREQUENCY_SN);
        }
    }

    private void checkHasAttribute(ConsumerAgentGroup cag, String name) throws ValidationException {
        if(!cag.hasGroupAttribute(name)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent group '{}' has no group attribute '{}'", cag.getName(), name);
        }
    }

    @Override
    public void postAgentCreation() {
        runGlobalInitalization();
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        checkAttributes();
        initUncertainty();
    }

    private void checkAttributes() throws ValidationException {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                checkFinancialInformation(ca);
                checkSpatialInformation(ca);
            }
        }
    }

    private void checkFinancialInformation(ConsumerAgent ca) throws ValidationException {
        checkHasDoubleAttribute(ca, RAConstants.PURCHASE_POWER);
    }

    private void checkSpatialInformation(ConsumerAgent ca) throws ValidationException {
        checkHasDoubleAttribute(ca, RAConstants.ORIENTATION);
        checkHasDoubleAttribute(ca, RAConstants.SLOPE);
        checkHasDoubleAttribute(ca, RAConstants.SHARE_1_2_HOUSE);
        checkHasDoubleAttribute(ca, RAConstants.HOUSE_OWNER);
    }

    private void checkHasDoubleAttribute(ConsumerAgent ca, String name) throws ValidationException {
        if(!ca.hasAnyAttribute(name)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent '{}' has no attribute '{}'", name);
        }
        Attribute attr = ca.findAttribute(name);
        if(attr.isNoValueAttribute() || attr.asValueAttribute().isNoDataType(DataType.DOUBLE)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent '{}' has no double-attribute '{}'", name);
        }
    }

    private void initUncertainty() {
        uncertaintyManager.initalize();
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca : cag.getAgents()) {
                Uncertainty uncertainty = uncertaintyManager.createFor(ca);
                registerUncertainty(ca, uncertainty, false, environment.isRestored());
            }
        }
    }

    protected Timestamp now() {
        return environment.getTimeModel().now();
    }

    @Override
    public void preSimulationStart() {
        setupTasks();
    }

    private void setupTasks() {
        int firstYear = environment.getTimeModel().getFirstSimulationYear();
        int lastYear = environment.getTimeModel().getLastSimulationYear();

        trace("create syncronisation tasks for process model '{}' ", getName());

        SyncTask firstAnnualTask = createStartOfYearTask(getName() + "_FirstAnnual");
        trace("create first annual task '{}'", firstAnnualTask.getName());
        environment.getLiveCycleControl().registerSyncTaskAsFirstAnnualAction(firstAnnualTask);

        SyncTask lastAnnualTask = createEndOfYearTask(getName() + "_LastAnnual");
        trace("create last annual task '{}'", lastAnnualTask.getName());
        environment.getLiveCycleControl().registerSyncTaskAsLastAnnualAction(lastAnnualTask);

        for(int y = firstYear; y <= lastYear; y++) {
            Timestamp tsWeek27 = environment.getTimeModel().at(y, WEEK27);
            SyncTask taskWeek27 = createWeek27Task(getName() + "_MidYear_" + y);
            trace("created mid year task (27th week) '{}'", taskWeek27.getName());
            environment.getLiveCycleControl().registerSyncTaskAsFirstAction(tsWeek27, taskWeek27);
        }
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
            w27 = environment.getTimeModel().at(ts.getYear(), WEEK27);
            week27Map.put(ts.getYear(), w27);
        }
        return w27;
    }

    @Override
    public void handleNewProduct(Product newProduct) {
        AgentManager agentManager = environment.getAgents();

        for(ConsumerAgentGroup cag: agentManager.getConsumerAgentGroups()) {
            for(ConsumerAgent ca : cag.getAgents()) {
                initalizeInitialProductAwareness(ca, newProduct);
                initalizeInitialProductInterest(ca, newProduct);
                initalizeInitialAdopter(ca, newProduct);
            }
        }
    }

    private void runGlobalInitalization() {
        if(globalRAProcessInitCalled) {
            return;
        }

        globalRAProcessInitCalled = true;
    }

    private void initalizeInitialAdopter(ConsumerAgent ca, Product fp) {
        double chance = getInitialAdopter(ca, fp);
        double draw = rnd.nextDouble();
        boolean isAdopter = draw < chance;
        trace("Is consumer agent '{}' initial adopter of product '{}'? {} ({} < {})", ca.getName(), fp.getName(), isAdopter, draw, chance);
        if(isAdopter) {
            ca.adoptInitial(fp);
        }
    }

    private void initalizeInitialProductInterest(ConsumerAgent ca, Product fp) {
        double interest = getInitialProductInterest(ca, fp);
        if(interest > 0) {
            trace("set awareness for consumer agent '{}' because initial interest value {} for product '{}'", ca.getName(), interest, fp.getName());
            ca.makeAware(fp);
        }
        ca.updateInterest(fp, interest);
        trace("consumer agent '{}' has initial interest value {} for product '{}'", ca.getName(), interest, fp.getName());
    }

    private void initalizeInitialProductAwareness(ConsumerAgent ca, Product fp) {
        if(ca.isAware(fp)) {
            trace("consumer agent '{}' already aware", ca.getName());
            return;
        }

        double chance = getInitialProductAwareness(ca, fp);
        double draw = rnd.nextDouble();
        boolean isAware = draw < chance;
        trace("is consumer agent '{}' initial aware of product '{}'? {} ({} < {})", ca.getName(), fp.getName(), isAware, draw, chance);
        if(isAware) {
            ca.makeAware(fp);
        }
    }

    private SyncTask createStartOfYearTask(String name) {
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
                            if(plan.isModel(RAProcessModel.this)) {
                                RAProcessPlan raPlan = (RAProcessPlan) plan;
                                raPlan.runAdjustmentAtStartOfYear();
                            }
                        }
                    }
                }
            }
        };
    }

    private SyncTask createEndOfYearTask(String name) {
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
                            if(plan.isModel(RAProcessModel.this)) {
                                RAProcessPlan raPlan = (RAProcessPlan) plan;
                                raPlan.runEvaluationAtEndOfYear();
                            }
                        }
                    }
                }
                setYearChange(false);
            }
        };
    }

    private SyncTask createWeek27Task(String name) {
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
                            if(plan.isModel(RAProcessModel.this)) {
                                RAProcessPlan raPlan = (RAProcessPlan) plan;
                                raPlan.runUpdateAtMidOfYear();
                            }
                        }
                    }
                }
            }
        };
    }

    public void registerUncertainty(ConsumerAgent ca, Uncertainty uncertainty, boolean overwrite, boolean allowExisting) {
        if(!allowExisting && uncertaintyCache.containsKey(ca)) {
            throw new IRPactIllegalArgumentException("uncertainty for agent '{}' already exists", ca.getName());
        }

        if(!overwrite && uncertaintyCache.containsKey(ca)) {
            return;
        }

        uncertaintyCache.put(ca, uncertainty);
    }

    public Uncertainty getUncertainty(ConsumerAgent ca) {
        return uncertaintyCache.get(ca);
    }

    protected synchronized Uncertainty getUncertainty0(ConsumerAgent ca) {
        Uncertainty uncertainty = uncertaintyCache.get(ca);
        if(uncertainty == null) {
            return syncGetUncertainty0(ca);
        } else {
            return uncertainty;
        }
    }

    private synchronized Uncertainty syncGetUncertainty0(ConsumerAgent ca) {
        Uncertainty uncertainty = uncertaintyCache.get(ca);
        if(uncertainty == null) {
            uncertainty = uncertaintyManager.createFor(ca);
            registerUncertainty(ca, uncertainty, true, false);
        }
        return uncertainty;
    }

    protected static ConsumerAgent cast(Agent agent) {
        if(agent instanceof ConsumerAgent) {
            return (ConsumerAgent) agent;
        } else {
            throw new IllegalArgumentException("requires ConsumerAgent");
        }
    }

    @Override
    public ProcessPlan newPlan(Agent agent, Need need, Product product) {
        ConsumerAgent cAgent = cast(agent);
        Uncertainty uncertainty = getUncertainty0(cAgent);
        Rnd rnd = environment.getSimulationRandom().deriveInstance();
        return new RAProcessPlan(environment, this, rnd, cAgent, need, product, uncertainty);
    }

    //==================================================
    //attributes
    //==================================================

    public static double getFinancialThreshold(
            ConsumerAgent agent,
            AttributeHelper helper) {
//        double pp = getPurchasePower(agent);
//        double ns = getNoveltySeeking(agent);
//        return pp;
        return helper.findDoubleValue(agent, RAConstants.PURCHASE_POWER);
    }

    //=========================
    //find
    //=========================

    public long getId(ConsumerAgent agent) {
        SpatialInformation info = agent.getSpatialInformation();
        if(info.hasId()) {
            return info.getId();
        } else {
            return ATTR_HELPER.findLongValue(agent, RAConstants.ID);
        }
    }

    public double getFinancialThreshold(ConsumerAgent agent) {
        return getFinancialThreshold(agent, ATTR_HELPER);
    }

    public double getPurchasePower(ConsumerAgent agent) {
        return ATTR_HELPER.findDoubleValue(agent, RAConstants.PURCHASE_POWER);
    }

    public boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        return ATTR_HELPER.findBooleanValue(agent, RAConstants.SHARE_1_2_HOUSE);
    }

    public void setShareOf1Or2FamilyHouse(ConsumerAgent agent, boolean value) {
        ATTR_HELPER.findAndSetBooleanValue(agent, RAConstants.SHARE_1_2_HOUSE, value);
    }

    public boolean isHouseOwner(ConsumerAgent agent) {
        return ATTR_HELPER.findBooleanValue(agent, RAConstants.HOUSE_OWNER);
    }

    public void setHouseOwner(ConsumerAgent agent, boolean value) {
        ATTR_HELPER.findAndSetBooleanValue(agent, RAConstants.HOUSE_OWNER, value);
    }

    //=========================
    //value
    //=========================

    public double getCommunicationFrequencySN(ConsumerAgent agent) {
        return ATTR_HELPER.getDoubleValue(agent, RAConstants.COMMUNICATION_FREQUENCY_SN);
    }

    public double getRewiringRate(ConsumerAgent agent) {
        return ATTR_HELPER.getDoubleValue(agent, RAConstants.REWIRING_RATE);
    }

    public double getNoveltySeeking(ConsumerAgent agent) {
        return ATTR_HELPER.getDoubleValue(agent, RAConstants.NOVELTY_SEEKING);
    }

    public double getDependentJudgmentMaking(ConsumerAgent agent) {
        return ATTR_HELPER.getDoubleValue(agent, RAConstants.DEPENDENT_JUDGMENT_MAKING);
    }

    public double getEnvironmentalConcern(ConsumerAgent agent) {
        return ATTR_HELPER.getDoubleValue(agent, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    public double getConstructionRate(ConsumerAgent agent) {
        return ATTR_HELPER.getDoubleValue(agent, RAConstants.CONSTRUCTION_RATE);
    }

    public double getRenovationRate(ConsumerAgent agent) {
        return ATTR_HELPER.getDoubleValue(agent, RAConstants.RENOVATION_RATE);
    }

    //=========================
    //product
    //=========================

    public double getFinancialThreshold(ConsumerAgent agent, Product product) {
        return ATTR_HELPER.getDoubleValue(agent, product, RAConstants.FINANCIAL_THRESHOLD);
    }

    public double getAdoptionThreshold(ConsumerAgent agent, Product product) {
        return ATTR_HELPER.getDoubleValue(agent, product, RAConstants.ADOPTION_THRESHOLD);
    }

    public double getInitialProductAwareness(ConsumerAgent agent, Product product) {
        return ATTR_HELPER.getDoubleValue(agent, product, RAConstants.INITIAL_PRODUCT_AWARENESS);
    }

    public double getInitialProductInterest(ConsumerAgent agent, Product product) {
        return ATTR_HELPER.getDoubleValue(agent, product, RAConstants.INITIAL_PRODUCT_INTEREST);
    }

    public double getInitialAdopter(ConsumerAgent agent, Product product) {
        return ATTR_HELPER.getDoubleValue(agent, product, RAConstants.INITIAL_ADOPTER);
    }
}
