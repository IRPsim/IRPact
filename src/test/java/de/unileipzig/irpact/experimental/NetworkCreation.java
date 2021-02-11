package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.jadex.agents.consumer.PlaceholderConsumerAgent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.LoggingPart;
import de.unileipzig.irpact.core.log.LoggingPartFilter;
import de.unileipzig.irpact.core.log.LoggingType;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.FreeNetworkTopology;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Disabled
public class NetworkCreation {

    private static void addAgents(JadexConsumerAgentGroup grp, int count, BasicSocialGraph graph) {
        for(int i = 0; i < count; i++) {
            PlaceholderConsumerAgent agent = new PlaceholderConsumerAgent();
            agent.setGroup(grp);
            agent.setName(grp.getName() + "_" + i);
            grp.addAgent(agent);
            SocialGraph.Node node = graph.addAgentAndGetNode(agent);
            agent.setSocialGraphNode(node);
        }
    }

    @Test
    void testIt() {
        IRPLogging.initConsole();
        LoggingPartFilter filter = new LoggingPartFilter();
        filter.put(LoggingType.INITIALIZATION, LoggingPart.NETWORK);
        IRPLogging.setFilter(filter);

        BasicSocialGraph graph = new BasicSocialGraph();

        JadexConsumerAgentGroup grp0 = new JadexConsumerAgentGroup();
        grp0.setName("A");
        addAgents(grp0, 8, graph);

        JadexConsumerAgentGroup grp1 = new JadexConsumerAgentGroup();
        grp1.setName("B");
        addAgents(grp1, 12, graph);

        System.out.println("> " + graph.getNodes().size());

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(grp0, 3);
        edgeCountMap.put(grp1, 4);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.put(grp0, grp0, 0.7);
        affinityMapping.put(grp0, grp1, 0.3);
        affinityMapping.put(grp1, grp1,0.9);
        affinityMapping.put(grp1, grp0, 0.1);

        Rnd rnd = new Rnd(123);

        FreeNetworkTopology topology = new FreeNetworkTopology(
                SocialGraph.Type.COMMUNICATION,
                edgeCountMap,
                affinityMapping,
                null,
                1.0,
                rnd
        );

        topology.initalize(null, graph);

        System.out.println("> edges: " + graph.getEdges(SocialGraph.Type.COMMUNICATION).size());
    }
}
