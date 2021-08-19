package de.unileipzig.irpact.core.process.modular.ca.components.base;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.agent.Acting;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.alg.RelativeAgreementAlgorithm;
import de.unileipzig.irpact.core.process.ra.uncert.BasicUncertaintyManager;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyCache;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyHandler;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractActionModule extends AbstractConsumerAgentModule {

    protected static final double DEFAULT_WEIGHT = 1.0;
    protected static final SocialGraph.Type ACTION_EDGE_TYPE = SocialGraph.Type.COMMUNICATION;

    public AbstractActionModule() {
    }

    //=========================
    //param
    //=========================


    @Override
    public void setEnvironment(SimulationEnvironment environment) {
        super.setEnvironment(environment);
        getUncertaintyHandler().setEnvironment(environment);
    }

    protected int adopterPoints;
    public int getAdopterPoints() {
        return adopterPoints;
    }
    public void setAdopterPoints(int adopterPoints) {
        this.adopterPoints = adopterPoints;
    }

    protected int interestedPoints;
    public int getInterestedPoints() {
        return interestedPoints;
    }
    public void setInterestedPoints(int interestedPoints) {
        this.interestedPoints = interestedPoints;
    }

    protected int awarePoints;
    public int getAwarePoints() {
        return awarePoints;
    }
    public void setAwarePoints(int awarePoints) {
        this.awarePoints = awarePoints;
    }

    protected int unknownPoints;
    public int getUnknownPoints() {
        return unknownPoints;
    }
    public void setUnknownPoints(int unknownPoints) {
        this.unknownPoints = unknownPoints;
    }

    protected RelativeAgreementAlgorithm relativeAgreementAlgorithm;
    public void setRelativeAgreementAlgorithm(RelativeAgreementAlgorithm relativeAgreementAlgorithm) {
        this.relativeAgreementAlgorithm = relativeAgreementAlgorithm;
    }
    public RelativeAgreementAlgorithm getRelativeAgreementAlgorithm() {
        return relativeAgreementAlgorithm;
    }

    protected UncertaintyHandler uncertaintyHandler = newHandler();
    protected static UncertaintyHandler newHandler() {
        UncertaintyHandler handler = new UncertaintyHandler();
        handler.setCache(new UncertaintyCache());
        handler.setManager(new BasicUncertaintyManager());
        return handler;
    }
    public UncertaintyHandler getUncertaintyHandler() {
        return uncertaintyHandler;
    }

    //=========================
    //core
    //=========================

    protected AdoptionResult doAction(ConsumerAgentData data) throws Throwable {
        data.getAgent().allowAttention();

        if(doCommunicate(data)) {
            return communicate(data);
        }

        if(doRewire(data)) {
            return rewire(data);
        }

        return nop(data);
    }

    protected boolean doCommunicate(ConsumerAgentData data) {
        double r = nextDouble(data);
        double freq = getCommunicationFrequencySN(data.getAgent());
        boolean doCommunicate = r < freq;
        trace("[{}] do communicate: {} ({} < {})", data.getAgent().getName(), doCommunicate, r, freq);
        return doCommunicate;
    }

    protected AdoptionResult communicate(ConsumerAgentData data) {
        ConsumerAgent agent = data.getAgent();
        SocialGraph graph = getGraph();
        SocialGraph.Node node = agent.getSocialGraphNode();

        List<SocialGraph.Node> targetList = new ArrayList<>();
        graph.getTargets(node, ACTION_EDGE_TYPE, targetList);
        Collections.shuffle(targetList, getRandom(data));

        for(SocialGraph.Node targetNode: targetList) {
            ConsumerAgent targetAgent = targetNode.getAgent(ConsumerAgent.class);

            Acting.AttentionResult result = Acting.aquireDoubleSidedAttentionAndDataAccess(agent, targetAgent);
            switch (result) {
                case SELF_OCCUPIED:
                    return AdoptionResult.IN_PROCESS;

                case TARGET_OCCUPIED:
                    continue;

                case SUCCESS:
                    agent.actionPerformed();
                    targetAgent.actionPerformed();
                    agent.releaseAttention();
                    targetAgent.releaseAttention();

                    //nur hier haben wir datalock durch aquireAttentionAndDataAccess
                    try {
                        updateInterest(agent, targetAgent, data);
                        updateCommunicationGraph(agent, targetNode, graph);
                        applyRelativeAgreement(agent, targetAgent);

                        return AdoptionResult.IN_PROCESS;
                    } finally {
                        agent.releaseDataAccess();
                        targetAgent.releaseDataAccess();
                    }
            }
        }

        return AdoptionResult.IN_PROCESS;
    }

    protected void updateInterest(ConsumerAgent source, ConsumerAgent target, ConsumerAgentData data) {
        double myPointsToAdd = getInterestPoints(source, data.getProduct());
        double targetPointsToAdd = getInterestPoints(target, data.getProduct());

        updateInterest(source, data.getProduct(), targetPointsToAdd);
        updateInterest(target, data.getProduct(), myPointsToAdd);
    }

    protected void updateCommunicationGraph(ConsumerAgent agent, SocialGraph.Node target, SocialGraph graph) {
        if(!graph.hasEdge(target, agent.getSocialGraphNode(), ACTION_EDGE_TYPE)) {
            graph.addEdge(target, agent.getSocialGraphNode(), ACTION_EDGE_TYPE, DEFAULT_WEIGHT);
        }
    }

    protected void applyRelativeAgreement(ConsumerAgent agent, ConsumerAgent target) {
        applyRelativeAgreement(agent, target, RAConstants.NOVELTY_SEEKING);
        applyRelativeAgreement(agent, target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
        applyRelativeAgreement(agent, target, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    protected void applyRelativeAgreement(ConsumerAgent agent, ConsumerAgent target, String attrName) {
        ConsumerAgentAttribute opinionThis = agent.getAttribute(attrName);
        Uncertainty uncertaintyThis = getUncertainty(agent);
        ConsumerAgentAttribute opinionTarget = target.getAttribute(attrName);
        Uncertainty uncertaintyTarget = getUncertainty(target);
        if(uncertaintyTarget == null) {
            warn("agent '{}' has no uncertainty - skip", target.getName());
        } else {
            getRelativeAgreementAlgorithm().apply(
                    agent.getName(),
                    opinionThis,
                    uncertaintyThis,
                    target.getName(),
                    opinionTarget,
                    uncertaintyTarget
            );
        }
    }

    protected boolean doRewire(ConsumerAgentData data) {
        double r = nextDouble(data);
        double freq = getRewiringRate(data.getAgent());
        boolean doRewire = r < freq;
        trace("[{}] do rewire: {} ({} < {})", data.getAgent().getName(), doRewire, r, freq);
        return doRewire;
    }

    protected AdoptionResult rewire(ConsumerAgentData data) {
        ConsumerAgent agent = data.getAgent();

        if(agent.tryAquireAttention()) {
            agent.actionPerformed();
            agent.aquireDataAccess(); //lock data
            agent.releaseAttention();
        } else {
            return AdoptionResult.IN_PROCESS;
        }

        try {
            SocialGraph graph = getGraph();
            SocialGraph.Node node = agent.getSocialGraphNode();
            SocialGraph.LinkageInformation linkInfo = graph.getLinkageInformation(node);

            //remove
            SocialGraph.Node rndTarget = graph.getRandomTarget(node, ACTION_EDGE_TYPE, data.rnd());
            if(rndTarget != null) {
                ConsumerAgentGroup tarCag = rndTarget.getAgent(ConsumerAgent.class).getGroup();
                SocialGraph.Edge edge = graph.getEdge(node, rndTarget, ACTION_EDGE_TYPE);
                graph.removeEdge(edge);
                updateLinkCount(linkInfo, tarCag, -1);
            }

            ConsumerAgentGroupAffinities affinities = getAffinities(agent);
            SocialGraph.Node tarNode = null;
            ConsumerAgentGroup tarCag = null;

            while(tarNode == null) {
                if(affinities.isEmpty()) {
                    tarCag = null;
                    break;
                }

                tarCag = affinities.getWeightedRandom(data.rnd());

                int currentLinkCount = linkInfo.get(tarCag, ACTION_EDGE_TYPE);
                int maxTargetAgents = tarCag.getNumberOfAgents();
                if(tarCag == agent.getGroup()) {
                    maxTargetAgents -= 1;
                }

                if(maxTargetAgents == currentLinkCount) {
                    affinities = affinities.createWithout(tarCag);
                    continue;
                }

                int unlinkedCount = maxTargetAgents - currentLinkCount;

                tarNode = getRandomUnlinked(
                        data,
                        node,
                        tarCag,
                        unlinkedCount
                );
            }

            if(tarNode != null) {
                graph.addEdge(node, tarNode, ACTION_EDGE_TYPE, DEFAULT_WEIGHT);
                updateLinkCount(linkInfo, tarCag, 1);
            }

            return AdoptionResult.IN_PROCESS;
        } finally {
            agent.releaseDataAccess();
        }
    }

    protected void updateLinkCount(
            SocialGraph.LinkageInformation linkInfo,
            ConsumerAgentGroup tarCag,
            int delta) {
        linkInfo.update(tarCag, ACTION_EDGE_TYPE, delta);
    }

    protected SocialGraph.Node getRandomUnlinked(
            ConsumerAgentData data,
            SocialGraph.Node srcNode,
            ConsumerAgentGroup tarCag,
            int unlinkedCount) {
        SocialGraph graph = getGraph();

        int rndId = data.rnd().nextInt(unlinkedCount);
        int id = 0;

        for(Agent tar: tarCag.getAgents()) {
            if(tar == data.getAgent()) {
                continue;
            }

            SocialGraph.Node tarNode = tar.getSocialGraphNode();
            if(graph.hasNoEdge(srcNode, tarNode, ACTION_EDGE_TYPE)) {
                if(id == rndId) {
                    return tarNode;
                } else {
                    id += 1;
                }
            }
        }

        return null;
    }

    protected AdoptionResult nop(ConsumerAgentData data) {
        return AdoptionResult.IN_PROCESS;
    }

    //=========================
    //util
    //=========================

    protected int getPartialChecksum() {
        return Checksums.SMART.getChecksum(
                getAdopterPoints(),
                getInterestedPoints(),
                getAwarePoints(),
                getUnknownPoints(),
                getRelativeAgreementAlgorithm(),
                getUncertaintyHandler().getManager()
        );
    }

    protected Uncertainty getUncertainty(ConsumerAgent agent) {
        UncertaintyHandler handler = getUncertaintyHandler();
        if(handler == null) {
            throw new NullPointerException("UncertaintyHandler");
        }
        UncertaintyCache cache = handler.getCache();
        if(cache == null) {
            throw new NullPointerException("UncertaintyCache");
        }
        return cache.getUncertainty(agent);
    }

    protected double getInterestPoints(ConsumerAgent agent, Product product) {
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
}
