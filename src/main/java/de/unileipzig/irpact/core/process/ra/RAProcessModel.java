package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.Rnd;
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

    public static final ConstantUnivariateDoubleDistribution DIRAQ0 = new ConstantUnivariateDoubleDistribution("RAProcessModel_diraq0", 0.0);

    protected SimulationEnvironment environment;

    protected BasicConsumerAgentSpatialAttributeSupplier orientationSupplier = new BasicConsumerAgentSpatialAttributeSupplier(RAConstants.ORIENTATION);
    protected BasicConsumerAgentSpatialAttributeSupplier slopeSupplier = new BasicConsumerAgentSpatialAttributeSupplier(RAConstants.SLOPE);

    protected BasicConsumerAgentGroupAttributeSupplier underConstructionSupplier = new BasicConsumerAgentGroupAttributeSupplier(RAConstants.UNDER_CONSTRUCTION, DIRAQ0);
    protected BasicConsumerAgentGroupAttributeSupplier underRenovationSupplier = new BasicConsumerAgentGroupAttributeSupplier(RAConstants.UNDER_RENOVATION, DIRAQ0);

    protected BasicUncertaintyGroupAttributeSupplier uncertaintySupplier = new BasicUncertaintyGroupAttributeSupplier();

    protected NPVData npvData;
    protected NPVCalculator npvCalculator;
    protected RAModelData modelData;
    protected Rnd rnd;

    public RAProcessModel() {
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                modelData.getHashCode(),
                rnd.getHashCode(),
                orientationSupplier.getHashCode(),
                slopeSupplier.getHashCode(),
                underConstructionSupplier.getHashCode(),
                underRenovationSupplier.getHashCode(),
                uncertaintySupplier.getHashCode()
        );
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

    public void setNpvData(NPVData npvData) {
        this.npvData = npvData;
    }

    public BasicConsumerAgentSpatialAttributeSupplier getSlopeSupplier() {
        return slopeSupplier;
    }

    public void setSlopeSupplier(BasicConsumerAgentSpatialAttributeSupplier slopeSupplier) {
        this.slopeSupplier = slopeSupplier;
    }

    public BasicConsumerAgentSpatialAttributeSupplier getOrientationSupplier() {
        return orientationSupplier;
    }

    public void setOrientationSupplier(BasicConsumerAgentSpatialAttributeSupplier orientationSupplier) {
        this.orientationSupplier = orientationSupplier;
    }

    public BasicConsumerAgentGroupAttributeSupplier getUnderConstructionSupplier() {
        return underConstructionSupplier;
    }

    public void setUnderConstructionSupplier(BasicConsumerAgentGroupAttributeSupplier underConstructionSupplier) {
        this.underConstructionSupplier = underConstructionSupplier;
    }

    public BasicConsumerAgentGroupAttributeSupplier getUnderRenovationSupplier() {
        return underRenovationSupplier;
    }

    public void setUnderRenovationSupplier(BasicConsumerAgentGroupAttributeSupplier underRenovationSupplier) {
        this.underRenovationSupplier = underRenovationSupplier;
    }

    public BasicUncertaintyGroupAttributeSupplier getUncertaintySupplier() {
        return uncertaintySupplier;
    }

    public void setUncertaintySupplier(BasicUncertaintyGroupAttributeSupplier uncertaintySupplier) {
        this.uncertaintySupplier = uncertaintySupplier;
    }

    @Override
    public void preAgentCreation() {
        if(npvData != null) {
            npvCalculator = new NPVCalculator();
            npvCalculator.setData(npvData);
        }

        int startYear = environment.getTimeModel().getStartYear();
        int endYear = environment.getTimeModel().getEndYearInclusive();

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
    public void postAgentCreation() throws MissingDataException {
        checkSpatialInformation();
    }

    private void checkSpatialInformation() throws MissingDataException {
        try {
            for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                for(ConsumerAgent ca: cag.getAgents()) {
                    if(!orientationSupplier.hasAttribute(ca)) {
                        orientationSupplier.addAttributeTo(ca);
                    }
                    if(!slopeSupplier.hasAttribute(ca)) {
                        slopeSupplier.addAttributeTo(ca);
                    }
                }
            }
        } catch (Exception e) {
            throw new MissingDataException(e);
        }
    }

    @Override
    public void preSimulationStart() {
        int startYear = environment.getTimeModel().getStartYear();
        int endYear = environment.getTimeModel().getEndYearInclusive();

        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "create sync points");
        for(int y = startYear; y <= endYear; y++) {
            Timestamp ts = environment.getTimeModel().at(startYear, Month.JULY, 1);
            SyncTask task = createConstructionRenovationSyncTask("ConsReno_" + startYear);
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "{} @ {}", task.getName(), ts);
            environment.getLiveCycleControl().registerSyncTask(ts, task);
        }
    }

    private SyncTask createConstructionRenovationSyncTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                LOGGER.debug("HALLO SYNC");
            }
        };
    }

    @Override
    public void onNewSimulationPeriod() {
        AgentManager agentManager = environment.getAgents();
        agentManager.streamConsumerAgents()
                .forEach(this::setupAgentForNewPeriod);
    }

    protected void setupAgentForNewPeriod(ConsumerAgent agent) {
        double renovationRate = RAProcessPlan.getRenovationRate(agent);
        boolean doRenovation = rnd.nextDouble() < renovationRate;
        setUnderRenovation(agent, doRenovation);

        double constructionRate = RAProcessPlan.getConstructionRate(agent);
        boolean doConstruction = rnd.nextDouble() < constructionRate;
        setUnderConstruction(agent, doConstruction);
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

    protected void setUnderConstruction(ConsumerAgent agent, boolean value) {
        double dvalue = value ? 1.0 : 0.0;
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_CONSTRUCTION);
        attr.setDoubleValue(dvalue);
    }

    protected void setUnderRenovation(ConsumerAgent agent, boolean value) {
        double dvalue = value ? 1.0 : 0.0;
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_RENOVATION);
        attr.setDoubleValue(dvalue);
    }
}
