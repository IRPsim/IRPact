package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import de.unileipzig.irpact.commons.util.data.weighted.RemoveOnDrawUnweightListMapping;
import de.unileipzig.irpact.commons.util.data.weighted.RemoveOnDrawWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class FreeNetworkTopology2 extends NameableBase implements GraphTopologyScheme {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FreeNetworkTopology2.class);

    protected SocialGraph.Type edgeType;
    protected Map<ConsumerAgentGroup, Integer> edgeCountMap;
    protected ConsumerAgentGroupAffinityMapping affinityMapping;
    protected DistanceEvaluator distanceEvaluator;
    protected double initialWeight;
    protected Rnd rnd;

    public FreeNetworkTopology2() {
    }

    public void setEdgeType(SocialGraph.Type edgeType) {
        this.edgeType = edgeType;
    }

    public SocialGraph.Type getEdgeType() {
        return edgeType;
    }

    public void setEdgeCountMap(Map<ConsumerAgentGroup, Integer> edgeCountMap) {
        this.edgeCountMap = edgeCountMap;
    }

    public Map<ConsumerAgentGroup, Integer> getEdgeCountMap() {
        return edgeCountMap;
    }

    public void setAffinityMapping(ConsumerAgentGroupAffinityMapping affinityMapping) {
        this.affinityMapping = affinityMapping;
    }

    public ConsumerAgentGroupAffinityMapping getAffinityMapping() {
        return affinityMapping;
    }

    public void setDistanceEvaluator(DistanceEvaluator distanceEvaluator) {
        this.distanceEvaluator = distanceEvaluator;
    }

    public DistanceEvaluator getDistanceEvaluator() {
        return distanceEvaluator;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                getInitialWeight(),
                getRnd().getChecksum(),
                getDistanceEvaluator().getChecksum(),
                getEdgeType().getChecksum(),
                getAffinityMapping().getChecksum(),
                ChecksumComparable.getMapChecksum(getEdgeCountMap())
        );
    }

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) throws InitializationException {
        LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "initialize free network graph");
        for(SocialGraph.Node node: graph.getNodes()) {
            SocialGraph.LinkageInformation li = graph.getLinkageInformation(node);
            ConsumerAgent ca = node.getAgent(ConsumerAgent.class);
            Collection<ConsumerAgent> agents = drawTargets(environment, ca, li);
            for(ConsumerAgent targetCa: agents) {
                LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "add edge: {}->{} ({},{})", ca.getName(), targetCa.getName(), edgeType, initialWeight);
                graph.addEdge(ca.getSocialGraphNode(), targetCa.getSocialGraphNode(), edgeType, initialWeight);
                li.inc(targetCa.getGroup(), edgeType);
            }
        }
    }

    protected Collection<ConsumerAgent> drawTargets(
            SimulationEnvironment environment,
            ConsumerAgent source,
            SocialGraph.LinkageInformation li) throws InitializationException {
        ConsumerAgentGroup sourceCag = source.getGroup();
        ConsumerAgentGroupAffinities sourceAffinities = affinityMapping.get(sourceCag);
        int edgeCount = edgeCountMap.get(sourceCag);
        CagOrder cagOrder = new CagOrder();
        cagOrder.determine(edgeCount, sourceAffinities, rnd);
        cagOrder.apply(li, edgeType);

        List<ConsumerAgent> targetList = new ArrayList<>();
        for(ConsumerAgentGroup targetCag: cagOrder.getLinkCounter().keys()) {
            int targetCount = cagOrder.getLinkCounter().get(targetCag);
            WeightedMapping<ConsumerAgent> targetWM = calculateDistanceMapping(environment, source, targetCag);
            for(int i = 0; i < targetCount; i++) {
                ConsumerAgent target = targetWM.getWeightedRandom(rnd);
                targetList.add(target);
            }
        }
        return targetList;
    }

    protected WeightedMapping<ConsumerAgent> calculateDistanceMapping(
            SimulationEnvironment environment,
            ConsumerAgent source,
            ConsumerAgentGroup targetCag) {
        if(distanceEvaluator.isDisabled()) {
            RemoveOnDrawUnweightListMapping<ConsumerAgent> mapping = new RemoveOnDrawUnweightListMapping<>();
            for(ConsumerAgent target: targetCag.getAgents()) {
                mapping.add(target);
            }
            mapping.shuffle(rnd);
            mapping.setRemoveFrist(true);
            return mapping;
        } else {
            RemoveOnDrawWeightedMapping<ConsumerAgent> mapping = new RemoveOnDrawWeightedMapping<>();
            SpatialModel spatialModel = environment.getSpatialModel();
            for(ConsumerAgent target: targetCag.getAgents()) {
                double distance = spatialModel.distance(source.getSpatialInformation(), target.getSpatialInformation());
                double influence = distanceEvaluator.evaluate(distance);
                mapping.set(target, influence);
            }
            mapping.normalize();
            return mapping;
        }
    }

    /**
     * @author Daniel Abitz
     */
    protected static class CagOrder {

        protected DataCounter<ConsumerAgentGroup> linkCounter = new DataCounter<>();

        protected CagOrder() {
        }

        protected boolean isMax(ConsumerAgentGroup cag) {
            int numberOfAgents = cag.getNumberOfAgents();
            int current = linkCounter.get(cag);
            return current >= numberOfAgents;
        }

        protected void determine(
                int count,
                ConsumerAgentGroupAffinities affinities,
                Rnd rnd) throws InitializationException {
            boolean tryAgain = true;
            ConsumerAgentGroupAffinities caga = affinities;
            ConsumerAgentGroup target;
            int found = 0;
            while(tryAgain) {
                target = caga.getWeightedRandom(rnd);

                if(isMax(target)) {
                    caga = caga.createWithout(target);
                    if(caga.isEmpty()) {
                        throw ExceptionUtil.create(
                                InitializationException::new,
                                "not enough agents, agents found: {}, required: {}",
                                found, count
                        );
                    }
                    tryAgain = true;
                } else {
                    found++;
                    linkCounter.inc(target);
                    tryAgain = found < count;
                }
            }
        }

        protected void apply(SocialGraph.LinkageInformation li, SocialGraph.Type edgeType) {
            for(ConsumerAgentGroup cag: linkCounter.keys()) {
                int count = linkCounter.get(cag);
                li.set(cag, edgeType, count);
            }
        }

        protected DataCounter<ConsumerAgentGroup> getLinkCounter() {
            return linkCounter;
        }
    }
}
