package de.unileipzig.irpact.v2.core.network.topology;

import de.unileipzig.irpact.v2.commons.CollectionUtil;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.v2.core.network.SocialGraph;
import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class FastHeterogeneousSmallWorldTopology extends FastHeterogeneousRegularGraphTopology {

    protected Map<ConsumerAgentGroup, Double> consumerGroupBetaMapping;

    public FastHeterogeneousSmallWorldTopology(
            SocialGraph.Type edgeType,
            Map<ConsumerAgentGroup, Double> consumerGroupBetaMapping,
            Map<ConsumerAgentGroup, Integer> consumerGroupZMapping,
            boolean isSelfReferential,
            double initialWeight,
            long seed) {
        super(edgeType, consumerGroupZMapping, isSelfReferential, initialWeight, seed);
        this.consumerGroupBetaMapping = consumerGroupBetaMapping;
    }

    protected static Map<ConsumerAgentGroup, Set<SocialGraph.Node>> group(SocialGraph graph) {
        Map<ConsumerAgentGroup, Set<SocialGraph.Node>> cagNodes = new HashMap<>();
        for(SocialGraph.Node node: graph.getNodes()) {
            ConsumerAgentGroup cag = node.getAgent(ConsumerAgent.class).getGroup();
            Set<SocialGraph.Node> cagNodesSet = cagNodes.computeIfAbsent(cag, _cag -> new HashSet<>());
            cagNodesSet.add(node);
        }
        return cagNodes;
    }

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        super.initalize(environment, graph);
        ConsumerAgentGroupAffinityMapping affinityMapping = environment.getAgents().getConsumerAgentGroupAffinityMapping();
        Map<ConsumerAgentGroup, Set<SocialGraph.Node>> cagNodes = group(graph);
        Set<SocialGraph.Edge> edgesCopy = new LinkedHashSet<>(graph.getEdges(edgeType));
        for(SocialGraph.Edge edge: edgesCopy) {
            SocialGraph.Node sourceNode = edge.getSource();
            ConsumerAgentGroup sourceGroup = sourceNode.getAgent(ConsumerAgent.class).getGroup();
            SocialGraph.Node targetNode = edge.getTarget();
            ConsumerAgentGroup targetGroup = targetNode.getAgent(ConsumerAgent.class).getGroup();
            double beta = consumerGroupBetaMapping.get(sourceGroup);
            if(rnd.nextDouble() < beta) {
                while(true) {
                    Set<? extends SocialGraph.Node> sourceTargets = graph.getTargets(sourceNode, edgeType);
                    Set<SocialGraph.Node> potentialTargets = cagNodes.get(targetGroup);
                    potentialTargets.removeAll(sourceTargets);
                    if(!isSelfReferential) {
                        potentialTargets.remove(sourceNode);
                    }
                    if(potentialTargets.isEmpty()) {
                        targetGroup = affinityMapping.get(sourceGroup).getWeightedRandom(rnd);
                        continue;
                    }
                    SocialGraph.Node newTarget = CollectionUtil.getRandom(potentialTargets, rnd);
                    graph.removeEdge(edge);
                    graph.addEdge(sourceNode, newTarget, edgeType);
                    SocialGraph.Edge newEdge = graph.getEdge(sourceNode, newTarget, edgeType);
                    newEdge.setWeight(initialWeight);
                    break;
                }
            }
        }
    }
}
