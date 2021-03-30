package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.time.Month;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class RAProcessModel extends NameableBase implements ProcessModel {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessModel.class);

    private static final long WEEK27 = 27;

    protected static boolean globalRAProcessInitCalled = false;

    protected SimulationEnvironment environment;

    protected MultiConsumerAgentGroupAttributeSupplier uncertaintySupplier = new BasicMultiConsumerAgentGroupAttributeSupplier();

    protected ProcessPlanNodeFilterScheme nodeFilterScheme;

    protected NPVData npvData;
    protected NPVCalculator npvCalculator;
    protected RAModelData modelData;
    protected Rnd rnd;

    public RAProcessModel() {
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                modelData.getChecksum(),
                rnd.getChecksum(),
                uncertaintySupplier.getChecksum()
        );
    }

    private static void logHash(String msg, int storedHash) {
        LOGGER.warn(
                "hash @ '{}': stored={}",
                msg,
                Integer.toHexString(storedHash)
        );
    }

    public void deepHashCode() {
        logHash("name", ChecksumComparable.getChecksum(getName()));
        logHash("model data", ChecksumComparable.getChecksum(modelData));
        logHash("rnd", ChecksumComparable.getChecksum(rnd));
        logHash("uncertainty supplier", ChecksumComparable.getChecksum(uncertaintySupplier));
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setModelData(RAModelData modelData) {
        this.modelData = modelData;
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

    public MultiConsumerAgentGroupAttributeSupplier getUncertaintySupplier() {
        return uncertaintySupplier;
    }

    public void setUncertaintySupplier(MultiConsumerAgentGroupAttributeSupplier uncertaintySupplier) {
        this.uncertaintySupplier = uncertaintySupplier;
    }

    public void setNodeFilterScheme(ProcessPlanNodeFilterScheme nodeFilterScheme) {
        this.nodeFilterScheme = nodeFilterScheme;
    }

    public ProcessPlanNodeFilterScheme getNodeFilterScheme() {
        return nodeFilterScheme;
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        if(npvData == null) {
            throw new MissingDataException("np npv data");
        }

        npvCalculator = new NPVCalculator();
        npvCalculator.setData(npvData);

        int firstYear = environment.getTimeModel()
                .getFirstSimulationYear();
        int lastYear = environment.getTimeModel()
                .getLastSimulationYear();

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "calculating npv matrix from '{}' to '{}'", firstYear, lastYear);
        for(int y = firstYear; y <= lastYear; y++) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "calculate year '{}'", y);
            NPVMatrix matrix = new NPVMatrix();
            matrix.calculate(npvCalculator, y);
            modelData.put(y, matrix);
        }

        addMissingGroupAttributesToCags();
    }

    private void addMissingGroupAttributesToCags() {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            uncertaintySupplier.addAllGroupAttributesTo(cag);
        }
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        if(npvData == null) {
            throw new ValidationException("npv data missing");
        }
        if(npvCalculator == null) {
            throw new ValidationException("npv calculator missing");
        }

        checkGroupAttributExistance();
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
        checkSpatialInformation();
    }

    private void checkSpatialInformation() throws ValidationException {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                checkHasAnyAttribute(ca, RAConstants.ORIENTATION);
                checkHasAnyAttribute(ca, RAConstants.SLOPE);
                checkHasAnyAttribute(ca, RAConstants.SHARE_1_2_HOUSE);
                checkHasAnyAttribute(ca, RAConstants.HOUSE_OWNER);
            }
        }
    }

    private void checkHasAnyAttribute(ConsumerAgent ca, String name) throws ValidationException {
        if(!ca.hasAnyAttribute(name)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent '{}' has no attribute '{}'", name);
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

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "create syncronisation tasks for process model '{}' ", getName());
        for(int y = firstYear; y <= lastYear; y++) {
            Timestamp tsJan = environment.getTimeModel().atStartOfYear(y);
            SyncTask taskJan = createNewYearTask(getName() + "_NewYear_" + y);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "create new year task '{}'", taskJan.getName());
            environment.getLiveCycleControl().registerSyncTask(tsJan, taskJan);

            Timestamp tsWeek27 = environment.getTimeModel().at(y, WEEK27);
            SyncTask taskWeek27 = createWeek27Task(getName() + "_MidYear_" + y);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created mid year task (27th week) '{}'", taskJan.getName());
            environment.getLiveCycleControl().registerSyncTask(tsWeek27, taskWeek27);
        }
    }

    @Override
    public void handleNewProduct(Product newProduct) {
        AgentManager agentManager = environment.getAgents();

        for(ConsumerAgentGroup cag: agentManager.getConsumerAgentGroups()) {
            for(ConsumerAgent ca : cag.getAgents()) {

                if(initalizeInitialAdopter(ca, newProduct)) {
                    continue;
                }
                if(initalizeInitialProductInterest(ca, newProduct)) {
                    continue;
                }
                initalizeInitialProductAwareness(ca, newProduct);
            }
        }
    }

    private void runGlobalInitalization() {
        if(globalRAProcessInitCalled) {
            return;
        }

        globalRAProcessInitCalled = true;
    }

    private boolean initalizeInitialAdopter(ConsumerAgent ca, Product fp) {
        double chance = RAProcessPlan.getInitialAdopter(ca, fp);
        double draw = rnd.nextDouble();
        boolean isAdopter = draw < chance;
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "Is consumer agent '{}' initial adopter of product '{}'? {} ({} < {})", ca.getName(), fp.getName(), isAdopter, draw, chance);
        if(isAdopter) {
            if(!ca.isAware(fp)) {
                ca.makeAware(fp);
            }
            if(!ca.isInterested(fp)) {
                ca.makeInterested(fp);
            }
            ca.adoptInitial(fp);
            return true;
        } else {
            return false;
        }
    }

    private void initalizeInitialProductAwareness(ConsumerAgent ca, Product fp) {
        double chance = RAProcessPlan.getInitialProductAwareness(ca, fp);
        double draw = rnd.nextDouble();
        boolean isAware = draw < chance;
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "is consumer agent '{}' initial aware of product '{}'? {} ({} < {})", ca.getName(), fp.getName(), isAware, draw, chance);
        if(isAware) {
            ca.makeAware(fp);
        }
    }

    private boolean initalizeInitialProductInterest(ConsumerAgent ca, Product fp) {
        double interest = RAProcessPlan.getInitialProductInterest(ca, fp);
        if(interest > 0) {
            if(!ca.isAware(fp)) {
                ca.makeAware(fp);
            }
            ca.updateInterest(fp, interest);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "consumer agent '{}' has initial interest value {} for product '{}'", ca.getName(), interest, fp.getName());
            return true;
        } else {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "consumer agent '{}' has no initial interest for product '{}'", ca.getName(), fp.getName());
            return false;
        }
    }

    private SyncTask createNewYearTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                LOGGER.trace(IRPSection.SIMULATION_PROCESS, "run 'createNewYearTask'");

                for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                    for(ConsumerAgent ca: cag.getAgents()) {
                        for(ProcessPlan plan: ca.getPlans().values()) {
                            if(plan.isModel(RAProcessModel.this)) {
                                RAProcessPlan raPlan = (RAProcessPlan) plan;
                                raPlan.adjustParametersOnNewYear();
                            }
                        }
                    }
                }
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
                LOGGER.trace(IRPSection.SIMULATION_PROCESS, "run 'createConstructionRenovationSyncTask'");

                for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                    for(ConsumerAgent ca: cag.getAgents()) {
                        for(ProcessPlan plan: ca.getPlans().values()) {
                            if(plan.isModel(RAProcessModel.this)) {
                                RAProcessPlan raPlan = (RAProcessPlan) plan;
                                raPlan.updateConstructionAndRenovation();
                            }
                        }
                    }
                }
            }
        };
    }

    @Override
    public ProcessPlan newPlan(Agent agent, Need need, Product product) {
        ConsumerAgent cAgent = cast(agent);
        Rnd rnd = environment.getSimulationRandom().deriveInstance();
        return new RAProcessPlan(environment, this, rnd, cAgent, need, product);
    }

    protected static ConsumerAgent cast(Agent agent) {
        if(agent instanceof ConsumerAgent) {
            return (ConsumerAgent) agent;
        } else {
            throw new IllegalArgumentException("requires ConsumerAgent");
        }
    }

    //=========================
    //util
    //=========================
}
