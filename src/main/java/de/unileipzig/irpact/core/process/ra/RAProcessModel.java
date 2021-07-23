package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.checksum.LoggableChecksum;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class RAProcessModel extends RAProcessModelBase implements LoggableChecksum {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessModel.class);

    protected static boolean globalRAProcessInitCalled = false;

    protected ProcessPlanNodeFilterScheme nodeFilterScheme;

    protected NPVData npvData;
    protected NPVCalculator npvCalculator;
    protected RAModelData modelData;

    public RAProcessModel() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                modelData,
                rnd,
                uncertaintyHandler.getManager()
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
        logHash("uncertainty manager", ChecksumComparable.getChecksum(uncertaintyHandler.getManager()));
    }

    @Override
    public void setEnvironment(SimulationEnvironment environment) {
        super.setEnvironment(environment);
        uncertaintyHandler.setEnvironment(environment);
    }

    public void setModelData(RAModelData modelData) {
        this.modelData = modelData;
        modelData.setAttributeHelper(getAttributeHelper());
    }

    public RAModelData getModelData() {
        return modelData;
    }

    public NPVData getNpvData() {
        return npvData;
    }

    public void setNpvData(NPVData npvData) {
        this.npvData = npvData;
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
        super.postAgentCreationValidation();
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

    protected Timestamp now() {
        return environment.getTimeModel().now();
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
        getUncertaintyCache().createUncertainty(cAgent, getUncertaintyManager());
        Rnd rnd = environment.getSimulationRandom().deriveInstance();
        RAProcessPlan plan = new RAProcessPlan(environment, this, rnd, cAgent, need, product);
        plan.setNetworkFilter(getNodeFilterScheme().createFilter(plan));
        return plan;
    }
}
