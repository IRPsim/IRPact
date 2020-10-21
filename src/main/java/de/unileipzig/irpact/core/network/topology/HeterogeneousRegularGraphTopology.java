package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.v2.commons.CollectionUtil;
import de.unileipzig.irpact.commons.div.KeyValueCountHelper;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.network.EdgeType;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class HeterogeneousRegularGraphTopology implements GraphTopologyScheme {

    protected Map<ConsumerAgentGroup, Integer> consumerGroupZMapping;
    protected boolean isSelfReferential;
    protected EdgeType edgeType;
    protected double initialWeight;
    protected Random rnd;

    public HeterogeneousRegularGraphTopology(
            EdgeType edgeType,
            Map<ConsumerAgentGroup, Integer> consumerGroupZMapping,
            boolean isSelfReferential,
            double initialWeight,
            Random rnd) {
        this.edgeType = edgeType;
        this.consumerGroupZMapping = consumerGroupZMapping;
        this.isSelfReferential = isSelfReferential;
        this.initialWeight = initialWeight;
        this.rnd = rnd;
    }

    public static void initalize(
            SocialGraph graph,
            EdgeType edgeType,
            Collection<? extends SocialGraph.Node> consideredNodes,
            ConsumerAgentGroupAffinitiesMapping affinitiesMapping,
            Map<ConsumerAgentGroup, Integer> consumerGroupZMapping,
            boolean isSelfReferential,
            double initialWeight,
            Random rnd) {
        for(SocialGraph.Node node: consideredNodes) {
            if(!graph.hasNode(node)) {
                throw new IllegalStateException("Missing Node: " + node.getLabel());
            }
        }
        //
        Map<ConsumerAgentGroup, Set<SocialGraph.Node>> cagNodes = new HashMap<>();
        KeyValueCountHelper<ConsumerAgentGroup, SocialGraph.Node> inDegreeHelper = new KeyValueCountHelper<>(
                () -> new TreeMap<>(Integer::compareTo), LinkedHashSet::new, new HashMap<>()
        );
        for(SocialGraph.Node node: consideredNodes) {
            ConsumerAgentGroup cag = node.getAgent(ConsumerAgent.class).getGroup();
            Set<SocialGraph.Node> cagNodesSet = cagNodes.computeIfAbsent(cag, _cag -> new HashSet<>());
            cagNodesSet.add(node);
            inDegreeHelper.initialize(cag, node, 0);
        }
        //
        for(Map.Entry<ConsumerAgentGroup, Set<SocialGraph.Node>> cagNodesEntry: cagNodes.entrySet()) {
            ConsumerAgentGroup sourceCag = cagNodesEntry.getKey();
            ConsumerAgentGroupAffinities affinities = affinitiesMapping.get(sourceCag);
            for(SocialGraph.Node sourceNode: cagNodesEntry.getValue()) {
                int i = 0;
                final int zMapping = consumerGroupZMapping.get(sourceCag);
                while(i < zMapping) {
                    ConsumerAgentGroup targetCag = affinities.getRandom(rnd);
                    List<SocialGraph.Node> targetNodes = inDegreeHelper.listFirstValues(targetCag);
                    targetNodes.removeIf(_target -> graph.hasEdge(sourceNode, _target, edgeType) || (isSelfReferential && sourceNode == _target));
                    if(!targetNodes.isEmpty()) {
                        SocialGraph.Node targetNode = CollectionUtil.getRandom(targetNodes, rnd);
                        SocialGraph.Edge edge = graph.addEdge(sourceNode, targetNode, edgeType);
                        edge.setWeight(initialWeight);
                        inDegreeHelper.update(targetNode, 1);
                        i++;
                    }
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
