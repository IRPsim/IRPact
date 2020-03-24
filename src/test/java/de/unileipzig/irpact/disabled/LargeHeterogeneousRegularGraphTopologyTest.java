package de.unileipzig.irpact.disabled;

import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinities;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.EdgeType;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.HeterogeneousSmallWorldTopology;
import de.unileipzig.irpact.mock.MockConsumerAgent;
import de.unileipzig.irpact.mock.MockConsumerAgentGroup;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Daniel Abitz
 */
class LargeHeterogeneousRegularGraphTopologyTest {

    @Test
    void testLarge() {
        ConsumerAgentGroup g0 = new MockConsumerAgentGroup();
        ConsumerAgentGroup g1 = new MockConsumerAgentGroup();
        ConsumerAgentGroup g2 = new MockConsumerAgentGroup();

        SocialGraph graph = new BasicSocialGraph(
                new DirectedMultiGraph<>(new HashMap<>()),
                new HashMap<>()
        );

        final int count = 1000;
        for(int i = 0; i < count; i++) {
            MockConsumerAgent mca = new MockConsumerAgent("g0_" + i);
            mca.setGroup(g0);
            graph.addAgent(mca);
        }
        for(int i = 0; i < count; i++) {
            MockConsumerAgent mca = new MockConsumerAgent("g1_" + i);
            mca.setGroup(g1);
            graph.addAgent(mca);
        }
        for(int i = 0; i < count; i++) {
            MockConsumerAgent mca = new MockConsumerAgent("g2_" + i);
            mca.setGroup(g2);
            graph.addAgent(mca);
        }

        ConsumerAgentGroupAffinitiesMapping affinitiesMapping = new BasicConsumerAgentGroupAffinitiesMapping(new HashMap<>());
        affinitiesMapping.put(g0, new BasicConsumerAgentGroupAffinities(new HashMap<>()));
        affinitiesMapping.put(g1, new BasicConsumerAgentGroupAffinities(new HashMap<>()));
        affinitiesMapping.put(g2, new BasicConsumerAgentGroupAffinities(new HashMap<>()));
        /*
        affinitiesMapping.put(g0, g0, 0.5);
        affinitiesMapping.put(g0, g1, 0.4);
        affinitiesMapping.put(g0, g2, 0.1);
        affinitiesMapping.put(g1, g0, 0.3);
        affinitiesMapping.put(g1, g1, 0.5);
        affinitiesMapping.put(g1, g2, 0.2);
        affinitiesMapping.put(g2, g0, 0.25);
        affinitiesMapping.put(g2, g1, 0.25);
        affinitiesMapping.put(g2, g2, 0.5);
        */
        affinitiesMapping.put(g0, g1, 1.0);
        affinitiesMapping.put(g1, g2, 1.0);
        affinitiesMapping.put(g2, g0, 1.0);



        Map<ConsumerAgentGroup, Integer> zMapping = new HashMap<>();
        zMapping.put(g0, 500);
        zMapping.put(g1, 4);
        zMapping.put(g2, 3);

        Map<ConsumerAgentGroup, Double> betaMapping = new HashMap<>();
        betaMapping.put(g0, 0.1);
        betaMapping.put(g1, 0.2);
        betaMapping.put(g2, 0.05);

        HeterogeneousSmallWorldTopology.initalize(
                graph,
                EdgeType.COMMUNICATION,
                graph.getNodes(),
                affinitiesMapping,
                zMapping,
                //betaMapping,
                false,
                1.0,
                new Random(123)
        );


    }
}
