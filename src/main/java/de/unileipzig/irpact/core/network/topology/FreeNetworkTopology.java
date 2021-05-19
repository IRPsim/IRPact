package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.checksum.LoggableChecksum;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.log.LazyPrinter;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import de.unileipzig.irpact.commons.util.data.Pair;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.RemoveOnDrawNavigableMapWeightedMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.develop.XXXXXXXXX;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class FreeNetworkTopology extends NameableBase implements GraphTopologyScheme, LoggableChecksum {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FreeNetworkTopology.class);

    //for testing
    protected int gcStep = -1;
    protected int useCase = 0;
    //===

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
        return Checksums.SMART.getChecksum(
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
    public void logChecksums() {
        doLog("getName", Checksums.SMART.getChecksum(getName()));
        doLog("getInitialWeight", Checksums.SMART.getChecksum(getInitialWeight()));
        doLog("getRnd", Checksums.SMART.getChecksum(getRnd()));
        doLog("getDistanceEvaluator", Checksums.SMART.getChecksum(getDistanceEvaluator()));
        doLog("getEdgeType", Checksums.SMART.getChecksum(getEdgeType()));
        doLog("getAffinityMapping", Checksums.SMART.getChecksum(getAffinityMapping()));
        doLog("getEdgeCountMap", Checksums.SMART.getChecksum(getEdgeCountMap()));
        doLog("isSelfReferential", Checksums.SMART.getChecksum(isSelfReferential()));
    }

    protected void doLog(String msg, int checksum) {
        LOGGER.trace(IRPSection.GENERAL, "checksum {}: {}", msg, Integer.toHexString(checksum));
    }

    @XXXXXXXXX
    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) throws InitializationException {
        LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "initialize free network graph");

        if(environment.isRestored()) {
            LOGGER.warn("DIRTY FIX");
            return;
        }

        int step = 0;
        int added = 0;
        for(SocialGraph.Node node: graph.getNodes()) {
            SocialGraph.LinkageInformation li = graph.getLinkageInformation(node);
            ConsumerAgent ca = node.getAgent(ConsumerAgent.class);
            Collection<ConsumerAgent> agents = drawTargets(environment, graph, ca, li);
            for(ConsumerAgent targetCa: agents) {
                LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "add edge: {}->{} ({},{})", ca.getName(), targetCa.getName(), edgeType, initialWeight);
                if(!graph.addEdge(ca.getSocialGraphNode(), targetCa.getSocialGraphNode(), edgeType, initialWeight)) {
                    throw new InitializationException("add failed, edge already exists (FATAL - this should NOT happen)");
                }
                added++;
            }

            if(step % 100 == 0) {
                System.out.println(step + "/" + graph.getNodes().size());
            }
            if(gcStep > 0 && step > 0 && step % gcStep == 0) {
                System.gc();
            }
            step++;
        }
        targetCache.clear();
        System.gc();
        LOGGER.trace("total added: {}", added);
    }

    protected Collection<ConsumerAgent> drawTargets(
            SimulationEnvironment environment,
            SocialGraph graph,
            ConsumerAgent source,
            SocialGraph.LinkageInformation li) throws InitializationException {
        ConsumerAgentGroup sourceCag = source.getGroup();
        ConsumerAgentGroupAffinities sourceAffinities = affinityMapping.get(sourceCag);
        int edgeCount = edgeCountMap.get(sourceCag);
        CagOrder cagOrder = new CagOrder();
        cagOrder.initalize(
                environment.getAgents().getConsumerAgentGroups(),
                li
        );
        if(!selfReferential) {
            cagOrder.setSkipSelfReferentialGroup(sourceCag);
        }
        cagOrder.determine(edgeCount, sourceAffinities, allowLessEdges, rnd);
        LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "edge targets for '{}': {}", source.getName(), LazyPrinter.of(cagOrder::printEdgeTargets));
        cagOrder.apply(li, edgeType);

        List<ConsumerAgent> targetList = new ArrayList<>();
        for(ConsumerAgentGroup targetCag: cagOrder.getLinkCounter().keys()) {
            int targetCount = cagOrder.getLinkCounter().get(targetCag);

            addForTargetCag(
                    environment,
                    graph,
                    source,
                    targetCag,
                    cagOrder.hasExistingLinks(targetCag),
                    targetCount,
                    targetList
            );
        }
        return targetList;
    }

    protected void addForTargetCag(
            SimulationEnvironment environment,
            SocialGraph graph,
            ConsumerAgent source,
            ConsumerAgentGroup targetCag,
            boolean hasExistingLinks,
            int targetCount,
            List<ConsumerAgent> targetList) {
        if(distanceEvaluator.isDisabled()) {
            addForTargetCagWithoutDistance(graph, source, targetCag, hasExistingLinks, targetCount, targetList);
        } else {
            switch (useCase) {
                case 2:
                    addForTargetCagWithDistanceV2(environment, graph, source, targetCag, hasExistingLinks, targetCount, targetList);
                    break;

                case 3:
                    addForTargetCagWithDistanceV3(environment, graph, source, targetCag, hasExistingLinks, targetCount, targetList);
                    break;

                default:
                    addForTargetCagWithDistance(environment, graph, source, targetCag, hasExistingLinks, targetCount, targetList);
                    break;
            }
        }
    }

    protected void addForTargetCagWithoutDistance(
            SocialGraph graph,
            ConsumerAgent source,
            ConsumerAgentGroup targetCag,
            boolean hasExistingLinks,
            int targetCount,
            List<ConsumerAgent> targetList) {
        List<ConsumerAgent> list = getList(targetCag);
        Set<ConsumerAgent> distinctCache = new HashSet<>();
        while(distinctCache.size() < targetCount) {
            ConsumerAgent target = rnd.getRandom(list);
            if(!selfReferential && source == target) {
                continue;
            }
            if(hasExistingLinks && graph.hasEdge(source.getSocialGraphNode(), target.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                continue;
            }
            if(distinctCache.add(target)) {
                targetList.add(target);
            }
        }
    }

    protected void addForTargetCagWithDistance(
            SimulationEnvironment environment,
            SocialGraph graph,
            ConsumerAgent source,
            ConsumerAgentGroup targetCag,
            boolean hasExistingLinks,
            int targetCount,
            List<ConsumerAgent> targetList) {
        RemoveOnDrawNavigableMapWeightedMapping<ConsumerAgent> mapping = new RemoveOnDrawNavigableMapWeightedMapping<>();
        SpatialModel spatialModel = environment.getSpatialModel();
        for(ConsumerAgent target: targetCag.getAgents()) {
            if(!selfReferential && source == target) {
                continue;
            }
            if(hasExistingLinks && graph.hasEdge(source.getSocialGraphNode(), target.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                continue;
            }
            double distance = spatialModel.distance(source.getSpatialInformation(), target.getSpatialInformation());
            double influence = distanceEvaluator.evaluate(distance);
            if(influence <= 0.0) {
                continue;
            }
            mapping.set(target, influence);
        }

        for(int i = 0; i < targetCount; i++) {
            targetList.add(mapping.getWeightedRandom(rnd));
        }
    }

    protected void addForTargetCagWithDistanceV2(
            SimulationEnvironment environment,
            SocialGraph graph,
            ConsumerAgent source,
            ConsumerAgentGroup targetCag,
            boolean hasExistingLinks,
            int targetCount,
            List<ConsumerAgent> targetList) {
        NavigableMapWeightedMapping<ConsumerAgent> mapping = new NavigableMapWeightedMapping<>();
        SpatialModel spatialModel = environment.getSpatialModel();
        for(ConsumerAgent target: targetCag.getAgents()) {
            if(!selfReferential && source == target) {
                continue;
            }
            if(hasExistingLinks && graph.hasEdge(source.getSocialGraphNode(), target.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                continue;
            }
            double distance = spatialModel.distance(source.getSpatialInformation(), target.getSpatialInformation());
            double influence = distanceEvaluator.evaluate(distance);
            if(influence <= 0.0) {
                continue;
            }
            mapping.set(target, influence);
        }

        Set<ConsumerAgent> distinctCache = new HashSet<>();
        while(distinctCache.size() < targetCount) {
            ConsumerAgent target = mapping.getWeightedRandom(rnd);
            if(distinctCache.add(target)) {
                targetList.add(target);
            }
        }
    }

    protected void addForTargetCagWithDistanceV3(
            SimulationEnvironment environment,
            SocialGraph graph,
            ConsumerAgent source,
            ConsumerAgentGroup targetCag,
            boolean hasExistingLinks,
            int targetCount,
            List<ConsumerAgent> targetList) {
        LinkedList<Pair<ConsumerAgent, Double>> data = new LinkedList<>();
        SpatialModel spatialModel = environment.getSpatialModel();
        for(ConsumerAgent target: targetCag.getAgents()) {
            if(!selfReferential && source == target) {
                continue;
            }
            if(hasExistingLinks && graph.hasEdge(source.getSocialGraphNode(), target.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                continue;
            }
            double distance = spatialModel.distance(source.getSpatialInformation(), target.getSpatialInformation());
            double influence = distanceEvaluator.evaluate(distance);
            if(influence <= 0.0) {
                continue;
            }
            data.add(new Pair<>(target, influence));
        }

        data.sort(Map.Entry.comparingByValue());

        for(int i = 0; i < targetCount; i++) {
            targetList.add(data.removeLast().getKey());
        }
    }

    /**
     * @author Daniel Abitz
     */
    protected static class CagOrder {

        protected DataCounter<ConsumerAgentGroup> existingLinkCounter = new DataCounter<>();
        protected DataCounter<ConsumerAgentGroup> linkCounter = new DataCounter<>();
        protected ConsumerAgentGroup skipSelfReferentialGroup;

        protected CagOrder() {
        }

        protected void initalize(
                Collection<? extends ConsumerAgentGroup> cags,
                SocialGraph.LinkageInformation li) {
            for(ConsumerAgentGroup cag: cags) {
                int current = li.get(cag, SocialGraph.Type.COMMUNICATION);
                existingLinkCounter.set(cag, current);
            }
        }

        protected boolean hasExistingLinks(ConsumerAgentGroup cag) {
            return existingLinkCounter.get(cag) != 0;
        }

        //Falls diese Gruppe gesetzt ist, wird ein Element uebersprungen, um die Selbstreferenzierung zu verhindern.
        protected void setSkipSelfReferentialGroup(ConsumerAgentGroup skipSelfReferentialGroup) {
            this.skipSelfReferentialGroup = skipSelfReferentialGroup;
        }

        protected boolean isMax(ConsumerAgentGroup cag) {
            int skipSelf = cag == skipSelfReferentialGroup ? 1 : 0;
            int numberOfAgents = cag.getNumberOfAgents() - skipSelf;
            int current = linkCounter.get(cag) + existingLinkCounter.get(cag);
            return current >= numberOfAgents;
        }

        protected void determine(
                int count,
                ConsumerAgentGroupAffinities affinities,
                boolean allowLessAgents,
                Rnd rnd) throws InitializationException {
            ConsumerAgentGroupAffinities caga = affinities;
            ConsumerAgentGroup target;
            int found = existingLinkCounter.total();
            boolean tryAgain = found < count;
            while(tryAgain) {
                target = caga.getWeightedRandom(rnd);

                if(isMax(target)) {
                    caga = caga.createWithout(target);
                    if(caga.isEmpty()) {
                        if(allowLessAgents) {
                            LOGGER.info("[allowLessAgents] not enough agents, agents found: {}, required: {}", found, count);
                            return;
                        } else {
                            throw new InitializationException("not enough agents, agents found: {}, required: {}", found, count);
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
                li.update(cag, edgeType, count);
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
