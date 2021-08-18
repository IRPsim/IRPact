package de.unileipzig.irpact.core.process.mra.component.special;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.DataType;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.Acting;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.*;
import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.mra.ModularRAProcessModel;
import de.unileipzig.irpact.core.process.mra.component.generic.AbstractComponent;
import de.unileipzig.irpact.core.process.mra.component.generic.ComponentType;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.process.ra.uncert.*;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public abstract class AbstractMRAComponent extends AbstractComponent implements LoggingHelper, InitalizablePart {

    protected final Predicate<SocialGraph.Node> IS_CONSUMER = node -> node.is(ConsumerAgent.class);

    protected AbstractMRAComponent(ComponentType type) {
        super(type);
    }

    //=========================
    //fields +get/set
    //=========================

    protected SimulationEnvironment environment;
    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }
    public SimulationEnvironment getEnvironment() {
        return environment;
    }
    protected int getCurrentYear() {
        return getEnvironment().getTimeModel().getCurrentYear();
    }

    protected ModularRAProcessModel model;
    public void setModel(ModularRAProcessModel model) {
        this.model = model;
    }
    public ModularRAProcessModel getModel() {
        return model;
    }

    protected int adopterPoints = RAModelData.DEFAULT_ADOPTER_POINTS;
    public void setAdopterPoints(int adopterPoints) {
        this.adopterPoints = adopterPoints;
    }
    public int getAdopterPoints() {
        return adopterPoints;
    }

    protected int interestedPoints = RAModelData.DEFAULT_INTERESTED_POINTS;
    public void setInterestedPoints(int interestedPoints) {
        this.interestedPoints = interestedPoints;
    }
    public int getInterestedPoints() {
        return interestedPoints;
    }

    protected int awarePoints = RAModelData.DEFAULT_AWARE_POINTS;
    public void setAwarePoints(int awarePoints) {
        this.awarePoints = awarePoints;
    }
    public int getAwarePoints() {
        return awarePoints;
    }

    protected int unknownPoints = RAModelData.DEFAULT_UNKNOWN_POINTS;
    public void setUnknownPoints(int unknownPoints) {
        this.unknownPoints = unknownPoints;
    }
    public int getUnknownPoints() {
        return unknownPoints;
    }

    protected double a;
    public void setA(double a) {
        this.a = a;
    }
    public double getA() {
        return a;
    }

    protected double b;
    public void setB(double b) {
        this.b = b;
    }
    public double getB() {
        return b;
    }

    protected double c;
    public void setC(double c) {
        this.c = c;
    }
    public double getC() {
        return c;
    }

    protected double d;
    public void setD(double d) {
        this.d = d;
    }
    public double getD() {
        return d;
    }

    protected double logisticFactor = RAConstants.DEFAULT_LOGISTIC_FACTOR;
    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }
    public double getLogisticFactor() {
        return logisticFactor;
    }

    protected ProcessPlanNodeFilterScheme nodeFilterScheme;
    public void setNodeFilterScheme(ProcessPlanNodeFilterScheme nodeFilterScheme) {
        this.nodeFilterScheme = nodeFilterScheme;
    }
    public ProcessPlanNodeFilterScheme getNodeFilterScheme() {
        return nodeFilterScheme;
    }

    protected NPVDataSupplier npvDataSupplier;
    public void setNPVDataSupplier(NPVDataSupplier npvDataSupplier) {
        this.npvDataSupplier = npvDataSupplier;
    }
    public NPVDataSupplier getNPVDataSupplier() {
        return npvDataSupplier;
    }

    protected NPVData npvData;
    public void setNpvData(NPVData npvData) {
        this.npvData = npvData;
    }
    public NPVData getNpvData() {
        return npvData;
    }

    protected double weightFT = 0.5;
    public void setWeightFT(double weightFT) {
        this.weightFT = weightFT;
    }
    public double getWeightFT() {
        return weightFT;
    }

    protected double weightNPV = 0.5;
    public void setWeightNPV(double weightNPV) {
        this.weightNPV = weightNPV;
    }
    public double getWeightNPV() {
        return weightNPV;
    }

    protected double weightSocial = 0.5;
    public void setWeightSocial(double weightSocial) {
        this.weightSocial = weightSocial;
    }
    public double getWeightSocial() {
        return weightSocial;
    }

    protected double weightLocal = 0.5;
    public void setWeightLocal(double weightLocal) {
        this.weightLocal = weightLocal;
    }
    public double getWeightLocal() {
        return weightLocal;
    }

    //=========================
    //init
    //=========================

    //=========================
    //...
    //=========================

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public final IRPSection getDefaultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }

    protected final void doSelfActionAndAllowAttention(Acting actor) {
        actor.actionPerformed();
        actor.allowAttention();
    }

    protected ProcessPlanResult doAction(
            ConsumerAgent agent,
            Rnd rnd,
            Product product) {
        trace("[{}] doAction", agent.getName());

        agent.allowAttention();

        if(doCommunicate(agent, rnd)) {
            return communicate(agent, rnd, product);
        }

        if(doRewire(agent, rnd)) {
            return rewire(agent, rnd);
        }

        return nop(agent);
    }

    protected final boolean doCommunicate(ConsumerAgent agent, Rnd rnd) {
        double r = rnd.nextDouble();
        double freq = getCommunicationFrequencySN(agent);
        boolean doCommunicate = r < freq;
        trace("[{}] do communicate: {} ({} < {})", agent.getName(), doCommunicate, r, freq);
        return doCommunicate;
    }

    protected ProcessPlanResult communicate(ConsumerAgent agent, Rnd rnd, Product product) {
        trace("[{}] start communication", agent.getName());

        SocialGraph graph = getEnvironment().getNetwork().getGraph();
        SocialGraph.Node node = agent.getSocialGraphNode();
        //create random target order
        List<SocialGraph.Node> targetList = new ArrayList<>();
        graph.getTargets(node, SocialGraph.Type.COMMUNICATION, targetList);
        Collections.shuffle(targetList, rnd.getRandom());

        for(SocialGraph.Node targetNode: targetList) {
            ConsumerAgent targetAgent = targetNode.getAgent(ConsumerAgent.class);

            Acting.AttentionResult result = Acting.aquireDoubleSidedAttentionAndDataAccess(agent, targetAgent);
            switch (result) {
                case SELF_OCCUPIED:
                    trace("[{}] failed communication('{}' -> '{}') , self occupied", agent.getName(), agent.getName(), targetAgent.getName());
                    return ProcessPlanResult.IN_PROCESS;

                case TARGET_OCCUPIED:
                    trace("[{}] failed communication ('{}' -> '{}'), target occupied, try next target", agent.getName(), agent.getName(), targetAgent.getName());
                    continue;

                case SUCCESS:
                    agent.actionPerformed();
                    targetAgent.actionPerformed();
                    agent.releaseAttention();
                    targetAgent.releaseAttention();

                    //nur hier haben wir datalock durch aquireAttentionAndDataAccess
                    try {
                        trace("[{}] start communication ('{}' -> '{}')", agent.getName(), agent.getName(), targetAgent.getName());

                        updateInterest(agent, product, targetAgent);
                        updateCommunicationGraph(agent, graph, targetNode);
                        applyRelativeAgreement(agent, targetAgent);

                        return ProcessPlanResult.IN_PROCESS;
                    } finally {
                        agent.releaseDataAccess();
                        targetAgent.releaseDataAccess();
                    }
            }
        }

        trace("[{}] no valid communication partner found", agent.getName());
        return ProcessPlanResult.IN_PROCESS;
    }

    protected final boolean doRewire(ConsumerAgent agent, Rnd rnd) {
        double r = rnd.nextDouble();
        double freq = getRewiringRate(agent);
        boolean doRewire = r < freq;
        trace("[{}] do rewire: {} ({} < {})", agent.getName(), doRewire, r, freq);
        return doRewire;
    }

    protected ProcessPlanResult rewire(ConsumerAgent agent, Rnd rnd) {
        if(agent.tryAquireAttention()) {
            agent.actionPerformed();
            agent.aquireDataAccess(); //lock data
            agent.releaseAttention();
        } else {
            trace("[{}] rewire canceled, already occupied", agent.getName());
            return ProcessPlanResult.IN_PROCESS;
        }

        //data locked
        try {
            trace("[{}] rewire", agent.getName());

            SocialGraph graph = getEnvironment().getNetwork().getGraph();
            SocialGraph.Node node = agent.getSocialGraphNode();
            SocialGraph.LinkageInformation linkInfo = graph.getLinkageInformation(node);

            //remove
            SocialGraph.Node rndTarget = graph.getRandomTarget(node, SocialGraph.Type.COMMUNICATION, rnd);
            if(rndTarget == null) {
                logGraphUpdateEdgeRemoved(agent, null);
            } else {
                ConsumerAgentGroup tarCag = rndTarget.getAgent(ConsumerAgent.class).getGroup();
                SocialGraph.Edge edge = graph.getEdge(node, rndTarget, SocialGraph.Type.COMMUNICATION);
                graph.removeEdge(edge);
                logGraphUpdateEdgeRemoved(agent, rndTarget.getAgent());
                updateLinkCount(agent, linkInfo, tarCag, -1); //dec
            }

            ConsumerAgentGroupAffinities affinities = getEnvironment().getAgents()
                    .getConsumerAgentGroupAffinityMapping()
                    .get(agent.getGroup());

            SocialGraph.Node tarNode = null;
            ConsumerAgentGroup tarCag = null;
            while(tarNode == null) {
                if(affinities.isEmpty()) {
                    tarCag = null;
                    break;
                }

                tarCag = affinities.getWeightedRandom(rnd);

                int currentLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
                int maxTargetAgents = tarCag.getNumberOfAgents();
                if(tarCag == agent.getGroup()) {
                    maxTargetAgents -= 1;
                }

                trace("[{}] tar: {}, currentLinkCount: {}, max: {} (self: {})", agent.getName(), tarCag.getName(), currentLinkCount, maxTargetAgents, agent.getGroup() == tarCag);

                if(maxTargetAgents == currentLinkCount) {
                    affinities = affinities.createWithout(tarCag);
                    continue;
                }

                int unlinkedInCag = maxTargetAgents - currentLinkCount;

                tarNode = getRandomUnlinked(
                        agent,
                        rnd,
                        agent.getSocialGraphNode(),
                        tarCag,
                        unlinkedInCag,
                        SocialGraph.Type.COMMUNICATION
                );
            }

            if(tarNode == null) {
                trace("[{}] no valid rewire target found", agent.getName());
            } else {
                graph.addEdge(agent.getSocialGraphNode(), tarNode, SocialGraph.Type.COMMUNICATION, 1.0);
                logGraphUpdateEdgeAdded(agent, tarNode.getAgent());
                updateLinkCount(agent, linkInfo, tarCag, 1); //inc
            }

            return ProcessPlanResult.IN_PROCESS;
        } finally {
            agent.releaseDataAccess();
        }
    }

    protected ProcessPlanResult nop(ConsumerAgent agent) {
        trace("[{}] nop", agent.getName());
        return ProcessPlanResult.IN_PROCESS;
    }

    @Todo
    protected final void applyRelativeAgreement(ConsumerAgent agent, ConsumerAgent target) {
        applyRelativeAgreement(agent, target, RAConstants.NOVELTY_SEEKING);
        applyRelativeAgreement(agent, target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
        applyRelativeAgreement(agent, target, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    @Todo
    protected final void applyRelativeAgreement(ConsumerAgent agent, ConsumerAgent target, String attrName) {
        ConsumerAgentAttribute opinionThis = agent.getAttribute(attrName);
        Uncertainty uncertaintyThis = getUncertainty(agent);
        ConsumerAgentAttribute opinionTarget = target.getAttribute(attrName);
        Uncertainty uncertaintyTarget = getUncertainty(target);
        if(uncertaintyTarget == null) {
            warn("agent '{}' has no uncertainty - skip", target.getName());
        } else {
            getModel().getRelativeAgreementAlgorithm().apply(
                    agent.getName(),
                    opinionThis,
                    uncertaintyThis,
                    target.getName(),
                    opinionTarget,
                    uncertaintyTarget
            );
        }
    }

    protected final void updateInterest(
            ConsumerAgent agent,
            Product product,
            ConsumerAgent targetAgent) {
        double myInterest = getInterest(agent, product);
        double targetInterest = getInterest(targetAgent, product);

        double myPointsToAdd = getInterestPoints(agent, product);
        double targetPointsToAdd = getInterestPoints(targetAgent, product);

        updateInterest(agent, product, targetPointsToAdd);
        updateInterest(targetAgent, product, myPointsToAdd);

        logInterestUpdate(agent, myInterest, getInterest(agent, product), targetAgent, targetInterest, getInterest(targetAgent, product));
    }

    protected final void updateCommunicationGraph(ConsumerAgent agent, SocialGraph graph, SocialGraph.Node target) {
        if(!graph.hasEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
            graph.addEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION, 1.0);
            logGraphUpdateEdgeAdded(agent, target.getAgent());
        }
    }

    protected void updateLinkCount(
            Agent agent,
            SocialGraph.LinkageInformation linkInfo,
            ConsumerAgentGroup tarCag,
            int delta) {
        int oldLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
        linkInfo.update(tarCag, SocialGraph.Type.COMMUNICATION, delta);
        int newLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
        trace("[{}] update link count to cag '{}': {} -> {}", agent.getName(), tarCag.getName(), oldLinkCount, newLinkCount);
    }

    @SuppressWarnings("SameParameterValue")
    protected SocialGraph.Node getRandomUnlinked(
            ConsumerAgent agent,
            Rnd rnd,
            SocialGraph.Node srcNode,
            ConsumerAgentGroup tarCag,
            int unlinkedInCag,
            SocialGraph.Type type) {
        SocialGraph graph = getEnvironment().getNetwork().getGraph();

        int rndId = rnd.nextInt(unlinkedInCag);
        int id = 0;

        for(Agent tar: tarCag.getAgents()) {
            if(tar == agent) {
                continue;
            }
            SocialGraph.Node tarNode = tar.getSocialGraphNode();
            if(graph.hasNoEdge(srcNode, tarNode, type)) {
                if(id == rndId) {
                    return tarNode;
                } else {
                    id++;
                }
            }
        }

        return null;
    }

    protected double getFinancialComponent(ConsumerAgent agent) {
        double npvAvg = getAverageNPV();
        double npvThis = getNPV(agent);

        return getFinancialComponent(agent,  npvAvg, npvThis);
    }

    protected double getFinancialComponent(
            ConsumerAgent agent,
            double npvAvg,
            double npvThis) {
        double ftAvg = getAverageFinancialPurchasePower();
        double ftThis = getFinancialPurchasePower(agent);

        double ft = getLogisticFactor() * (ftThis - ftAvg);
        double npv = getLogisticFactor() * (npvThis - npvAvg);

        double logisticFt = MathUtil.logistic(ft);
        double logisticNpv = MathUtil.logistic(npv);

        double comp = (getWeightFT() * logisticFt) + (getWeightNPV() * logisticNpv);

        logFinancialComponent(agent, ftAvg, ftThis, npvAvg, npvThis, getLogisticFactor(), ft, npv, logisticFt, logisticNpv, comp);

        return comp;
    }

    protected double getEnvironmentalComponent(ConsumerAgent agent) {
        return getEnvironmentalConcern(agent);
    }

    protected double getNoveltyCompoenent(ConsumerAgent agent) {
        return getNoveltySeeking(agent);
    }

    protected double getSocialComponent(ConsumerAgent agent, Product product, NodeFilter filter) {
        MutableDouble shareOfAdopterInSocialNetwork = MutableDouble.zero();
        MutableDouble shareOfAdopterInLocalArea = MutableDouble.zero();
        getShareOfAdopterInSocialNetworkAndLocalArea(agent, product,  filter, shareOfAdopterInSocialNetwork, shareOfAdopterInLocalArea);
        double djm = getDependentJudgmentMaking(agent);
        double comp = djm * ((getWeightSocial() * shareOfAdopterInSocialNetwork.get()) + (getWeightLocal() * shareOfAdopterInLocalArea.get()));
        trace(
                "[{}] dependent judgment making = {}, share of adopter in social network = {}, share of adopter in local area = {}, social component = {} = {} * ({} + {}) / 2.0",
                agent.getName(), djm, shareOfAdopterInSocialNetwork.get(), shareOfAdopterInLocalArea.get(), comp, djm, shareOfAdopterInSocialNetwork.get(), shareOfAdopterInLocalArea.get()
        );
        return comp;
    }

    protected void getShareOfAdopterInSocialNetworkAndLocalArea(
            ConsumerAgent agent,
            Product product,
            NodeFilter filter,
            MutableDouble global,
            MutableDouble local) {
        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();
        MutableDouble totalLocal = MutableDouble.zero();
        MutableDouble adopterLocal = MutableDouble.zero();

        getEnvironment().getNetwork().getGraph()
                .streamSourcesAndTargets(agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)
                .filter(IS_CONSUMER)
                .distinct()
                .forEach(globalNode -> {
                    totalGlobal.inc();
                    if(globalNode.getAgent(ConsumerAgent.class).hasAdopted(product)) {
                        adopterGlobal.inc();
                    }
                });

        //TODO vllt ergebnisse cachen
        //-> selbiges dann auch gleich bei obrigen
        getEnvironment().getNetwork().getGraph()
                .streamNodes()
                .filter(IS_CONSUMER)
                .filter(filter)
                .forEach(localNode -> {
                    totalLocal.inc();
                    if(localNode.getAgent(ConsumerAgent.class).hasAdopted(product)) {
                        adopterLocal.inc();
                    }
                });

        if(totalGlobal.isZero()) {
            global.set(0);
        } else {
            global.set(adopterGlobal.get() / totalGlobal.get());
        }
        if(totalLocal.isZero()) {
            local.set(0);
        } else {
            local.set(adopterLocal.get() / totalLocal.get());
        }

        logShareOfAdopterInSocialNetworkAndLocalArea(agent,  totalGlobal, adopterGlobal, global, totalLocal, adopterLocal, local);
    }

    //=========================
    //logging
    //=========================

    protected final IRPLogger getLogger(boolean logData) {
        return logData
                ? IRPLogging.getResultLogger()
                : getDefaultLogger();
    }

    protected final IRPSection getSection(boolean logData) {
        return getDefaultSection().orGeneral(logData);
    }

    protected static Level getLevel(boolean logData) {
        return logData
                ? Level.INFO
                : Level.TRACE;
    }

    protected final void logGraphUpdateEdgeAdded(Agent agent, Agent target) {
        boolean logData = getEnvironment().getSettings().isLogGraphUpdate();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        logger.log(
                section, level,
                "{} [{}] graph update, edge added: '{}' -> '{}'",
                InfoTag.GRAPH_UPDATE, agent.getName(),
                agent.getName(), target.getName()
        );
    }

    protected final void logInterestUpdate(
            Agent agent,
            double myOldPoints, double myNewPoints,
            Agent target, double targetOldPoints, double targetNewPoints) {
        boolean logData = getEnvironment().getSettings().isLogInterestUpdate();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        logger.log(
                section, level,
                "{} [{}] interest update: '{}': {} -> {}, '{}': {} -> {}",
                InfoTag.INTEREST_UPDATE, agent.getName(),
                agent.getName(), myOldPoints, myNewPoints,
                target.getName(), targetOldPoints, targetNewPoints
        );
    }

    protected void logGraphUpdateEdgeRemoved(Agent agent, Agent target) {
        boolean logData = getEnvironment().getSettings().isLogGraphUpdate();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        if(target == null) {
            logger.log(
                    section, level,
                    "{} [{}] graph not updated, no valid target found",
                    InfoTag.GRAPH_UPDATE, agent.getName()
            );
        } else {
            logger.log(
                    section, level,
                    "{} [{}] graph update, edge removed: '{}' -> '{}'",
                    InfoTag.GRAPH_UPDATE, agent.getName(),
                    agent.getName(), target.getName()
            );
        }
    }

    protected void logFinancialComponent(
            ConsumerAgent agent,
            double ftAvg, double ftThis,
            double npvAvg, double npvThis,
            double logisticFactor,
            double ft, double npv,
            double logisticFt, double logisticNpv,
            double comp) {
        boolean logData = getEnvironment().getSettings().isLogFinancialComponent();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        logger.log(section, level,
                "{} [{}] financal component calculation: t_avg = {}, t = {}, NPV_avg = {}, NPV = {}, lf = {} | lf * (t - t_avg) = {}, lf * (NPV - NPV_avg) = {} | 1 / (1 + e^(-(lf * (t - t_avg))) = {}, 1 / (1 + e^(-(lf * (NPV - NPV_avg))) = {} | (logistic(t) + logistic(NPV)) / 2.0 = {}",
                InfoTag.FINANCAL_COMPONENT, agent.getName(),
                ftAvg, ftThis, npvAvg, npvThis, logisticFactor, ft, npv, logisticFt, logisticNpv, comp
        );
    }

    protected void logShareOfAdopterInSocialNetworkAndLocalArea(
            ConsumerAgent agent,
            MutableDouble totalGlobal,
            MutableDouble adopterGlobal,
            MutableDouble shareGlobal,
            MutableDouble totalLocal,
            MutableDouble adopterLocal,
            MutableDouble shareLocal) {
        boolean logData = getEnvironment().getSettings().isLogShareNetworkLocale();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);

        logger.log(section, level,
                "{} [{}] global share = {} ({} / {}), local share = {} ({} / {})",
                InfoTag.SHARE_NETORK_LOCAL, agent.getName(),
                shareGlobal.doubleValue(), adopterGlobal.doubleValue(), totalGlobal.doubleValue(), shareLocal.doubleValue(), adopterLocal.doubleValue(), totalLocal.doubleValue()
        );
    }

    protected void logCalculateDecisionMaking(IRPLoggingMessageCollection mlm) {
        boolean logData = getEnvironment().getSettings().isLogCalculateDecisionMaking();
        mlm.setSection(getSection(logData))
                .setLevel(getLevel(logData))
                .log(getLogger(logData));
    }

    //=========================
    //util
    //=========================

    @Override
    public void handleNewProduct(Product product) {
        trace("handle new product '{}'", product.getName());
        GlobalComponentSettings.get().handleNewProduct(product, getModel());
    }

    protected void checkGroupAttributExistanceWithDefaultValues() throws ValidationException {
        checkGroupAttributExistance(
                RAConstants.NOVELTY_SEEKING,
                RAConstants.DEPENDENT_JUDGMENT_MAKING,
                RAConstants.ENVIRONMENTAL_CONCERN,
                RAConstants.CONSTRUCTION_RATE,
                RAConstants.RENOVATION_RATE,
                RAConstants.REWIRING_RATE,
                RAConstants.COMMUNICATION_FREQUENCY_SN
        );
    }

    protected void checkGroupAttributExistance(String... attrs) throws ValidationException {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(String attr: attrs) {
                checkHasAttribute(cag, attr);
            }
        }
    }

    private void checkHasAttribute(ConsumerAgentGroup cag, String name) throws ValidationException {
        if(!cag.hasGroupAttribute(name)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent group '{}' has no group attribute '{}'", cag.getName(), name);
        }
    }

    protected void checkHasDefaultSpatialInformations() throws ValidationException {
        checkHasDoubleAttributes(
                RAConstants.PURCHASE_POWER_EUR,
                RAConstants.ORIENTATION,
                RAConstants.SLOPE,
                RAConstants.SHARE_1_2_HOUSE,
                RAConstants.HOUSE_OWNER
        );
    }

    protected void checkHasDoubleAttributes(String... names) throws ValidationException {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca : cag.getAgents()) {
                checkHasDoubleAttributes(ca, names);
            }
        }
    }

    protected void checkHasDoubleAttributes(ConsumerAgent ca, String... names) throws ValidationException {
        for(String name: names) {
            checkHasDoubleAttribute(ca, name);
        }
    }

    protected void checkHasDoubleAttribute(ConsumerAgent ca, String name) throws ValidationException {
        if(!ca.hasAnyAttribute(name)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent '{}' has no attribute '{}'", name);
        }
        Attribute attr = ca.findAttribute(name);
        if(attr.isNoValueAttribute() || attr.asValueAttribute().isNoDataType(DataType.DOUBLE)) {
            throw ExceptionUtil.create(ValidationException::new, "consumer agent '{}' has no double-attribute '{}'", name);
        }
    }

    protected Uncertainty getUncertainty(ConsumerAgent ca) {
        return getModel().getUncertaintyCache().getUncertainty(ca);
    }

    protected void initNPVMatrixWithFile() {
        NPVData npvData = getNpvData();
        if(npvData == null) {
            return;
        }

        if(npvDataSupplier == null) {
            npvDataSupplier = new NPVDataSupplier(getEnvironment().getAttributeHelper());
        }

        NPVCalculator npvCalculator = new NPVCalculator();
        npvCalculator.setData(npvData);

        int firstYear = getEnvironment().getTimeModel()
                .getFirstSimulationYear();
        int lastYear = getEnvironment().getTimeModel()
                .getLastSimulationYear();

        trace("calculating npv matrix from '{}' to '{}'", firstYear, lastYear);
        for(int y = firstYear; y <= lastYear; y++) {
            trace("calculate year '{}'", y);
            NPVMatrix matrix = new NPVMatrix();
            matrix.calculate(npvCalculator, y);
            getNPVDataSupplier().put(y, matrix);
        }
    }

    protected double getAverageNPV() {
        double sum = getEnvironment().getAgents()
                .streamConsumerAgents()
                .mapToDouble(this::getNPV)
                .sum();
        return sum / getEnvironment().getAgents().getTotalNumberOfConsumerAgents();
    }

    protected double getNPV(ConsumerAgent agent) {
        return getNPV(agent, getCurrentYear());
    }

    protected double getNPV(ConsumerAgent agent, int year) {
        return getNPVDataSupplier().NPV(agent, year);
    }

    protected double getInterestPoints(
            ConsumerAgent agent,
            Product product) {
        if(isAdopter(agent, product)) {
            return getAdopterPoints();
        }
        if(isInterested(agent, product)) {
            return getInterestedPoints();
        }
        if(isAware(agent, product)) {
            return getAwarePoints();
        }
        return getUnknownPoints();
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

    protected double getInterest(ConsumerAgent agent, Product product) {
        return agent.getInterest(product);
    }

    protected double getCommunicationFrequencySN(ConsumerAgent agent) {
        return getModel().getCommunicationFrequencySN(agent);
    }

    protected double getRewiringRate(ConsumerAgent agent) {
        return getModel().getRewiringRate(agent);
    }

    protected void updateInterest(ConsumerAgent agent, Product product, double points) {
        agent.updateInterest(product, points);
    }

    protected void makeInterested(ConsumerAgent agent, Product product) {
        agent.makeInterested(product);
    }

    protected boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        return getModel().isShareOf1Or2FamilyHouse(agent);
    }

    protected boolean isHouseOwner(ConsumerAgent agent) {
        return getModel().isHouseOwner(agent);
    }

    protected double getFinancialPurchasePower(ConsumerAgent agent) {
        return getModel().getFinancialPurchasePower(agent);
    }

    protected double getEnvironmentalConcern(ConsumerAgent agent) {
        return getModel().getEnvironmentalConcern(agent);
    }

    protected double getNoveltySeeking(ConsumerAgent agent) {
        return getModel().getNoveltySeeking(agent);
    }

    protected double getDependentJudgmentMaking(ConsumerAgent agent) {
        return getModel().getDependentJudgmentMaking(agent);
    }

    protected double getFinancialThreshold(ConsumerAgent agent, Product product) {
        return getModel().getFinancialThreshold(agent, product);
    }

    protected double getAdoptionThreshold(ConsumerAgent agent, Product product) {
        return getModel().getAdoptionThreshold(agent, product);
    }

    protected double getInitialProductAwareness(ConsumerAgent agent, Product product) {
        return getModel().getInitialProductAwareness(agent, product);
    }

    protected double getInitialProductInterest(ConsumerAgent agent, Product product) {
        return getModel().getInitialProductInterest(agent, product);
    }

    protected double getInitialAdopter(ConsumerAgent agent, Product product) {
        return getModel().getInitialAdopter(agent, product);
    }

    protected Timestamp now() {
        return getEnvironment().getTimeModel().now();
    }

    protected AdoptionPhase determinePhase(Timestamp ts) {
        if(getModel().isYearChange()) {
            return AdoptionPhase.END_START;
        } else {
            if(getModel().isBeforeWeek27(ts)) {
                return AdoptionPhase.START_MID;
            } else {
                return AdoptionPhase.MID_END;
            }
        }
    }

    protected double getAverageFinancialPurchasePower() {
        return getNPVDataSupplier().getAverageFinancialPurchasePower(getEnvironment().getAgents().streamConsumerAgents());
    }
}
