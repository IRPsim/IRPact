package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.eval.NoDistance;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.DataCounter;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.product.awareness.ProductBinaryAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.distribution.FixSpatialDistrubtion;
import de.unileipzig.irpact.core.spatial.distribution.SpatialDistribution;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class FreeNetworkTopologyTest {

    private static BasicJadexSimulationEnvironment buildTestEnv() throws InitializationException {
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

        JadexConsumerAgentGroup C = new JadexConsumerAgentGroup();
        C.setName("C");
        C.setEnvironment(env);
        C.setSpatialDistribution(spatialDistrubtion);
        C.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("C_awa"));
        C.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("C_int"));
        agentManager.addConsumerAgentGroup(C);

        env.getSettings().setInitialNumberOfConsumerAgents(A, 4);
        env.getSettings().setInitialNumberOfConsumerAgents(B, 5);
        env.getSettings().setInitialNumberOfConsumerAgents(C, 6);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.put(A, A, 1);
        affinityMapping.put(B, B, 1);
        affinityMapping.put(C, C, 1);
        agentManager.setConsumerAgentGroupAffinityMapping(affinityMapping);

        env.createAgents();

        return env;
    }

    private static FreeNetworkTopology createTopo(
            Map<ConsumerAgentGroup, Integer> edgeCountMap,
            BasicJadexSimulationEnvironment env) {
        FreeNetworkTopology topology = new FreeNetworkTopology();
        topology.setName("topo");
        topology.setAffinityMapping(env.getAgents().getConsumerAgentGroupAffinityMapping());
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setEdgeCountMap(edgeCountMap);
        topology.setSelfReferential(false);
        topology.setDistanceEvaluator(new BasicDistanceEvaluator(new NoDistance()));
        topology.setRnd(new Rnd(123));
        topology.setAllowLessEdges(false);
        topology.setInitialWeight(1);

        return topology;
    }

    @Test
    void testNotEnoughAgents() throws InitializationException {
        IRPLogging.disableLogging();

        BasicJadexSimulationEnvironment env = buildTestEnv();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("A"), 5);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("B"), 4);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("C"), 5);

        FreeNetworkTopology topology = createTopo(edgeCountMap, env);

        assertThrows(InitializationException.class, () -> topology.initalize(env));
    }

    @Test
    void testEnoughAgents() throws InitializationException {
        //IRPLogging.initConsole();
        IRPLogging.disableLogging();

        BasicJadexSimulationEnvironment env = buildTestEnv();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("A"), 3);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("B"), 4);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("C"), 5);

        FreeNetworkTopology topology = createTopo(edgeCountMap, env);

        assertDoesNotThrow(() -> topology.initalize(env));

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("A").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(3, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("A"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("B").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(4, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("B"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("C").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(5, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("C"), tarCa.getGroup());
            }
        }
    }

    @Test
    void testMultipleInitalizations() throws InitializationException {
        IRPLogging.disableLogging();

        BasicJadexSimulationEnvironment env = buildTestEnv();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("A"), 1);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("B"), 2);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("C"), 2);

        FreeNetworkTopology topology = createTopo(edgeCountMap, env);

        assertDoesNotThrow(() -> topology.initalize(env));

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("A").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(1, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("A"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("B").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(2, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("B"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("C").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(2, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("C"), tarCa.getGroup());
            }
        }

        assertDoesNotThrow(() -> topology.initalize(env));

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("A").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(1, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("A"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("B").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(2, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("B"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("C").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(2, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("C"), tarCa.getGroup());
            }
        }
    }

    @Test
    void testMultipleInitalizationsWithNewSizes() throws InitializationException {
        IRPLogging.disableLogging();

        BasicJadexSimulationEnvironment env = buildTestEnv();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("A"), 1);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("B"), 2);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("C"), 2);

        FreeNetworkTopology topology = createTopo(edgeCountMap, env);

        assertDoesNotThrow(() -> topology.initalize(env));

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("A").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(1, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("A"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("B").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(2, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("B"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("C").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(2, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("C"), tarCa.getGroup());
            }
        }

        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("A"), 2);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("B"), 4);
        edgeCountMap.put(env.getAgents().secureGetConsumerAgentGroup("C"), 4);

        assertDoesNotThrow(() -> topology.initalize(env));

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("A").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(2, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("A"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("B").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(4, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("B"), tarCa.getGroup());
            }
        }

        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("C").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(4, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("C"), tarCa.getGroup());
            }
        }
    }

    @Test
    void testNotEnoughAgentsWithMultipleCags() throws InitializationException {
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

        JadexConsumerAgentGroup C = new JadexConsumerAgentGroup();
        C.setName("C");
        C.setEnvironment(env);
        C.setSpatialDistribution(spatialDistrubtion);
        C.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("C_awa"));
        C.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("C_int"));
        agentManager.addConsumerAgentGroup(C);

        env.getSettings().setInitialNumberOfConsumerAgents(A, 4);
        env.getSettings().setInitialNumberOfConsumerAgents(B, 5);
        env.getSettings().setInitialNumberOfConsumerAgents(C, 5);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.put(A, A, 1);
        affinityMapping.put(B, B, 1);
        affinityMapping.put(C, C, 0.1);
        affinityMapping.put(C, A, 0.9);

        env.createAgents();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(A, 3);
        edgeCountMap.put(B, 4);
        edgeCountMap.put(C, 9);

        FreeNetworkTopology topology = new FreeNetworkTopology();
        topology.setName("topo");
        topology.setAffinityMapping(affinityMapping);
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setEdgeCountMap(edgeCountMap);
        topology.setSelfReferential(false);
        topology.setDistanceEvaluator(new BasicDistanceEvaluator(new NoDistance()));
        topology.setRnd(new Rnd(123));
        topology.setAllowLessEdges(false);
        topology.setInitialWeight(1);

        assertThrows(InitializationException.class, () -> topology.initalize(env));
    }

    @Test
    void testEnoughAgentsWithMultipleCags() throws InitializationException {
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

        JadexConsumerAgentGroup C = new JadexConsumerAgentGroup();
        C.setName("C");
        C.setEnvironment(env);
        C.setSpatialDistribution(spatialDistrubtion);
        C.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("C_awa"));
        C.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("C_int"));
        agentManager.addConsumerAgentGroup(C);

        env.getSettings().setInitialNumberOfConsumerAgents(A, 4);
        env.getSettings().setInitialNumberOfConsumerAgents(B, 5);
        env.getSettings().setInitialNumberOfConsumerAgents(C, 5);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.put(A, A, 1);
        affinityMapping.put(B, B, 1);
        affinityMapping.put(C, B, 0.1);
        affinityMapping.put(C, A, 0.9);

        env.createAgents();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(A, 3);
        edgeCountMap.put(B, 4);
        edgeCountMap.put(C, 9);

        FreeNetworkTopology topology = new FreeNetworkTopology();
        topology.setName("topo");
        topology.setAffinityMapping(affinityMapping);
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setEdgeCountMap(edgeCountMap);
        topology.setSelfReferential(false);
        topology.setDistanceEvaluator(new BasicDistanceEvaluator(new NoDistance()));
        topology.setRnd(new Rnd(123));
        topology.setAllowLessEdges(false);
        topology.setInitialWeight(1);

        assertDoesNotThrow(() -> topology.initalize(env));
    }

    @Test
    void testEnougAgentsWithOneCag() throws InitializationException {
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

        JadexConsumerAgentGroup C = new JadexConsumerAgentGroup();
        C.setName("C");
        C.setEnvironment(env);
        C.setSpatialDistribution(spatialDistrubtion);
        C.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("C_awa"));
        C.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("C_int"));
        agentManager.addConsumerAgentGroup(C);

        env.getSettings().setInitialNumberOfConsumerAgents(A, 4);
        env.getSettings().setInitialNumberOfConsumerAgents(B, 5);
        env.getSettings().setInitialNumberOfConsumerAgents(C, 6);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.put(A, A, 0.5);
        affinityMapping.put(A, B, 0.5);
        affinityMapping.put(B, B, 1);
        affinityMapping.put(C, C, 0.1);
        affinityMapping.put(C, A, 0.9);

        env.createAgents();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(A, 5);
        edgeCountMap.put(B, 4);
        edgeCountMap.put(C, 5);

        FreeNetworkTopology topology = new FreeNetworkTopology();
        topology.setName("topo");
        topology.setAffinityMapping(affinityMapping);
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setEdgeCountMap(edgeCountMap);
        topology.setSelfReferential(false);
        topology.setDistanceEvaluator(new BasicDistanceEvaluator(new NoDistance()));
        topology.setRnd(new Rnd(123));
        topology.setAllowLessEdges(false);
        topology.setInitialWeight(1);

        assertDoesNotThrow(() -> topology.initalize(env));
    }

    @Disabled
    @Test
    void testWithSimpleDistanceEval() throws InitializationException {
        IRPLogging.disableLogging();

        DistanceEvaluator eval = new DistanceEvaluator() {
            @Override
            public boolean isDisabled() {
                return false;
            }

            @Override
            public double evaluate(double distance) {
                //return 1.0 / distance;
                return distance;
            }
        };

        SpatialDistribution customSpatialDist = new SpatialDistribution() {

            private double xy = 0;

            @Override
            public SpatialInformation drawValue() {
                return new BasicPoint2D(xy++);
            }

            @Override
            public String getName() {
                return "XXXX";
            }
        };

        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.initDefault();
        env.setSpatialModel(new Space2D("2D", Metric2D.EUCLIDEAN2));
        BasicSocialNetwork network = (BasicSocialNetwork) env.getNetwork();
        network.initDefaultGraph();
        BasicAgentManager agentManager = (BasicAgentManager) env.getAgents();

        JadexConsumerAgentGroup A = new JadexConsumerAgentGroup();
        A.setName("A");
        A.setEnvironment(env);
        A.setSpatialDistribution(customSpatialDist);
        A.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("A_awa"));
        A.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("A_int"));
        agentManager.addConsumerAgentGroup(A);

        env.getSettings().setInitialNumberOfConsumerAgents(A, 100);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 1);

        env.createAgents();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(A, 5);

        FreeNetworkTopology topology = new FreeNetworkTopology();
        topology.setName("topo");
        topology.setAffinityMapping(affinityMapping);
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setEdgeCountMap(edgeCountMap);
        topology.setSelfReferential(false);
        topology.setDistanceEvaluator(eval);
        topology.setRnd(new Rnd(123));
        topology.setAllowLessEdges(false);
        topology.setInitialWeight(1);

        assertDoesNotThrow(() -> topology.initalize(env));

        DataCounter<String> tarCounter = new DataCounter<>();
        for(ConsumerAgent ca: env.getAgents().secureGetConsumerAgentGroup("A").getAgents()) {
            SocialGraph.Node node = ca.getSocialGraphNode();
            SocialGraph.LinkageInformation li = env.getNetwork().getGraph().getLinkageInformation(node);
            assertEquals(5, li.sum(SocialGraph.Type.COMMUNICATION));

            for(SocialGraph.Node tarNode: env.getNetwork().getGraph().getTargets(ca.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
                ConsumerAgent tarCa = tarNode.getAgent(ConsumerAgent.class);
                tarCounter.inc(tarCa.getName());
                assertNotSame(ca, tarCa);
                assertSame(env.getAgents().secureGetConsumerAgentGroup("A"), tarCa.getGroup());
            }
        }

        tarCounter.sortValueDescending();
        tarCounter.forEach((name, count) -> System.out.println(name + ": " + count));
    }

    @Disabled
    @Test
    void testWithSimpleDistanceEvalPerformance() throws InitializationException {
        IRPLogging.disableLogging();

        DistanceEvaluator eval = new DistanceEvaluator() {
            @Override
            public boolean isDisabled() {
                return false;
            }

            @Override
            public double evaluate(double distance) {
                //return 1.0 / distance;
                return distance;
            }
        };

        SpatialDistribution customSpatialDist = new SpatialDistribution() {

            private double xy = 0;

            @Override
            public SpatialInformation drawValue() {
                return new BasicPoint2D(xy++);
            }

            @Override
            public String getName() {
                return "XXXX";
            }
        };

        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.initDefault();
        env.setSpatialModel(new Space2D("2D", Metric2D.EUCLIDEAN2));
        BasicSocialNetwork network = (BasicSocialNetwork) env.getNetwork();
        network.initDefaultGraph();
        BasicAgentManager agentManager = (BasicAgentManager) env.getAgents();

        JadexConsumerAgentGroup A = new JadexConsumerAgentGroup();
        A.setName("A");
        A.setEnvironment(env);
        A.setSpatialDistribution(customSpatialDist);
        A.setAwarenessSupplyScheme(new ProductBinaryAwarenessSupplyScheme("A_awa"));
        A.setInterestSupplyScheme(new ProductThresholdInterestSupplyScheme("A_int"));
        agentManager.addConsumerAgentGroup(A);

        env.getSettings().setInitialNumberOfConsumerAgents(A, 20000);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 1);

        env.createAgents();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(A, 20);

        FreeNetworkTopology topology = new FreeNetworkTopology();
        topology.setName("topo");
        topology.setAffinityMapping(affinityMapping);
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setEdgeCountMap(edgeCountMap);
        topology.setSelfReferential(false);
        topology.setDistanceEvaluator(eval);
        topology.setRnd(new Rnd(123));
        topology.setAllowLessEdges(false);
        topology.setInitialWeight(1);

        topology.gcStep = 1000;
        topology.useCase = 3;

        long start = System.currentTimeMillis();
        assertDoesNotThrow(() -> topology.initalize(env));
        System.out.println((System.currentTimeMillis() - start));
    }

    @Disabled
    @Test
    void testLargeGraph() throws InitializationException {
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

        env.getSettings().setInitialNumberOfConsumerAgents(A, 50000);

        BasicConsumerAgentGroupAffinityMapping affinityMapping = new BasicConsumerAgentGroupAffinityMapping();
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 1);

        env.createAgents();

        Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
        edgeCountMap.put(A, 20);

        FreeNetworkTopology topology = new FreeNetworkTopology();
        topology.setName("topo");
        topology.setAffinityMapping(affinityMapping);
        topology.setEdgeType(SocialGraph.Type.COMMUNICATION);
        topology.setEdgeCountMap(edgeCountMap);
        topology.setSelfReferential(false);
        topology.setDistanceEvaluator(new BasicDistanceEvaluator(new NoDistance()));
        topology.setRnd(new Rnd(123));
        topology.setAllowLessEdges(false);
        topology.setInitialWeight(1);

        assertDoesNotThrow(() -> topology.initalize(env));
    }
}