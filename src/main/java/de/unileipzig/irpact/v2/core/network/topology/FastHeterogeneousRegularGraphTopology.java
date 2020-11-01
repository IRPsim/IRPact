package de.unileipzig.irpact.v2.core.network.topology;

import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.v2.core.network.SocialGraph;
import de.unileipzig.irpact.v2.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class FastHeterogeneousRegularGraphTopology implements GraphTopologyScheme {

    protected SocialGraph.Type edgeType;
    protected Map<ConsumerAgentGroup, Integer> consumerGroupZMapping;
    protected boolean isSelfReferential;
    protected double initialWeight;
    protected Random rnd;

    public FastHeterogeneousRegularGraphTopology(
            SocialGraph.Type edgeType,
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

    @Override
    public void initalize(SimulationEnvironment environment, SocialGraph graph) {
        Map<ConsumerAgentGroup, NodeHelper> cagNodes = new HashMap<>();
        for(SocialGraph.Node node: graph.getNodes()) {
            ConsumerAgentGroup cag = node.getAgent(ConsumerAgent.class).getGroup();
            NodeHelper cagNodeList = cagNodes.computeIfAbsent(cag, _cag -> new NodeHelper());
            cagNodeList.add(node);
        }
        for(NodeHelper cagNodeList: cagNodes.values()) {
            cagNodeList.init(rnd);
        }
        ConsumerAgentGroupAffinityMapping affinityMapping = environment.getAgents().getConsumerAgentGroupAffinityMapping();
        for(SocialGraph.Node sourceNode: graph.getNodes()) {
            ConsumerAgentGroup sourceCag = sourceNode.getAgent(ConsumerAgent.class).getGroup();
            ConsumerAgentGroupAffinities sourceAffinities = affinityMapping.get(sourceCag);
            int i = 0;
            final int zMapping = consumerGroupZMapping.get(sourceCag);
            while(i < zMapping) {
                ConsumerAgentGroup targetCag = sourceAffinities.getWeightedRandom(rnd);
                NodeHelper nodeList = cagNodes.get(targetCag);
                SocialGraph.Node peek = nodeList.peek();
                if(!isSelfReferential && peek == sourceNode) {
                    continue;
                }
                if(graph.hasEdge(sourceNode, peek, edgeType)) {
                    nodeList.discard();
                    continue;
                }
                SocialGraph.Node targetNode = nodeList.next(rnd);
                graph.addEdge(sourceNode, targetNode, edgeType);
                SocialGraph.Edge edge = graph.getEdge(sourceNode, targetNode, edgeType);
                edge.setWeight(initialWeight);
                i++;
            }
        }
    }

    /**
     * @author Daniel Abitz
     */
    protected static class NodeHelper {

        protected LinkedList<SocialGraph.Node> back = new LinkedList<>();
        protected LinkedList<SocialGraph.Node> front = new LinkedList<>();

        protected NodeHelper() {
        }

        protected void add(SocialGraph.Node node) {
            front.add(node);
        }

        protected void init(Random rnd) {
            shuffle(rnd);
        }

        protected void shuffle(Random rnd) {
            Collections.shuffle(front, rnd);
        }

        protected SocialGraph.Node peek() {
            return front.getFirst();
        }

        protected void discard() {
            SocialGraph.Node next = front.removeFirst();
            front.addLast(next);
        }

        protected SocialGraph.Node next(Random rnd) {
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
