package de.unileipzig.irpact.core.network.topology;

import de.unileipzig.irpact.commons.eval.NoDistance;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.product.awareness.ProductBinaryAwarenessSupplyScheme;
import de.unileipzig.irpact.core.product.interest.ProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.core.spatial.distribution.FixSpatialDistrubtion;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
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

    @Test
    void testNotEnoughAgents() throws InitializationException {
        IRPLogging.initConsole();
        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.initDefault();
        BasicSocialNetwork network = (BasicSocialNetwork) env.getNetwork();
        network.initDefaultGraph();
        BasicSocialGraph graph = (BasicSocialGraph) network.getGraph();
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
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 0);
        affinityMapping.put(A, A, 1);
        affinityMapping.put(B, B, 1);
        affinityMapping.put(C, C, 1);

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

        assertThrows(InitializationException.class, () -> topology.initalize(env));
    }

    @Test
    void testNotEnoughAgentsWithMultipleCags() throws InitializationException {
        IRPLogging.initConsole();
        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.initDefault();
        BasicSocialNetwork network = (BasicSocialNetwork) env.getNetwork();
        network.initDefaultGraph();
        BasicSocialGraph graph = (BasicSocialGraph) network.getGraph();
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
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 0);
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
        IRPLogging.initConsole();
        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.initDefault();
        BasicSocialNetwork network = (BasicSocialNetwork) env.getNetwork();
        network.initDefaultGraph();
        BasicSocialGraph graph = (BasicSocialGraph) network.getGraph();
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
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 0);
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
        IRPLogging.initConsole();
        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        env.initDefault();
        BasicSocialNetwork network = (BasicSocialNetwork) env.getNetwork();
        network.initDefaultGraph();
        BasicSocialGraph graph = (BasicSocialGraph) network.getGraph();
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
        affinityMapping.putForAll(agentManager.getConsumerAgentGroups(), 0);
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
    void testLargeGraph() throws InitializationException {
        IRPLogging.disableAll();
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

        env.getSettings().setInitialNumberOfConsumerAgents(A, 100000);

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