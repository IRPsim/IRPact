package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.BasicWeightedMapping;
import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.WeightedMapping;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class FreeNetworkTopology implements GraphTopologyScheme {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FreeNetworkTopology.class);

    protected SocialGraph.Type edgeType;
    protected Map<ConsumerAgentGroup, Integer> edgeCountMap;
    protected ConsumerAgentGroupAffinityMapping affinityMapping;
    protected DistanceEvaluator distanceEvaluator;
    protected double initialWeight;
    protected Rnd rnd;

    public FreeNetworkTopology(
            SocialGraph.Type edgeType,
            Map<ConsumerAgentGroup, Integer> edgeCountMap,
            ConsumerAgentGroupAffinityMapping affinityMapping,
            DistanceEvaluator distanceEvaluator,
            double initialWeight,
            Rnd rnd) {
        this.edgeType = edgeType;
        this.edgeCountMap = edgeCountMap;
        this.affinityMapping = affinityMapping;
        this.distanceEvaluator = distanceEvaluator;
        this.initialWeight = initialWeight;
        this.rnd = rnd;
    }

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        LOGGER.debug("HALLO {}", graph.getNodes().size());
        for(SocialGraph.Node node: graph.getNodes()) {
            ConsumerAgent ca = node.getAgent(ConsumerAgent.class);
            Set<ConsumerAgent> agents = drawTargets(environment, ca);
            for(ConsumerAgent targetCa: agents) {
                LOGGER.trace(IRPSection.INITIALIZATION_NETWORK, "add edge: {}-{} ({},{})", ca.getName(), targetCa.getName(), edgeType, initialWeight);
                graph.addEdge(ca.getSocialGraphNode(), targetCa.getSocialGraphNode(), edgeType, initialWeight);
            }
        }
    }

    protected Set<ConsumerAgent> drawTargets(SimulationEnvironment environment, ConsumerAgent ca) {
        ConsumerAgentGroup cag = ca.getGroup();
        ConsumerAgentGroupAffinities cagAffinities = affinityMapping.get(cag);
        int edgeCount = edgeCountMap.get(cag);
        Set<ConsumerAgent> set = new LinkedHashSet<>();
        while(set.size() < edgeCount) {
            ConsumerAgentGroup targetCag;
            do {
                targetCag = cagAffinities.getWeightedRandom(rnd.getRandom());
            } while(set.containsAll(targetCag.getAgents()));
            WeightedMapping<ConsumerAgent, ConsumerAgent, Double> distanceMapping = calculateDistanceMapping(environment, ca, targetCag);
            ConsumerAgent targetCa = distanceMapping.getWeightedRandom(ca, rnd.getRandom());
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
            if(distanceEvaluator == null) {
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
}
