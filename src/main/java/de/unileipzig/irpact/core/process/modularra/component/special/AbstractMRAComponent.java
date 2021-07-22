package de.unileipzig.irpact.core.process.modularra.component.special;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.Acting;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.InfoTag;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.modularra.AgentData;
import de.unileipzig.irpact.core.process.modularra.component.generic.AbstractComponent;
import de.unileipzig.irpact.core.process.modularra.component.generic.ComponentType;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractMRAComponent extends AbstractComponent implements LoggingHelper {

    protected final Predicate<SocialGraph.Node> IS_CONSUMER = node -> node.is(ConsumerAgent.class);

    protected AbstractMRAComponent(ComponentType type) {
        super(type);
    }

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

    protected ProcessPlanResult doAction(ConsumerAgent agent, AgentData data) {
        trace("[{}] doAction", agent.getName());

        agent.allowAttention();

        if(doCommunicate(agent, data)) {
            return communicate(agent, data);
        }

        if(doRewire(agent, data)) {
            return rewire(agent, data);
        }

        return nop(agent, data);
    }

    protected final boolean doCommunicate(ConsumerAgent agent, AgentData data) {
        double r = data.getRnd().nextDouble();
        double freq = getCommunicationFrequencySN(agent, data);
        boolean doCommunicate = r < freq;
        trace("[{}] do communicate: {} ({} < {})", agent.getName(), doCommunicate, r, freq);
        return doCommunicate;
    }

    protected ProcessPlanResult communicate(ConsumerAgent agent, AgentData data) {
        trace("[{}] start communication", agent.getName());

        SocialGraph graph = data.getGraph();
        SocialGraph.Node node = agent.getSocialGraphNode();
        //create random target order
        List<SocialGraph.Node> targetList = new ArrayList<>();
        graph.getTargets(node, SocialGraph.Type.COMMUNICATION, targetList);
        Collections.shuffle(targetList, data.getRandom());

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

                        updateInterest(agent, data, targetAgent);
                        updateCommunicationGraph(agent, data, graph, targetNode);
                        applyRelativeAgreement(agent, data, targetAgent);

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

    protected final boolean doRewire(ConsumerAgent agent, AgentData data) {
        double r = data.getRnd().nextDouble();
        double freq = getRewiringRate(agent, data);
        boolean doRewire = r < freq;
        trace("[{}] do rewire: {} ({} < {})", agent.getName(), doRewire, r, freq);
        return doRewire;
    }

    protected ProcessPlanResult rewire(ConsumerAgent agent, AgentData data) {
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

            SocialGraph graph = data.getGraph();
            SocialGraph.Node node = agent.getSocialGraphNode();
            SocialGraph.LinkageInformation linkInfo = graph.getLinkageInformation(node);

            //remove
            SocialGraph.Node rndTarget = graph.getRandomTarget(node, SocialGraph.Type.COMMUNICATION, data.getRnd());
            if(rndTarget == null) {
                logGraphUpdateEdgeRemoved(agent, data, null);
            } else {
                ConsumerAgentGroup tarCag = rndTarget.getAgent(ConsumerAgent.class).getGroup();
                SocialGraph.Edge edge = graph.getEdge(node, rndTarget, SocialGraph.Type.COMMUNICATION);
                graph.removeEdge(edge);
                logGraphUpdateEdgeRemoved(agent, data, rndTarget.getAgent());
                updateLinkCount(agent, linkInfo, tarCag, -1); //dec
            }

            ConsumerAgentGroupAffinities affinities = data.getAgents()
                    .getConsumerAgentGroupAffinityMapping()
                    .get(agent.getGroup());

            SocialGraph.Node tarNode = null;
            ConsumerAgentGroup tarCag = null;
            while(tarNode == null) {
                if(affinities.isEmpty()) {
                    tarCag = null;
                    break;
                }

                tarCag = affinities.getWeightedRandom(data.getRnd());

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
                        agent, data,
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
                logGraphUpdateEdgeAdded(agent, data, tarNode.getAgent());
                updateLinkCount(agent, linkInfo, tarCag, 1); //inc
            }

            return ProcessPlanResult.IN_PROCESS;
        } finally {
            agent.releaseDataAccess();
        }
    }

    protected ProcessPlanResult nop(ConsumerAgent agent, @SuppressWarnings("unused") AgentData data) {
        trace("[{}] nop", agent.getName());
        return ProcessPlanResult.IN_PROCESS;
    }

    @Todo
    protected final void applyRelativeAgreement(ConsumerAgent agent, AgentData data, ConsumerAgent target) {
        applyRelativeAgreement(agent, target, RAConstants.NOVELTY_SEEKING, data);
        applyRelativeAgreement(agent, target, RAConstants.DEPENDENT_JUDGMENT_MAKING, data);
        applyRelativeAgreement(agent, target, RAConstants.ENVIRONMENTAL_CONCERN, data);
    }

    @Todo
    protected final void applyRelativeAgreement(ConsumerAgent agent, ConsumerAgent target, String attrName, AgentData data) {
        ConsumerAgentAttribute opinionThis = agent.getAttribute(attrName);
        Uncertainty uncertaintyThis = data.getUncertainty();
        ConsumerAgentAttribute opinionTarget = target.getAttribute(attrName);
        Uncertainty uncertaintyTarget = data.getModel().getUncertainty(target);
        if(uncertaintyTarget == null) {
            warn("agent '{}' has no uncertainty - skip", target.getName());
        } else {
            data.getRelativeAgreementAlgorithm().apply(
                    agent.getName(),
                    opinionThis,
                    uncertaintyThis,
                    target.getName(),
                    opinionTarget,
                    uncertaintyTarget
            );
        }
    }

    protected final void updateInterest(ConsumerAgent agent, AgentData data, ConsumerAgent targetAgent) {
        double myInterest = getInterest(agent, data.getProduct());
        double targetInterest = getInterest(targetAgent, data.getProduct());

        double myPointsToAdd = getInterestPoints(agent, data.getModelData(), data.getProduct());
        double targetPointsToAdd = getInterestPoints(targetAgent, data.getModelData(), data.getProduct());

        updateInterest(agent, data.getProduct(), targetPointsToAdd);
        updateInterest(targetAgent, data.getProduct(), myPointsToAdd);

        logInterestUpdate(agent, data, myInterest, getInterest(agent, data.getProduct()), targetAgent, targetInterest, getInterest(targetAgent, data.getProduct()));
    }

    protected final void updateCommunicationGraph(ConsumerAgent agent, AgentData data, SocialGraph graph, SocialGraph.Node target) {
        if(!graph.hasEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
            graph.addEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION, 1.0);
            logGraphUpdateEdgeAdded(agent, data, target.getAgent());
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
            ConsumerAgent agent, AgentData data,
            SocialGraph.Node srcNode,
            ConsumerAgentGroup tarCag,
            int unlinkedInCag,
            SocialGraph.Type type) {
        SocialGraph graph = data.getGraph();

        int rndId = data.getRnd().nextInt(unlinkedInCag);
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

    protected double getFinancialComponent(ConsumerAgent agent, AgentData data) {
        double npvAvg = getAverageNPV(data);
        double npvThis = getNPV(agent, data);

        return getFinancialComponent(agent, data, npvAvg, npvThis);
    }

    protected double getFinancialComponent(ConsumerAgent agent, AgentData data, double npvAvg, double npvThis) {
        double ftAvg = getAverageFinancialThresholdAgent(data);
        double ftThis = getFinancialThresholdAgent(agent, data);

        double ft = getLogisticFactor(data) * (ftThis - ftAvg);
        double npv = getLogisticFactor(data) * (npvThis - npvAvg);

        double logisticFt = MathUtil.logistic(ft);
        double logisticNpv = MathUtil.logistic(npv);

        double comp = (logisticFt + logisticNpv) / 2.0;

        logFinancialComponent(agent, data, ftAvg, ftThis, npvAvg, npvThis, getLogisticFactor(data), ft, npv, logisticFt, logisticNpv, comp);

        return comp;
    }

    protected double getEnvironmentalComponent(ConsumerAgent agent, AgentData data) {
        return getEnvironmentalConcern(agent, data);
    }

    protected double getNoveltyCompoenent(ConsumerAgent agent, AgentData data) {
        return getNoveltySeeking(agent, data);
    }

    protected double getSocialComponent(ConsumerAgent agent, AgentData data, NodeFilter filter) {
        MutableDouble shareOfAdopterInSocialNetwork = MutableDouble.zero();
        MutableDouble shareOfAdopterInLocalArea = MutableDouble.zero();
        getShareOfAdopterInSocialNetworkAndLocalArea(agent, data, filter, shareOfAdopterInSocialNetwork, shareOfAdopterInLocalArea);
        double djm = getDependentJudgmentMaking(agent, data);
        double comp = djm * (shareOfAdopterInSocialNetwork.get() + shareOfAdopterInLocalArea.get()) / 2.0;
        trace(
                "[{}] dependent judgment making = {}, share of adopter in social network = {}, share of adopter in local area = {}, social component = {} = {} * ({} + {}) / 2.0",
                agent.getName(), djm, shareOfAdopterInSocialNetwork.get(), shareOfAdopterInLocalArea.get(), comp, djm, shareOfAdopterInSocialNetwork.get(), shareOfAdopterInLocalArea.get()
        );
        return comp;
    }

    protected void getShareOfAdopterInSocialNetworkAndLocalArea(
            ConsumerAgent agent,
            AgentData data,
            NodeFilter filter,
            MutableDouble global,
            MutableDouble local) {
        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();
        MutableDouble totalLocal = MutableDouble.zero();
        MutableDouble adopterLocal = MutableDouble.zero();

        data.getGraph()
                .streamSourcesAndTargets(agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)
                .filter(IS_CONSUMER)
                .distinct()
                .forEach(globalNode -> {
                    totalGlobal.inc();
                    if(globalNode.getAgent(ConsumerAgent.class).hasAdopted(data.getProduct())) {
                        adopterGlobal.inc();
                    }
                });

        //TODO vllt ergebnisse cachen
        //-> selbiges dann auch gleich bei obrigen
        data.getGraph()
                .streamNodes()
                .filter(IS_CONSUMER)
                .filter(filter)
                .forEach(localNode -> {
                    totalLocal.inc();
                    if(localNode.getAgent(ConsumerAgent.class).hasAdopted(data.getProduct())) {
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

        logShareOfAdopterInSocialNetworkAndLocalArea(agent, data, totalGlobal, adopterGlobal, global, totalLocal, adopterLocal, local);
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

    protected final void logGraphUpdateEdgeAdded(Agent agent, AgentData data, Agent target) {
        boolean logData = data.getSettings().isLogGraphUpdate();
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
            Agent agent, AgentData data,
            double myOldPoints, double myNewPoints,
            Agent target, double targetOldPoints, double targetNewPoints) {
        boolean logData = data.getSettings().isLogInterestUpdate();
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

    protected void logGraphUpdateEdgeRemoved(Agent agent, AgentData data, Agent target) {
        boolean logData = data.getSettings().isLogGraphUpdate();
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
            ConsumerAgent agent, AgentData data,
            double ftAvg, double ftThis,
            double npvAvg, double npvThis,
            double logisticFactor,
            double ft, double npv,
            double logisticFt, double logisticNpv,
            double comp) {
        boolean logData = data.getSettings().isLogFinancialComponent();
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
            ConsumerAgent agent, AgentData data,
            MutableDouble totalGlobal,
            MutableDouble adopterGlobal,
            MutableDouble shareGlobal,
            MutableDouble totalLocal,
            MutableDouble adopterLocal,
            MutableDouble shareLocal) {
        boolean logData = data.getSettings().isLogShareNetworkLocale();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);

        logger.log(section, level,
                "{} [{}] global share = {} ({} / {}), local share = {} ({} / {})",
                InfoTag.SHARE_NETORK_LOCAL, agent.getName(),
                shareGlobal.doubleValue(), adopterGlobal.doubleValue(), totalGlobal.doubleValue(), shareLocal.doubleValue(), adopterLocal.doubleValue(), totalLocal.doubleValue()
        );
    }

    //=========================
    //util
    //=========================

    protected static double getLogisticFactor(AgentData data) {
        return data.getModelData().getLogisticFactor();
    }

    protected static double getAverageNPV(AgentData data) {
        double sum = data.getAgents()
                .streamConsumerAgents()
                .mapToDouble(agent -> getNPV(agent, data))
                .sum();
        return sum / data.getAgents().getTotalNumberOfConsumerAgents();
    }

    protected static double getNPV(ConsumerAgent agent, AgentData data) {
        return getNPV(agent, data, data.getCurrentYear());
    }

    protected static double getNPV(ConsumerAgent agent, AgentData data, int year) {
        return data.getModelData().NPV(agent, year);
    }

    protected static double getInterestPoints(ConsumerAgent agent, RAModelData data, Product product) {
        if(isAdopter(agent, product)) {
            return data.getAdopterPoints();
        }
        if(isInterested(agent, product)) {
            return data.getInterestedPoints();
        }
        if(isAware(agent, product)) {
            return data.getAwarePoints();
        }
        return data.getUnknownPoints();
    }

    protected static boolean isAdopter(ConsumerAgent agent, Product product) {
        return agent.hasAdopted(product);
    }

    protected static boolean isInterested(ConsumerAgent agent, Product product) {
        return agent.isInterested(product);
    }

    protected static boolean isAware(ConsumerAgent agent, Product product) {
        return agent.isAware(product);
    }

    protected static double getInterest(ConsumerAgent agent, Product product) {
        return agent.getInterest(product);
    }

    protected static double getCommunicationFrequencySN(ConsumerAgent agent, AgentData data) {
        return data.getModel().getCommunicationFrequencySN(agent);
    }

    protected static double getRewiringRate(ConsumerAgent agent, AgentData data) {
        return data.getModel().getRewiringRate(agent);
    }

    protected void updateInterest(ConsumerAgent agent, Product product, double points) {
        agent.updateInterest(product, points);
    }

    protected static void makeInterested(ConsumerAgent agent, Product product) {
        agent.makeInterested(product);
    }

    protected static boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent, AgentData data) {
        return data.getModel().isShareOf1Or2FamilyHouse(agent);
    }

    protected static boolean isHouseOwner(ConsumerAgent agent, AgentData data) {
        return data.getModel().isHouseOwner(agent);
    }

    protected static double getFinancialThreshold(ConsumerAgent agent, AgentData data) {
        return data.getModel().getFinancialThreshold(agent, data.getProduct());
    }

    protected static double getAdoptionThreshold(ConsumerAgent agent, AgentData data) {
        return data.getModel().getAdoptionThreshold(agent, data.getProduct());
    }

    protected static double getFinancialThresholdAgent(ConsumerAgent agent, AgentData data) {
        return data.getModel().getFinancialThreshold(agent);
    }

    protected static double getEnvironmentalConcern(ConsumerAgent agent, AgentData data) {
        return data.getModel().getEnvironmentalConcern(agent);
    }

    protected static double getNoveltySeeking(ConsumerAgent agent, AgentData data) {
        return data.getModel().getNoveltySeeking(agent);
    }

    protected static double getDependentJudgmentMaking(ConsumerAgent agent, AgentData data) {
        return data.getModel().getDependentJudgmentMaking(agent);
    }

    protected static Timestamp now(AgentData data) {
        return data.getTimeModel().now();
    }

    protected static AdoptionPhase determinePhase(AgentData data, Timestamp ts) {
        if(data.getModel().isYearChange()) {
            return AdoptionPhase.END_START;
        } else {
            if(data.getModel().isBeforeWeek27(ts)) {
                return AdoptionPhase.START_MID;
            } else {
                return AdoptionPhase.MID_END;
            }
        }
    }

    protected static double getAverageFinancialThresholdAgent(AgentData data) {
        return data.getModelData().getAverageFinancialThresholdAgent(data.getAgents().streamConsumerAgents());
    }
}
