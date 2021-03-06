package de.unileipzig.irpact.core.process.modular.ca.components.base;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataStore;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.util.StandardFlag;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentModule extends SimulationEntityBase implements ConsumerAgentModule, LoggingHelper {

    protected final Predicate<SocialGraph.Node> IS_CONSUMER = node -> node.is(ConsumerAgent.class);

    public AbstractConsumerAgentModule() {
    }

    //=========================
    // general
    //=========================

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }

    @Override
    public abstract int getChecksum();

    protected int getTotalNumberOfConsumerAgents() {
        return getAgentManager().getTotalNumberOfConsumerAgents();
    }

    protected ConsumerAgentGroupAffinities getAffinities(ConsumerAgent agent) {
        ConsumerAgentGroupAffinityMapping mapping = getAgentManager().getConsumerAgentGroupAffinityMapping();
        if(mapping == null) {
            throw new NullPointerException("ConsumerAgentGroupAffinityMapping");
        }
        return mapping.get(agent.getGroup());
    }

    protected double getShareOfAdopterInSocialNetwork(
            ConsumerAgent agent,
            Product product) {
        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();

        environment.getNetwork().getGraph()
                .streamSourcesAndTargets(agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)
                .filter(IS_CONSUMER)
                .distinct()
                .forEach(globalNode -> {
                    ConsumerAgent ca = globalNode.getAgent(ConsumerAgent.class);
                    if(isPotentialAdopter(ca)) {
                        totalGlobal.inc();
                    }
                    if(ca.hasAdopted(product)) {
                        adopterGlobal.inc();
                    }
                });

        if(totalGlobal.isZero()) {
            return 0.0;
        } else {
            return adopterGlobal.get() / totalGlobal.get();
        }
    }

    protected boolean isPotentialAdopter(ConsumerAgent ca) {
        throw new UnsupportedOperationException("TODO");
    }

    protected double getShareOfAdopterInLocalNetwork(
            Product product,
            NodeFilter filter) {
        MutableDouble totalLocal = MutableDouble.zero();
        MutableDouble adopterLocal = MutableDouble.zero();

        //TODO vllt ergebnisse cachen
        //-> selbiges dann auch gleich bei obrigen
        environment.getNetwork().getGraph()
                .streamNodes()
                .filter(IS_CONSUMER)
                .filter(filter)
                .forEach(localNode -> {
                    totalLocal.inc();
                    if(localNode.getAgent(ConsumerAgent.class).hasAdopted(product)) {
                        adopterLocal.inc();
                    }
                });

        if(totalLocal.isZero()) {
            return 0.0;
        } else {
            return adopterLocal.get() / totalLocal.get();
        }
    }

    //=========================
    // global init
    //=========================

    protected boolean isGlobalInitalized(Object obj) {
        DataStore dataStore = getGlobalData();
        dataStore.lock();
        try {
            return getGlobalData().hasFlag(obj, StandardFlag.INITALIZED);
        } finally {
            dataStore.unlock();
        }
    }

    protected void setGlobalInitalized(Object obj) {
        DataStore dataStore = getGlobalData();
        dataStore.lock();
        try {
            getGlobalData().setFlag(obj, StandardFlag.INITALIZED);
        } finally {
            dataStore.unlock();
        }
    }

    //=========================
    // validate
    //=========================

    protected void validateHasGroupAttribute(String... names) throws ValidationException {
        for(ConsumerAgentGroup cag: getAgentManager().getConsumerAgentGroups()) {
            for(String name: names) {
                validateHasGroupAttribute(cag, name);
            }
        }
    }

    protected void validateHasGroupAttribute(ConsumerAgentGroup cag, String name) throws ValidationException {
        if(!cag.hasGroupAttribute(name)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent group '{}' has no group attribute '{}'", cag.getName(), name);
        }
    }

    protected void validateHasDoubleAttribute(String... names) throws ValidationException {
        for(ConsumerAgent ca: getAgentManager().iterableConsumerAgents()) {
            for(String name: names) {
                validateHasDoubleAttribute(ca, name);
            }
        }
    }

    protected void validateHasDoubleAttribute(ConsumerAgent ca, String name) throws ValidationException {
        if(!ca.hasAnyAttribute(name)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent '{}' has no attribute '{}'", name);
        }
        Attribute attr = ca.findAttribute(name);
        if(attr.isNoValueAttribute() || attr.asValueAttribute().isNoDataType(DataType.DOUBLE)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent '{}' has no double-attribute '{}'", name);
        }
    }

    //=========================
    // data
    //=========================

    protected Random getRandom(ConsumerAgentData data) {
        return data.rnd().getRandom();
    }

    protected double nextDouble(ConsumerAgentData data) {
        return data.rnd().nextDouble();
    }

    protected double getAverageNPV(NPVDataSupplier npvDataSupplier) {
        int total = getTotalNumberOfConsumerAgents();
        if(total == 0) {
            throw new IllegalArgumentException("number of consumer agents is zero");
        }

        double sum = getAgentManager().streamConsumerAgents()
                .mapToDouble(agent -> getNPV(npvDataSupplier, agent))
                .sum();
        return sum / total;
    }

    protected double getNPV(NPVDataSupplier npvDataSupplier, ConsumerAgent agent) {
        return npvDataSupplier.NPV(agent, getCurrentYear());
    }

    protected double getAverageFinancialPurchasePower(NPVDataSupplier npvDataSupplier) {
        return npvDataSupplier.getAverageFinancialPurchasePower(getAgentManager().streamConsumerAgents());
    }

    protected void initNPVDataSupplier(NPVData npvData, NPVDataSupplier dataSupplier) {
        if(isGlobalInitalized(dataSupplier)) {
            trace("NPVDataSupplier (hash={}) already initalized", dataSupplier.hashCode());
            return;
        }

        NPVCalculator npvCalculator = new NPVCalculator();
        npvCalculator.setData(npvData);

        int firstYear = getTimeModel().getFirstSimulationYear();
        int lastYear = getTimeModel().getLastSimulationYear();

        trace(IRPSection.INITIALIZATION_PARAMETER, "calculating npv matrix from '{}' to '{}'", firstYear, lastYear);
        for(int y = firstYear; y <= lastYear; y++) {
            trace("calculate year '{}'", y);
            NPVMatrix matrix = new NPVMatrix();
            matrix.calculate(npvCalculator, y);
            dataSupplier.put(y, matrix);
        }

        trace("NPVDataSupplier (hash={}) initalized", dataSupplier.hashCode());
        setGlobalInitalized(dataSupplier);
    }

    //=========================
    // init
    //=========================

    protected void handleNewProduct(Product product, Rnd rnd) {
        for(ConsumerAgent ca: getAgentManager().iterableConsumerAgents()) {
            initalizeInitialProductAwareness(ca, product, rnd);
            initalizeInitialProductInterest(ca, product);
            initalizeInitialAdopter(ca, product, rnd);
        }
    }

    protected void initalizeInitialAdopter(ConsumerAgent ca, Product fp, Rnd rnd) {
        double chance = getInitialAdopter(ca, fp);
        double draw = rnd.nextDouble();
        boolean isAdopter = draw < chance;
        trace("Is consumer agent '{}' initial adopter of product '{}'? {} ({} < {})", ca.getName(), fp.getName(), isAdopter, draw, chance);
        if(isAdopter) {
            ca.adoptInitial(fp);
        }
    }

    protected void initalizeInitialProductAwareness(ConsumerAgent ca, Product fp, Rnd rnd) {
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

    protected void initalizeInitialProductInterest(ConsumerAgent ca, Product fp) {
        double interest = getInitialProductInterest(ca, fp);
        if(interest > 0) {
            trace("set awareness for consumer agent '{}' because initial interest value {} for product '{}'", ca.getName(), interest, fp.getName());
            ca.makeAware(fp);
        }
        ca.updateInterest(fp, interest);
        trace("consumer agent '{}' has initial interest value {} for product '{}'", ca.getName(), interest, fp.getName());
    }

    protected double getAverageFinancialPurchasePower(Stream<? extends ConsumerAgent> agents, MutableDouble avgFT) {
        if(avgFT != null && avgFT.isNumber()) {
            return avgFT.get();
        } else {
            if(avgFT == null) {
                return calcAvgFT(agents);
            } else {
                return syncCalcAvgFT(agents, avgFT);
            }
        }
    }

    private synchronized double syncCalcAvgFT(Stream<? extends ConsumerAgent> agents, MutableDouble avgFT) {
        if(avgFT != null && avgFT.isNumber()) {
            return avgFT.get();
        } else {
            double result = calcAvgFT(agents);
            if(avgFT != null) {
                avgFT.set(result);
            }
            return result;
        }
    }

    private double calcAvgFT(Stream<? extends ConsumerAgent> agents) {
        MutableDouble total = MutableDouble.zero();
        double sum = agents.mapToDouble(ca -> {
            total.inc();
            return getFinancialPurchasePower(ca);
        }).sum();
        return sum / total.get();
    }

    //=========================
    // agent
    //=========================

    protected void allowAttention(ConsumerAgent agent) {
        agent.allowAttention();
    }

    protected void doSelfActionAndAllowAttention(ConsumerAgent agent) {
        agent.actionPerformed();
        agent.allowAttention();
    }

    protected void makeInterested(ConsumerAgent agent, Product product) {
        agent.makeInterested(product);
    }

    protected void updateInterest(ConsumerAgent agent, Product product, double points) {
        agent.updateInterest(product, points);
    }

    protected double getInterest(ConsumerAgent agent, Product product) {
        return agent.getInterest(product);
    }

    protected boolean isAdopter(ConsumerAgent agent, Product product) {
        return agent.hasAdopted(product);
    }

    protected boolean isInterested(ConsumerAgent agent, Product product) {
        return agent.isInterested(product);
    }

    protected boolean isAware(ConsumerAgent agent, Product product) {
        return agent.isAware(product);
    }

    protected double getInitialAdopter(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.INITIAL_ADOPTER, true);
    }

    protected double getInitialProductAwareness(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.INITIAL_PRODUCT_AWARENESS, true);
    }

    protected double getInitialProductInterest(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.INITIAL_PRODUCT_INTEREST, true);
    }

    protected double getFinancialThreshold(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.FINANCIAL_THRESHOLD, true);
    }

    protected double getAdoptionThreshold(ConsumerAgent agent, Product product) {
        return getAttributeHelper().getDouble(agent, product, RAConstants.ADOPTION_THRESHOLD, true);
    }

    protected double getCommunicationFrequencySN(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, null, RAConstants.COMMUNICATION_FREQUENCY_SN, true);
    }

    protected double getRewiringRate(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, null, RAConstants.REWIRING_RATE, true);
    }

    protected double getEnvironmentalConcern(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, null, RAConstants.ENVIRONMENTAL_CONCERN, true);
    }

    protected boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        return getAttributeHelper().getBoolean(agent, null, RAConstants.SHARE_1_2_HOUSE, true);
    }

    protected boolean isHouseOwner(ConsumerAgent agent) {
        return getAttributeHelper().getBoolean(agent, null, RAConstants.HOUSE_OWNER, true);
    }

    protected double getPurchasePower(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, null, RAConstants.PURCHASE_POWER_EUR, true);
    }

    protected double getNoveltySeeking(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, null, RAConstants.NOVELTY_SEEKING, true);
    }

    protected double getDependentJudgmentMaking(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, null, RAConstants.DEPENDENT_JUDGMENT_MAKING, true);
    }

    protected double getFinancialPurchasePower(ConsumerAgent agent) {
//        return getPurchasePower(agent) * getNoveltySeeking(agent);
        return getPurchasePower(agent);
    }
}
