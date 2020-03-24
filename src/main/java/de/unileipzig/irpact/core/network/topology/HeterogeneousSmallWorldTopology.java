package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.network.EdgeType;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

public class HeterogeneousSmallWorldTopology extends HeterogeneousRegularGraphTopology {

    protected Map<ConsumerAgentGroup, Double> consumerGroupBetaMapping;

    public HeterogeneousSmallWorldTopology(
            EdgeType edgeType,
            Map<ConsumerAgentGroup, Integer> consumerGroupZMapping,
            Map<ConsumerAgentGroup, Double> consumerGroupBetaMapping,
            boolean isSelfReferential,
            double initialWeight,
            Random rnd) {
        super(edgeType, consumerGroupZMapping, isSelfReferential, initialWeight, rnd);
        this.consumerGroupBetaMapping = consumerGroupBetaMapping;
    }

    @ToDo("umschreiben")
    @ToDo("testen, ob sets erstellt werden (wegen remove)")
    public static void initalize(
            SocialGraph graph,
            EdgeType edgeType,
            Collection<? extends SocialGraph.Node> consideredNodes,
            ConsumerAgentGroupAffinitiesMapping affinitiesMapping,
            Map<ConsumerAgentGroup, Integer> consumerGroupZMapping,
            Map<ConsumerAgentGroup, Double> consumerGroupBetaMapping,
            boolean isSelfReferential,
            double initialWeight,
            Random rnd) {
        HeterogeneousRegularGraphTopology.initalize(
                graph,
                edgeType,
                consideredNodes,
                affinitiesMapping,
                consumerGroupZMapping,
                isSelfReferential,
                initialWeight,
                rnd
        );
        Map<ConsumerAgentGroup, Set<SocialGraph.Node>> cagNodes = new HashMap<>();
        for(SocialGraph.Node node: consideredNodes) {
            ConsumerAgentGroup cag = node.getAgent(ConsumerAgent.class).getGroup();
            Set<SocialGraph.Node> cagNodesSet = cagNodes.computeIfAbsent(cag, _cag -> new HashSet<>());
            cagNodesSet.add(node);
        }
        Set<SocialGraph.Edge> edgesCopy = new HashSet<>(graph.getEdges(edgeType));
        for(SocialGraph.Edge edge: edgesCopy) {
            SocialGraph.Node sourceNode = edge.getSource();
            ConsumerAgentGroup sourceGroup = sourceNode.getAgent(ConsumerAgent.class)
                    .getGroup();
            SocialGraph.Node targetNode = edge.getTarget();
            ConsumerAgentGroup targetGroup = targetNode.getAgent(ConsumerAgent.class)
                    .getGroup();
            double beta = consumerGroupBetaMapping.get(sourceGroup);
            if(rnd.nextDouble() < beta) {
                while(true) {
                    Set<? extends SocialGraph.Node> currentTargets = graph.getTargetNodes(sourceNode, edgeType);
                    Set<SocialGraph.Node> potentialTargetsNodes = cagNodes.get(targetGroup);
                    potentialTargetsNodes.removeAll(currentTargets);
                    if(!isSelfReferential) {
                        potentialTargetsNodes.remove(sourceNode);
                    }
                    if(potentialTargetsNodes.isEmpty()) {
                        targetGroup = affinitiesMapping.get(sourceGroup)
                                .getRandom(rnd);
                        continue; //ugly...
                    }
                    SocialGraph.Node newTarget = CollectionUtil.getRandom(potentialTargetsNodes, rnd);
                    graph.removeEdge(edge, edgeType);
                    SocialGraph.Edge newEdge = graph.addEdge(sourceNode, newTarget, edgeType);
                    newEdge.setWeight(initialWeight);
                    break; //ugly2...
                }
            }
        }
    }

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        initalize(
                graph,
                edgeType,
                graph.getNodes(),
                environment.getConfiguration().getAffinitiesMapping(),
                consumerGroupZMapping,
                consumerGroupBetaMapping,
                isSelfReferential,
                initialWeight,
                rnd
        );
    }

    @Override
    public void addSubsequently(SimulationEnvironment environment, SocialGraph graph, ConsumerAgent agent) {

    }

    @Override
    public void removeSubsequently(SimulationEnvironment environment, SocialGraph graph, ConsumerAgent agent) {

    }
}
