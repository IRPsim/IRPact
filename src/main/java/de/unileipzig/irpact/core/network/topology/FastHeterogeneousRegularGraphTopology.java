package de.unileipzig.irpact.core.network.topology;

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
public class FastHeterogeneousRegularGraphTopology implements GraphTopologyScheme {

    protected Map<ConsumerAgentGroup, Integer> consumerGroupZMapping;
    protected boolean isSelfReferential;
    protected EdgeType edgeType;
    protected double initialWeight;
    protected Random rnd;

    public FastHeterogeneousRegularGraphTopology(
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
        Map<ConsumerAgentGroup, HelpNodeList> cagNodes = new HashMap<>();
        for(SocialGraph.Node node: consideredNodes) {
            ConsumerAgentGroup cag = node.getAgent(ConsumerAgent.class).getGroup();
            HelpNodeList cagNodeList = cagNodes.computeIfAbsent(cag, _cag -> new HelpNodeList());
            cagNodeList.add(node);
        }
        for(HelpNodeList cagNodeList: cagNodes.values()) {
            cagNodeList.init(rnd);
        }
        for(SocialGraph.Node sourceNode: consideredNodes) {
            ConsumerAgentGroup sourceCag = sourceNode.getAgent(ConsumerAgent.class).getGroup();
            ConsumerAgentGroupAffinities sourceAffinities = affinitiesMapping.get(sourceCag);
            int i = 0;
            final int zMapping = consumerGroupZMapping.get(sourceCag);
            while(i < zMapping) {
                ConsumerAgentGroup targetCag = sourceAffinities.getRandom(rnd);
                HelpNodeList nodeList = cagNodes.get(targetCag);
                SocialGraph.Node peek = nodeList.peek();
                if(!isSelfReferential && peek == sourceNode) {
                    continue;
                }
                if(graph.hasEdge(sourceNode, peek, edgeType)) {
                    nodeList.discard();
                    continue;
                }
                SocialGraph.Node targetNode = nodeList.next(rnd);
                SocialGraph.Edge edge = graph.addEdge(sourceNode, targetNode, edgeType);
                edge.setWeight(initialWeight);
                i++;
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

    static class HelpNodeList {

        private LinkedList<SocialGraph.Node> back = new LinkedList<>();
        private LinkedList<SocialGraph.Node> front = new LinkedList<>();

        private HelpNodeList() {
        }

        private void add(SocialGraph.Node node) {
            front.add(node);
        }

        private void init(Random rnd) {
            shuffle(rnd);
        }

        private void shuffle(Random rnd) {
            Collections.shuffle(front, rnd);
        }

        private SocialGraph.Node peek() {
            return front.getFirst();
        }

        private void discard() {
            SocialGraph.Node next = front.removeFirst();
            front.addLast(next);
        }

        private SocialGraph.Node next(Random rnd) {
            SocialGraph.Node next = front.removeFirst();
            back.addLast(next);
            if(front.isEmpty()) {
                LinkedList<SocialGraph.Node> temp = front;
                front = back;
                back = temp;
                shuffle(rnd);
            }
            return next;
        }
    }
}
