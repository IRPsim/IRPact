package de.unileipzig.irpact.io.input;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.awareness.ThresholdAwareness;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.LoggingPart;
import de.unileipzig.irpact.core.log.LoggingPartFilter;
import de.unileipzig.irpact.core.log.LoggingType;
import de.unileipzig.irpact.core.network.BasicGraphConfiguration;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.FreeNetworkTopology;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.io.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.input.network.InNumberOfTies;
import de.unileipzig.irpact.io.input.process.InProcessModel;
import de.unileipzig.irpact.io.input.process.InRAProcessModel;
import de.unileipzig.irpact.io.input.product.InProductGroup;
import de.unileipzig.irpact.io.input.product.InProductGroupAttribute;
import de.unileipzig.irpact.io.input.spatial.InConstantSpatialDistribution2D;
import de.unileipzig.irpact.io.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.input.spatial.InSpatialDistribution;
import de.unileipzig.irpact.io.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.input.time.InTimeModel;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.DiscreteTimeModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class InputParserX {

    private void parseGeneral(InRoot root, BasicJadexSimulationEnvironment environment) {
        long seed = root.general.seed;
        Rnd rnd = seed == -1L ? new Rnd() : new Rnd(seed);
        environment.setSimulationRandom(rnd);

        IRPLogging.initConsole();
        LoggingPartFilter filter = new LoggingPartFilter();
        if(root.general.logGraphCreation) {
            filter.put(LoggingType.INITIALIZATION, LoggingPart.NETWORK);
        }
        if(root.general.logAgentCreation) {
            filter.put(LoggingType.INITIALIZATION, LoggingPart.AGENT);
        }
        IRPLogging.setFilter(filter);
    }

    private void parseConsumerAgentGroups(InRoot root, BasicJadexSimulationEnvironment environment) {
        for(InConsumerAgentGroup inGrp: root.consumerAgentGroups) {
            JadexConsumerAgentGroup jcag = new JadexConsumerAgentGroup();
            jcag.setEnvironment(environment);
            jcag.setName(inGrp.getName());
            jcag.setInformationAuthority(inGrp.getInformationAuthority());
            ThresholdAwareness<Product> awareness = new ThresholdAwareness<>();
            awareness.setThreshold(10);
            jcag.setProductAwareness(awareness);
            for(InConsumerAgentGroupAttribute inAttr: inGrp.getAttributes()) {
                BasicConsumerAgentGroupAttribute cagAttr = new BasicConsumerAgentGroupAttribute();
                cagAttr.setName(inAttr.getCagAttrName().getName());
                cagAttr.setDistribution(inAttr.getCagAttrDistribution().getInstance());
            }
            environment.getAgents().add(jcag);
            environment.getInitializationData().setInitialNumberOfConsumerAgents(jcag, inGrp.getNumberOfAgents());
        }
    }

    private void parseSpatialInformation(InRoot root, BasicJadexSimulationEnvironment environment) {
        for(InSpatialDistribution inSpa: root.spatialDistributions) {
            if(inSpa instanceof InConstantSpatialDistribution2D) {
                InConstantSpatialDistribution2D spa2d = (InConstantSpatialDistribution2D) inSpa;
                SpatialDistribution spa = spa2d.getInstance();
                JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) environment.getAgents()
                        .getConsumerAgentGroup(spa2d.getConsumerAgentGroup().getName());
                jcag.setSpatialDistribution(spa);
            } else {
                throw new IllegalArgumentException("unsupported: " + inSpa.getClass());
            }
        }
    }

    private void parseSpatialModel(InRoot root, BasicJadexSimulationEnvironment environment) {
        InSpatialModel inSpatial = root.spatialModel[0];
        if(inSpatial instanceof InSpace2D) {
            InSpace2D in2D = (InSpace2D) inSpatial;
            Space2D space2D = new Space2D();
            space2D.setName(in2D.getName());
            if(in2D.useEuclid()) {
                space2D.setMetric(Metric2D.EUCLIDEAN);
            } else {
                space2D.setMetric(Metric2D.MANHATTEN);
            }
            environment.setSpatialModel(space2D);
        }
    }

    private void parseAffinity(InRoot root, BasicJadexSimulationEnvironment environment) {
        BasicConsumerAgentGroupAffinityMapping mapping = new BasicConsumerAgentGroupAffinityMapping();
        for(InAffinityEntry entry: root.affinityEntries) {
            ConsumerAgentGroup srcCag = environment.getAgents().getConsumerAgentGroup(entry.srcCag.getName());
            ConsumerAgentGroup tarCag = environment.getAgents().getConsumerAgentGroup(entry.tarCag.getName());
            mapping.put(srcCag, tarCag, entry.affinityValue);
        }
        environment.getAgents().setConsumerAgentGroupAffinityMapping(mapping);
    }

    private void parseNetwork(InRoot root, BasicJadexSimulationEnvironment environment) {
        InGraphTopologyScheme topologyScheme = root.graphTopologySchemes[0];
        if(topologyScheme instanceof InFreeNetworkTopology) {
            InFreeNetworkTopology inFree = (InFreeNetworkTopology) topologyScheme;

            Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
            for(InNumberOfTies entry: inFree.getNumberOfTies()) {
                ConsumerAgentGroup cag = environment.getAgents().getConsumerAgentGroup(entry.getCag().getName());
                int count = entry.getCount();
                edgeCountMap.put(cag, count);
            }
            DistanceEvaluator distanceEvaluator = inFree.getDistanceEvaluator().getInstance();
            Rnd rnd = environment.getSimulationRandom().createNewRandom();

            FreeNetworkTopology topology = new FreeNetworkTopology(
                    SocialGraph.Type.COMMUNICATION,
                    edgeCountMap,
                    environment.getAgents().getConsumerAgentGroupAffinityMapping(),
                    distanceEvaluator,
                    inFree.getInitialWeight(),
                    rnd
            );

            BasicGraphConfiguration configuration = new BasicGraphConfiguration();
            configuration.setGraphTopologyScheme(topology);

            environment.getNetwork().setConfiguration(configuration);
        } else {
            throw new IllegalArgumentException("unsupported: " + topologyScheme.getClass());
        }
    }

    private void parseProcessModel(InRoot root, BasicJadexSimulationEnvironment environment) {
        InProcessModel inPM = root.processModel[0];
        if(inPM instanceof InRAProcessModel) {
            InRAProcessModel inRA = (InRAProcessModel) inPM;

            RAModelData data = new RAModelData();
            data.setA(inRA.getA());
            data.setB(inRA.getB());
            data.setC(inRA.getC());
            data.setD(inRA.getD());
            data.setAdopterPoints(inRA.getAdopterPoints());
            data.setInteresetedPoints(inRA.getInteresetedPoints());
            data.setAwarePoints(inRA.getAwarePoints());
            data.setUnknownPoints(inRA.getUnknownPoints());

            RAProcessModel model = new RAProcessModel();
            model.setEnvironment(environment);
            model.setModelData(data);
            model.setRnd(environment.getSimulationRandom().createNewRandom());

            for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) cag;
                jcag.setProcessFindingScheme(product -> model); //!!!
            }
        } else {
            throw new IllegalArgumentException("unsupported: " + inPM.getClass());
        }
    }

    private void parseProducts(InRoot root, BasicJadexSimulationEnvironment environment) {
        for(InProductGroup pgrp: root.productGroups) {
            BasicProductGroup group = new BasicProductGroup();
            group.setName(pgrp.getName());
            for(InProductGroupAttribute inGrpAttr: pgrp.getAttributes()) {
                BasicProductGroupAttribute pgAttr = new BasicProductGroupAttribute();
                pgAttr.setName(inGrpAttr.getAttrName().getName());
                pgAttr.setDistribution(inGrpAttr.getAttrDistribution().getInstance());
            }
            environment.getProducts().add(group);
        }
    }

    private void parseTimeModel(InRoot root, BasicJadexSimulationEnvironment environment) {
        InTimeModel inTime = root.timeModel[0];
        if(inTime instanceof InDiscreteTimeModel) {
            InDiscreteTimeModel inDisc = (InDiscreteTimeModel) inTime;
            DiscreteTimeModel discModel = new DiscreteTimeModel();
            discModel.setStoredDelta(1L);
            discModel.setStoredTimePerTickInMs(inDisc.getTimePerTickInMs());
            environment.setTimeModel(discModel);
        } else {
            throw new IllegalArgumentException("unsupported: " + inTime.getClass());
        }
    }

    public SimulationEnvironment parse(InRoot root) {
        BasicJadexSimulationEnvironment environment = new BasicJadexSimulationEnvironment();
        parseGeneral(root, environment);
        parseConsumerAgentGroups(root, environment);
        parseAffinity(root, environment);
        parseSpatialInformation(root, environment);
        parseSpatialModel(root, environment);
        parseNetwork(root, environment);
        parseProcessModel(root, environment);
        parseProducts(root, environment);
        parseTimeModel(root, environment);
        return environment;
    }
}
