package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class FastHeterogeneousRegularGraphTopology extends NameableBase implements GraphTopologyScheme {

    protected SocialGraph.Type edgeType;
    protected Map<ConsumerAgentGroup, Integer> consumerGroupZMapping;
    protected boolean isSelfReferential;
    protected double initialWeight;
    protected long seed;
    protected Rnd rnd;

    public FastHeterogeneousRegularGraphTopology(
            SocialGraph.Type edgeType,
            Map<ConsumerAgentGroup, Integer> consumerGroupZMapping,
            boolean isSelfReferential,
            double initialWeight,
            long seed) {
        this.edgeType = edgeType;
        this.consumerGroupZMapping = consumerGroupZMapping;
        this.isSelfReferential = isSelfReferential;
        this.initialWeight = initialWeight;
        this.seed = seed;
        rnd = new Rnd(seed);
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
                graph.addEdge(sourceNode, targetNode, edgeType, initialWeight);
                i++;
            }
        }
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.unsupportedChecksum(getClass());
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

        protected void init(Rnd rnd) {
            shuffle(rnd);
        }

        protected void shuffle(Rnd rnd) {
            rnd.shuffle(front);
        }

        protected SocialGraph.Node peek() {
            return front.getFirst();
        }

        protected void discard() {
            SocialGraph.Node next = front.removeFirst();
            front.addLast(next);
        }

        protected SocialGraph.Node next(Rnd rnd) {
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
