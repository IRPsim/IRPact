package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.log.LazyPrinter;
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
public class FreeNetworkTopology extends NameableBase implements GraphTopologyScheme {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FreeNetworkTopology.class);

    protected SocialGraph.Type edgeType;
    protected Map<ConsumerAgentGroup, Integer> edgeCountMap;
    protected ConsumerAgentGroupAffinityMapping affinityMapping;
    protected DistanceEvaluator distanceEvaluator;
    protected double initialWeight;
    protected Rnd rnd;
    protected boolean selfReferential;
    protected boolean allowLessEdges;

    public FreeNetworkTopology() {
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

    public void setSelfReferential(boolean selfReferential) {
        this.selfReferential = selfReferential;
    }

    public boolean isSelfReferential() {
        return selfReferential;
    }

    public void setAllowLessEdges(boolean allowLessEdges) {
        this.allowLessEdges = allowLessEdges;
    }

    public boolean isAllowLessEdges() {
        return allowLessEdges;
    }

    protected Map<ConsumerAgentGroup, List<ConsumerAgent>> targetCache = new HashMap<>();
    protected List<ConsumerAgent> getList(ConsumerAgentGroup cag) {
        if(targetCache.containsKey(cag)) {
            return targetCache.get(cag);
        } else {
            List<ConsumerAgent> list = new ArrayList<>(cag.getAgents());
            targetCache.put(cag, list);
            return list;
        }
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                getInitialWeight(),
                getRnd().getChecksum(),
                getDistanceEvaluator().getChecksum(),
                getEdgeType().getChecksum(),
                getAffinityMapping().getChecksum(),
                ChecksumComparable.getMapChecksum(getEdgeCountMap()),
                isSelfReferential()
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
        targetCache.clear();
        System.gc();
    }

    protected Collection<ConsumerAgent> drawTargets(
            SimulationEnvironment environment,
            ConsumerAgent source,
            SocialGraph.LinkageInformation li) throws InitializationException {
        ConsumerAgentGroup sourceCag = source.getGroup();
        ConsumerAgentGroupAffinities sourceAffinities = affinityMapping.get(sourceCag);
        int edgeCount = edgeCountMap.get(sourceCag);
        CagOrder cagOrder = new CagOrder();
        if(!selfReferential) {
            cagOrder.setSkipSelfReferentialGroup(sourceCag);
        }
        cagOrder.determine(edgeCount, sourceAffinities, allowLessEdges, rnd);
        LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "edge targets for '{}': {}", source.getName(), LazyPrinter.of(cagOrder::printEdgeTargets));
        cagOrder.apply(li, edgeType);

        List<ConsumerAgent> targetList = new ArrayList<>();
        for(ConsumerAgentGroup targetCag: cagOrder.getLinkCounter().keys()) {
            int targetCount = cagOrder.getLinkCounter().get(targetCag);
            WeightedMapping<ConsumerAgent> targetWM = calculateDistanceMapping(environment, source, targetCag, targetCount);
            int i = 0;
            while(i < targetCount) {
                ConsumerAgent target = targetWM.getWeightedRandom(rnd);
                if(!selfReferential && source == target) {
                    continue;
                }
                targetList.add(target);
                i++;
            }
        }
        return targetList;
    }

    protected WeightedMapping<ConsumerAgent> calculateDistanceMapping(
            SimulationEnvironment environment,
            ConsumerAgent source,
            ConsumerAgentGroup targetCag,
            int targetCount) {
        if(distanceEvaluator.isDisabled()) {
            RemoveOnDrawUnweightListMapping<ConsumerAgent> mapping = new RemoveOnDrawUnweightListMapping<>();
            List<ConsumerAgent> list = getList(targetCag);
            Set<ConsumerAgent> cache = new HashSet<>();
            while(cache.size() < targetCount) {
                ConsumerAgent target = rnd.getRandom(list);
                if(!selfReferential && source == target) {
                    continue;
                }
                if(cache.add(target)) {
                    mapping.add(target);
                }
            }
//            //v1
//            Set<ConsumerAgent> cache = new HashSet<>();
//            while(cache.size() < targetCount) {
//                ConsumerAgent target = rnd.getRandom(targetCag.getAgents());
//                if(!selfReferential && source == target) {
//                    continue;
//                }
//                if(cache.add(target)) {
//                    mapping.add(target);
//                }
//            }
//            //v2
//            for(ConsumerAgent target: targetCag.getAgents()) {
//                mapping.add(target);
//            }
//            mapping.shuffle(rnd);
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
        protected ConsumerAgentGroup skipSelfReferentialGroup;

        protected CagOrder() {
        }

        //Falls diese Gruppe gesetzt ist, wird ein Element uebersprungen, um die Selbstreferenzierung zu verhindern.
        public void setSkipSelfReferentialGroup(ConsumerAgentGroup skipSelfReferentialGroup) {
            this.skipSelfReferentialGroup = skipSelfReferentialGroup;
        }

        protected boolean isMax(ConsumerAgentGroup cag) {
            int skipSelf = cag == skipSelfReferentialGroup ? 1 : 0;
            int numberOfAgents = cag.getNumberOfAgents() - skipSelf;
            int current = linkCounter.get(cag);
            return current >= numberOfAgents;
        }

        protected void determine(
                int count,
                ConsumerAgentGroupAffinities affinities,
                boolean allowLessAgents,
                Rnd rnd) throws InitializationException {
            ConsumerAgentGroupAffinities caga = affinities;
            ConsumerAgentGroup target;
            int found = 0;
            boolean tryAgain = true;
            while(tryAgain) {
                target = caga.getWeightedRandom(rnd);

                if(isMax(target)) {
                    caga = caga.createWithout(target);
                    if(caga.isEmpty()) {
                        if(allowLessAgents) {
                            LOGGER.info("[allowLessAgents] not enough agents, agents found: {}, required: {}", found, count);
                            return;
                        } else {
                            throw ExceptionUtil.create(
                                    InitializationException::new,
                                    "not enough agents, agents found: {}, required: {}",
                                    found, count
                            );
                        }
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

        protected String printEdgeTargets() {
            Map<String, String> printMap = new LinkedHashMap<>();
            for(ConsumerAgentGroup cag: linkCounter.keys()) {
                printMap.put(cag.getName(), Integer.toString(linkCounter.get(cag)));
            }
            return printMap.toString();
        }
    }
}
