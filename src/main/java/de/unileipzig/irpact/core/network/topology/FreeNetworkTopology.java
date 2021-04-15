package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.*;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.weighted.BasicWeightedMapping;
import de.unileipzig.irpact.commons.util.weighted.WeightedMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.util.Todo;
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

    public FreeNetworkTopology() {
    }

    public FreeNetworkTopology(
            SocialGraph.Type edgeType,
            String name,
            Map<ConsumerAgentGroup, Integer> edgeCountMap,
            ConsumerAgentGroupAffinityMapping affinityMapping,
            DistanceEvaluator distanceEvaluator,
            double initialWeight,
            Rnd rnd) {
        this.edgeType = edgeType;
        setName(name);
        this.edgeCountMap = edgeCountMap;
        this.affinityMapping = affinityMapping;
        this.distanceEvaluator = distanceEvaluator;
        this.initialWeight = initialWeight;
        this.rnd = rnd;
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
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "initialize free network graph");
        for(SocialGraph.Node node: graph.getNodes()) {
            SocialGraph.LinkageInformation li = graph.getLinkageInformation(node);
            ConsumerAgent ca = node.getAgent(ConsumerAgent.class);
            Set<ConsumerAgent> agents = drawTargets(environment, ca);
            for(ConsumerAgent targetCa: agents) {
                LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "add edge: {}->{} ({},{})", ca.getName(), targetCa.getName(), edgeType, initialWeight);
                graph.addEdge(ca.getSocialGraphNode(), targetCa.getSocialGraphNode(), edgeType, initialWeight);
                li.inc(targetCa.getGroup(), edgeType);
            }
        }
    }

    @Todo("hier ist was kaputt")
    @Todo("sicherheitschecks fuer endlosschleife einfuegen, wenn z.b. der einzig gueltige agent ich selber bin")
    protected Set<ConsumerAgent> drawTargets(SimulationEnvironment environment, ConsumerAgent ca) {
        ConsumerAgentGroup cag = ca.getGroup();
        ConsumerAgentGroupAffinities cagAffinities = affinityMapping.get(cag);
        int edgeCount = edgeCountMap.get(cag);
        Set<ConsumerAgent> set = new LinkedHashSet<>();
        while(set.size() < edgeCount) {
            ConsumerAgentGroup targetCag;
            do {
                targetCag = cagAffinities.getWeightedRandom(rnd);
            } while(set.containsAll(targetCag.getAgents()));
            WeightedMapping<ConsumerAgent, ConsumerAgent, Double> distanceMapping = calculateDistanceMapping(environment, ca, targetCag);
            ConsumerAgent targetCa = distanceMapping.getWeightedRandom(ca, rnd);
            //TODO self referential hinzufuegen
            if(targetCa != ca) {
                set.add(targetCa);
            }
        }
        return set;
    }

    protected WeightedMapping<ConsumerAgent, ConsumerAgent, Double> calculateDistanceMapping(
            SimulationEnvironment environment,
            ConsumerAgent ca,
            ConsumerAgentGroup targetCag) {
        BasicWeightedMapping<ConsumerAgent, ConsumerAgent, Double> mapping = new BasicWeightedMapping<>();
        for(ConsumerAgent target: targetCag.getAgents()) {
            if(distanceEvaluator.isDisabled()) {
                mapping.put(ca, target, 1.0);
            } else {
                SpatialModel spatialModel = environment.getSpatialModel();
                double distance = spatialModel.distance(ca.getSpatialInformation(), target.getSpatialInformation());
                double eval = distanceEvaluator.evaluate(distance);
                mapping.put(ca, target, eval);
            }
        }
        return mapping;
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
}
