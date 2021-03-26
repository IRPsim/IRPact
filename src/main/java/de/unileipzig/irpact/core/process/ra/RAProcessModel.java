package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ra.attributes.BasicUncertaintyGroupAttributeSupplier;
import de.unileipzig.irpact.core.process.ra.attributes.UncertaintyGroupAttributeSupplier;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.time.Month;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class RAProcessModel extends NameableBase implements ProcessModel {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessModel.class);

    public static final ConstantUnivariateDoubleDistribution DIRAQ0 = new ConstantUnivariateDoubleDistribution("RAProcessModel_diraq0", 0.0);

    protected SimulationEnvironment environment;

    protected ConsumerAgentGroupAttributeSupplier underConstructionSupplier = new BasicConsumerAgentGroupAttributeSupplier(RAConstants.UNDER_CONSTRUCTION, DIRAQ0);
    protected ConsumerAgentGroupAttributeSupplier underRenovationSupplier = new BasicConsumerAgentGroupAttributeSupplier(RAConstants.UNDER_RENOVATION, DIRAQ0);

    protected UncertaintyGroupAttributeSupplier uncertaintySupplier = new BasicUncertaintyGroupAttributeSupplier();

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
                underConstructionSupplier.getChecksum(),
                underRenovationSupplier.getChecksum(),
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
        logHash("under construction supplier", ChecksumComparable.getChecksum(underConstructionSupplier));
        logHash("under renovation supplier", ChecksumComparable.getChecksum(underRenovationSupplier));
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

    public ConsumerAgentGroupAttributeSupplier getUnderConstructionSupplier() {
        return underConstructionSupplier;
    }

    public void setUnderConstructionSupplier(ConsumerAgentGroupAttributeSupplier underConstructionSupplier) {
        this.underConstructionSupplier = underConstructionSupplier;
    }

    public ConsumerAgentGroupAttributeSupplier getUnderRenovationSupplier() {
        return underRenovationSupplier;
    }

    public void setUnderRenovationSupplier(ConsumerAgentGroupAttributeSupplier underRenovationSupplier) {
        this.underRenovationSupplier = underRenovationSupplier;
    }

    public UncertaintyGroupAttributeSupplier getUncertaintySupplier() {
        return uncertaintySupplier;
    }

    public void setUncertaintySupplier(UncertaintyGroupAttributeSupplier uncertaintySupplier) {
        this.uncertaintySupplier = uncertaintySupplier;
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        if(npvData == null) {
            throw new MissingDataException("np npv data");
        }

        npvCalculator = new NPVCalculator();
        npvCalculator.setData(npvData);

        int startYear = environment.getTimeModel()
                .getStartYear();
        int endYear = environment.getTimeModel()
                .getEndYearInclusive();

        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "calculating npv matrix from '{}' to '{}'", startYear, endYear);
        for(int y = startYear; y <= endYear; y++) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "> '{}'", y);
            NPVMatrix matrix = new NPVMatrix();
            matrix.calculate(npvCalculator, y);
            modelData.put(y, matrix);
        }

        addMissingGroupAttributesToCags();
    }

    @Override
    public void initialize() {
//        loadNPVData();
    }

    @Override
    public void validate() throws ValidationException {
        if(npvData == null) {
            throw new ValidationException("npv data missing");
        }
        if(npvCalculator == null) {
            throw new ValidationException("npv calculator missing");
        }
    }

    private void addMissingGroupAttributesToCags() {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            if(!underConstructionSupplier.hasGroupAttribute(cag)) {
                underConstructionSupplier.addGroupAttributeTo(cag);
            }
            if(!underRenovationSupplier.hasGroupAttribute(cag)) {
                underRenovationSupplier.addGroupAttributeTo(cag);
            }
            uncertaintySupplier.applyTo(cag);
        }
    }

    @Override
    public void postAgentCreation(boolean initialCall) throws MissingDataException {
        //checkSpatialInformation();
    }

    @Todo("NOCH ETWAS UEBERARBEITEN, bessere validierung")
//    private void checkSpatialInformation() throws MissingDataException {
//        try {
//            for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
//                for(ConsumerAgent ca: cag.getAgents()) {
//                    if(!orientationSupplier.hasAttribute(ca)) {
//                        orientationSupplier.addAttributeTo(ca);
//                    }
//                    if(!slopeSupplier.hasAttribute(ca)) {
//                        slopeSupplier.addAttributeTo(ca);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            throw new MissingDataException(e);
//        }
//    }

    protected Timestamp now() {
        return environment.getTimeModel().now();
    }

    @Override
    public void preSimulationStart() {
        setupTasks();
        addNeedToConsumers();
        checkInitialAdopter();
    }

    private void setupTasks() {
        int startYear = environment.getTimeModel().getStartYear();
        int endYear = environment.getTimeModel().getEndYearInclusive();

        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "create sync points");
        for(int y = startYear; y <= endYear; y++) {
            Timestamp tsJan = environment.getTimeModel().at(startYear, Month.JANUARY, 1);
            SyncTask taskJan = createNewYearTask("NewYear_" + startYear);
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "{} @ {}", taskJan.getName(), tsJan);
            environment.getLiveCycleControl().registerSyncTask(tsJan, taskJan);

            Timestamp tsJuly = environment.getTimeModel().at(startYear, Month.JULY, 1);
            SyncTask taskJuly = createConstructionRenovationSyncTask("ConsReno_" + startYear);
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "{} @ {}", taskJuly.getName(), tsJuly);
            environment.getLiveCycleControl().registerSyncTask(tsJuly, taskJuly);
        }
    }

    @Todo("auslagern")
    private final Need pvNeed = new BasicNeed("PV");
    private void addNeedToConsumers() {
        LOGGER.trace("add initial need '{}'", pvNeed.getName());
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                ca.addNeed(pvNeed);
            }
        }
    }

    @Todo("awareness und interest trennen")
    @Todo("aufraeumen")
    @Todo("ueberlegen welche Parameter noch eine initialisierung benoetigen")
    @Todo("noch eine stage einbauen, aber speziell fuers ProzessModel, fuer init der Agenten")
    private void checkInitialAdopter() {
        LOGGER.trace("setup initial adopter");
        Need need = pvNeed;
        Timestamp startTime = environment.getTimeModel().startTime();
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {

                if(RAProcessPlan.isInitialAdopter(ca)) {
                    Product p = ca.getProductFindingScheme()
                            .findProduct(ca, need);
                    if(p != null) {
                        LOGGER.trace(IRPSection.INITIALIZATION_AGENT, "initial adopter '{}' (need '{}', product '{}')", ca.getName(), need.getName(), p.getName());
                        ca.adoptAt(need, p, startTime);
                    }
                }
                //===
                Product p = ca.getProductFindingScheme()
                        .findProduct(ca, need);
                double initialInterest = RAProcessPlan.getInitialProductInterest(ca);
                ca.getProductInterest().update(p, initialInterest);
                double v =  ca.getProductInterest().isAware(p) ? ca.getProductInterest().getValue(p) : 0;
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, ">>>>> {} {}", ca.getName(), v);
            }
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
                LOGGER.debug("run 'createNewYearTask' ({})", now());
                for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                    for(ConsumerAgent ca: cag.getAgents()) {
                        for(ProcessPlan plan: ca.getPlans().values()) {
                            if(plan instanceof RAProcessPlan) {
                                RAProcessPlan raPlan = (RAProcessPlan) plan;
                                if(raPlan.getModel() == RAProcessModel.this) {
                                    raPlan.adjustParametersOnNewYear();
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    private SyncTask createConstructionRenovationSyncTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                LOGGER.debug("run 'createConstructionRenovationSyncTask' ({})", now());
                for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                    for(ConsumerAgent ca: cag.getAgents()) {
                        for(ProcessPlan plan: ca.getPlans().values()) {
                            if(plan instanceof RAProcessPlan) {
                                RAProcessPlan raPlan = (RAProcessPlan) plan;
                                if(raPlan.getModel() == RAProcessModel.this) {
                                    raPlan.updateConstructionAndRenovation();
                                }
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
