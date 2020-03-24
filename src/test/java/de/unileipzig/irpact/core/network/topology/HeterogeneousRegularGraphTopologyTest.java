package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.EdgeType;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.mock.MockConsumerAgent;
import de.unileipzig.irpact.mock.MockConsumerAgentGroup;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class HeterogeneousRegularGraphTopologyTest {

    @Test
    void testStaticInit() {
        ConsumerAgentGroup g0 = new MockConsumerAgentGroup();
        ConsumerAgentGroup g1 = new MockConsumerAgentGroup();
        ConsumerAgentGroup g2 = new MockConsumerAgentGroup();

        ConsumerAgentGroupAffinitiesMapping affinitiesMapping = new BasicConsumerAgentGroupAffinitiesMapping(new HashMap<>());
        affinitiesMapping.put(g0, new BasicConsumerAgentGroupAffinities(new HashMap<>()));
        affinitiesMapping.put(g1, new BasicConsumerAgentGroupAffinities(new HashMap<>()));
        affinitiesMapping.put(g2, new BasicConsumerAgentGroupAffinities(new HashMap<>()));

        affinitiesMapping.put(g0, g1, 1.0);
        affinitiesMapping.put(g1, g2, 1.0);
        affinitiesMapping.put(g2, g0, 1.0);

        assertEquals(1.0, affinitiesMapping.getValue(g0, g1));
        assertThrows(NoSuchElementException.class, () -> affinitiesMapping.getValue(g0, g2));

        MockConsumerAgent g0_0 = new MockConsumerAgent("g0_0");
        g0_0.setGroup(g0);
        MockConsumerAgent g0_1 = new MockConsumerAgent("g0_1");
        g0_1.setGroup(g0);
        MockConsumerAgent g0_2 = new MockConsumerAgent("g0_2");
        g0_2.setGroup(g0);

        MockConsumerAgent g1_0 = new MockConsumerAgent("g1_0");
        g1_0.setGroup(g1);
        MockConsumerAgent g1_1 = new MockConsumerAgent("g1_1");
        g1_1.setGroup(g1);
        MockConsumerAgent g1_2 = new MockConsumerAgent("g1_2");
        g1_2.setGroup(g1);

        MockConsumerAgent g2_0 = new MockConsumerAgent("g2_0");
        g2_0.setGroup(g2);
        MockConsumerAgent g2_1 = new MockConsumerAgent("g2_1");
        g2_1.setGroup(g2);

        Set<ConsumerAgent> agents = new HashSet<>();
        agents.add(g0_0);
        agents.add(g0_1);
        agents.add(g0_2);
        agents.add(g1_0);
        agents.add(g1_1);
        agents.add(g1_2);
        agents.add(g2_0);
        agents.add(g2_1);

        Map<ConsumerAgentGroup, Integer> zMapping = new HashMap<>();
        zMapping.put(g0, 2);
        zMapping.put(g1, 1);
        zMapping.put(g2, 3);

        SocialGraph graph = new BasicSocialGraph(
                new DirectedMultiGraph<>(new HashMap<>()),
                new HashMap<>()
        );
        Set<SocialGraph.Node> nodes = new HashSet<>();
        for(ConsumerAgent agent: agents) {
            nodes.add(graph.addAgent(agent));
        }

        HeterogeneousRegularGraphTopology.initalize(
                graph,
                EdgeType.COMMUNICATION,
                nodes,
                affinitiesMapping,
                zMapping,
                false,
                1.0,
                new Random(123)
        );

        Collection<? extends SocialGraph.Edge> g0_0Edges = graph.getOutEdges(
                graph.findNode(g0_0),
                EdgeType.COMMUNICATION
        );
        assertEquals(2, g0_0Edges.size());
        for(SocialGraph.Edge e: g0_0Edges) {
            assertSame(g0, e.getSource().getAgent(ConsumerAgent.class).getGroup());
            assertSame(g1, e.getTarget().getAgent(ConsumerAgent.class).getGroup());
            assertEquals(1.0, e.getWeight());
        }

        Collection<? extends SocialGraph.Edge> g1_0Edges = graph.getOutEdges(
                graph.findNode(g1_0),
                EdgeType.COMMUNICATION
        );
        assertEquals(1, g1_0Edges.size());
        for(SocialGraph.Edge e: g1_0Edges) {
            assertSame(g1, e.getSource().getAgent(ConsumerAgent.class).getGroup());
            assertSame(g2, e.getTarget().getAgent(ConsumerAgent.class).getGroup());
        }

        Collection<? extends SocialGraph.Edge> g2_0Edges = graph.getOutEdges(
                graph.findNode(g2_0),
                EdgeType.COMMUNICATION
        );
        assertEquals(3, g2_0Edges.size());
        for(SocialGraph.Edge e: g2_0Edges) {
            assertSame(g2, e.getSource().getAgent(ConsumerAgent.class).getGroup());
            assertSame(g0, e.getTarget().getAgent(ConsumerAgent.class).getGroup());
        }
    }

    @Test
    void testMinimal() {
        ConsumerAgentGroup g0 = new MockConsumerAgentGroup();
        ConsumerAgentGroup g1 = new MockConsumerAgentGroup();

        ConsumerAgentGroupAffinitiesMapping affinitiesMapping = new BasicConsumerAgentGroupAffinitiesMapping(new HashMap<>());
        affinitiesMapping.put(g0, new BasicConsumerAgentGroupAffinities(new HashMap<>()));
        affinitiesMapping.put(g1, new BasicConsumerAgentGroupAffinities(new HashMap<>()));

        affinitiesMapping.put(g0, g1, 1.0);
        //affinitiesMapping.put(g0, g1, 1.0);

        assertEquals(1.0, affinitiesMapping.getValue(g0, g1));

        MockConsumerAgent g0_0 = new MockConsumerAgent("g0_0");
        g0_0.setGroup(g0);
        MockConsumerAgent g0_1 = new MockConsumerAgent("g0_1");
        g0_1.setGroup(g0);
        MockConsumerAgent g0_2 = new MockConsumerAgent("g0_2");
        g0_2.setGroup(g0);

        MockConsumerAgent g1_0 = new MockConsumerAgent("g1_0");
        g1_0.setGroup(g1);
        MockConsumerAgent g1_1 = new MockConsumerAgent("g1_1");
        g1_1.setGroup(g1);
        MockConsumerAgent g1_2 = new MockConsumerAgent("g1_2");
        g1_2.setGroup(g1);

        Set<ConsumerAgent> agents = new HashSet<>();
        agents.add(g0_0);
        agents.add(g0_1);
        agents.add(g0_2);
        agents.add(g1_0);
        agents.add(g1_1);
        agents.add(g1_2);

        Map<ConsumerAgentGroup, Integer> zMapping = new HashMap<>();
        zMapping.put(g0, 3);
        zMapping.put(g1, 0);

        SocialGraph graph = new BasicSocialGraph(
                new DirectedMultiGraph<>(new HashMap<>()),
                new HashMap<>()
        );
        Set<SocialGraph.Node> nodes = new HashSet<>();
        for(ConsumerAgent agent: agents) {
            nodes.add(graph.addAgent(agent));
        }

        HeterogeneousRegularGraphTopology.initalize(
                graph,
                EdgeType.COMMUNICATION,
                nodes,
                affinitiesMapping,
                zMapping,
                false,
                0.0,
                new Random(123)
        );

        Collection<? extends SocialGraph.Edge> g0_0Edges = graph.getOutEdges(
                graph.findNode(g0_0),
                EdgeType.COMMUNICATION
        );
        assertEquals(3, g0_0Edges.size());
        assertEquals(
                CollectionUtil.hashSetOf(
                        graph.getEdge(graph.findNode(g0_0), graph.findNode(g1_0), EdgeType.COMMUNICATION),
                        graph.getEdge(graph.findNode(g0_0), graph.findNode(g1_1), EdgeType.COMMUNICATION),
                        graph.getEdge(graph.findNode(g0_0), graph.findNode(g1_2), EdgeType.COMMUNICATION)
                ),
                new HashSet<>(g0_0Edges)
        );
    }
}