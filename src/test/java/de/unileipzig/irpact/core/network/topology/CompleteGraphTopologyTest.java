package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.product.awareness.ProductBinaryAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.core.spatial.distribution.FixSpatialDistrubtion;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class CompleteGraphTopologyTest {

    @Test
    void testSingleInit() throws InitializationException {
        IRPLogging.disableLogging();

        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.initDefault();
        BasicSocialNetwork network = (BasicSocialNetwork) env.getNetwork();
        network.initDefaultGraph();
        BasicAgentManager agentManager = (BasicAgentManager) env.getAgents();

        FixSpatialDistrubtion spatialDistrubtion = new FixSpatialDistrubtion();
        spatialDistrubtion.setName("FixSD");
        spatialDistrubtion.set(new BasicPoint2D(0, 0));

        JadexConsumerAgentGroup A = new JadexConsumerAgentGroup();
        A.setName("A");
        A.setEnvironment(env);
        A.setSpatialDistribution(spatialDistrubtion);
        A.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("A_awa"));
        A.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("A_int"));
        agentManager.addConsumerAgentGroup(A);

        JadexConsumerAgentGroup B = new JadexConsumerAgentGroup();
        B.setName("B");
        B.setEnvironment(env);
        B.setSpatialDistribution(spatialDistrubtion);
        B.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("B_awa"));
        B.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("B_int"));
        agentManager.addConsumerAgentGroup(B);

        env.getSettings().setInitialNumberOfConsumerAgents(A, 10);
        env.getSettings().setInitialNumberOfConsumerAgents(B, 10);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 1);

        env.createAgents();

        CompleteGraphTopology topology = new CompleteGraphTopology();
        topology.setName("topo");
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setInitialWeight(1);

        assertDoesNotThrow(() -> topology.initalize(env));

        for(ConsumerAgent ca: env.getAgents().iterableConsumerAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(19, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
            }
        }
    }

    @Test
    void testMultiInit() throws InitializationException {
        IRPLogging.disableLogging();

        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.initDefault();
        BasicSocialNetwork network = (BasicSocialNetwork) env.getNetwork();
        network.initDefaultGraph();
        BasicAgentManager agentManager = (BasicAgentManager) env.getAgents();

        FixSpatialDistrubtion spatialDistrubtion = new FixSpatialDistrubtion();
        spatialDistrubtion.setName("FixSD");
        spatialDistrubtion.set(new BasicPoint2D(0, 0));

        JadexConsumerAgentGroup A = new JadexConsumerAgentGroup();
        A.setName("A");
        A.setEnvironment(env);
        A.setSpatialDistribution(spatialDistrubtion);
        A.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("A_awa"));
        A.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("A_int"));
        agentManager.addConsumerAgentGroup(A);

        JadexConsumerAgentGroup B = new JadexConsumerAgentGroup();
        B.setName("B");
        B.setEnvironment(env);
        B.setSpatialDistribution(spatialDistrubtion);
        B.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("B_awa"));
        B.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("B_int"));
        agentManager.addConsumerAgentGroup(B);

        int total = 20;
        env.getSettings().setInitialNumberOfConsumerAgents(A, total / 2);
        env.getSettings().setInitialNumberOfConsumerAgents(B, total / 2);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 1);

        env.createAgents();

        CompleteGraphTopology topology = new CompleteGraphTopology();
        topology.setName("topo");
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setInitialWeight(1);

        assertDoesNotThrow(() -> topology.initalize(env));

        for(ConsumerAgent ca: env.getAgents().iterableConsumerAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(total - 1, li.sum(SocialGraph.Type.COMMUNICATION));
            assertEquals(total - 1, env.getNetwork().getGraph().streamTargets(node, SocialGraph.Type.COMMUNICATION).count());

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
            }
        }

        assertDoesNotThrow(() -> topology.initalize(env));

        for(ConsumerAgent ca: env.getAgents().iterableConsumerAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(total - 1, li.sum(SocialGraph.Type.COMMUNICATION));
            assertEquals(total - 1, env.getNetwork().getGraph().streamTargets(node, SocialGraph.Type.COMMUNICATION).count());

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
            }
        }
    }
}