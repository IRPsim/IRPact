package de.unileipzig.irpact.v2.core.network.topology;

import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.v2.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.v2.core.network.SocialGraph;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class HeterogeneousRegularGraphTopology {


//    public static void initalize(
//            SocialGraph graph,
//            SocialGraph.Type edgeType,
//            Collection<? extends SocialGraph.Node> consideredNodes,
//            ConsumerAgentGroupAffinitiesMapping affinitiesMapping,
//            Map<ConsumerAgentGroup, Integer> consumerGroupZMapping,
//            boolean isSelfReferential,
//            double initialWeight,
//            Random rnd) {
//        for(SocialGraph.Node node: consideredNodes) {
//            if(!graph.hasNode(node)) {
//                throw new IllegalStateException("Missing Node: " + node.getLabel());
//            }
//        }
//        Map<ConsumerAgentGroup, NodeList> cagNodes = new HashMap<>();
//        for(SocialGraph.Node node: consideredNodes) {
//            ConsumerAgentGroup cag = node.getAgent(ConsumerAgent.class).getGroup();
//            NodeList cagNodeList = cagNodes.computeIfAbsent(cag, _cag -> new NodeList());
//            cagNodeList.add(node);
//        }
//        for(NodeList cagNodeList: cagNodes.values()) {
//            cagNodeList.init(rnd);
//        }
//        for(SocialGraph.Node sourceNode: consideredNodes) {
//            ConsumerAgentGroup sourceCag = sourceNode.getAgent(ConsumerAgent.class).getGroup();
//            ConsumerAgentGroupAffinities sourceAffinities = affinitiesMapping.get(sourceCag);
//            int i = 0;
//            final int zMapping = consumerGroupZMapping.get(sourceCag);
//            while(i < zMapping) {
//                ConsumerAgentGroup targetCag = sourceAffinities.getRandom(rnd);
//                NodeList nodeList = cagNodes.get(targetCag);
//                SocialGraph.Node peek = nodeList.peek();
//                if(!isSelfReferential && peek == sourceNode) {
//                    continue;
//                }
//                if(graph.hasEdge(sourceNode, peek, edgeType)) {
//                    nodeList.discard();
//                    continue;
//                }
//                SocialGraph.Node targetNode = nodeList.next(rnd);
//                graph.addEdge(sourceNode, targetNode, edgeType);
//                SocialGraph.Edge edge = graph.getEdge(sourceNode, targetNode, edgeType);
//                edge.setWeight(initialWeight);
//                i++;
//            }
//        }
//    }

    static class NodeList {

        private LinkedList<SocialGraph.Node> back = new LinkedList<>();
        private LinkedList<SocialGraph.Node> front = new LinkedList<>();

        private NodeList() {
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
