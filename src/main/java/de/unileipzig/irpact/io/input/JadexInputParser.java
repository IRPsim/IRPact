package de.unileipzig.irpact.io.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.DirectedMultiGraph;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irpact.core.agent.BasicAgentManager;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.SectionLoggingFilter;
import de.unileipzig.irpact.core.network.BasicGraphConfiguration;
import de.unileipzig.irpact.core.network.BasicSocialGraph;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.FreeNetworkTopology;
import de.unileipzig.irpact.core.process.BasicProcessModelManager;
import de.unileipzig.irpact.core.process.FixProcessModelFindingScheme;
import de.unileipzig.irpact.core.process.ra.RADataSupplier;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.product.BasicProductGroup;
import de.unileipzig.irpact.core.product.BasicProductGroupAttribute;
import de.unileipzig.irpact.core.product.BasicProductManager;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.BasicInitializationData;
import de.unileipzig.irpact.core.simulation.BasicVersion;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.io.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.awareness.InAwareness;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.input.network.InNumberOfTies;
import de.unileipzig.irpact.io.input.process.InOrientationSupplier;
import de.unileipzig.irpact.io.input.process.InProcessModel;
import de.unileipzig.irpact.io.input.process.InRAProcessModel;
import de.unileipzig.irpact.io.input.process.InSlopeSupplier;
import de.unileipzig.irpact.io.input.product.InProductGroup;
import de.unileipzig.irpact.io.input.product.InProductGroupAttribute;
import de.unileipzig.irpact.io.input.spatial.InConstantSpatialDistribution2D;
import de.unileipzig.irpact.io.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.input.spatial.InSpatialDistribution;
import de.unileipzig.irpact.io.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.input.time.InTimeModel;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.DiscreteTimeModel;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class JadexInputParser implements InputParser<JadexSimulationEnvironment> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JadexInputParser.class);

    public JadexInputParser() {
    }

    @Override
    public JadexSimulationEnvironment parse(InRoot root, ObjectNode jsonRoot) throws Exception {
        ParsingJob job = new ParsingJob(root, jsonRoot);
        return job.run();
    }

    /**
     * @author Daniel Abitz
     */
    private static class ParsingJob {

        @SuppressWarnings({"FieldCanBeLocal", "unused"})
        private final ObjectNode jsonRoot;
        private final InRoot root;
        private final BasicJadexSimulationEnvironment environment = new BasicJadexSimulationEnvironment();
        private final BasicInitializationData initData = new BasicInitializationData();
        private final BasicAgentManager agentManager = new BasicAgentManager();
        private final BasicSocialNetwork socialNetwork = new BasicSocialNetwork();
        private final BasicGraphConfiguration graphConfiguration = new BasicGraphConfiguration();
        private final BasicProcessModelManager processModelManager = new BasicProcessModelManager();
        private final BasicProductManager productManager = new BasicProductManager();
        private final BasicJadexLifeCycleControl lifeCycleControl = new BasicJadexLifeCycleControl();

        private ParsingJob(InRoot root, ObjectNode jsonRoot) {
            this.root = root;
            this.jsonRoot = jsonRoot;

            environment.setInitializationData(initData);

            environment.setAgentManager(agentManager);
            agentManager.setEnvironment(environment);

            environment.setSocialNetwork(socialNetwork);
            socialNetwork.setEnvironment(environment);
            socialNetwork.setConfiguration(graphConfiguration);

            environment.setProductManager(productManager);
            productManager.setEnvironment(environment);

            environment.setProcessModels(processModelManager);
            processModelManager.setEnvironment(environment);

            environment.setLifeCycleControl(lifeCycleControl);
            lifeCycleControl.setEnvironment(environment);
        }

        private static void log(String msg) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, msg);
        }

        private static void log(String pattern, Object arg) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, pattern, arg);
        }

        @SuppressWarnings("SameParameterValue")
        private static void log(String pattern, Object arg1, Object arg2) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, pattern, arg1, arg2);
        }

        @SuppressWarnings("SameParameterValue")
        private static void log(String pattern, Object... args) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, pattern, args);
        }

        private JadexSimulationEnvironment run() {
            parseVersion();
            parseLoggingSetup();
            parseSeed();
            parseLifeCycleControl();

            parseConsumerAgentGroups();
            parseAffinity();
            parseSpatialInformation();
            parseNetwork();
            parseSocialGraph();

            parseSpatialModel();
            parseProcessModel();
            parseProducts();
            parseTimeModel();
            return environment;
        }

        private void parseVersion() {
            InVersion inV = root.version[0];
            BasicVersion v = new BasicVersion();
            v.setVersion(inV.getVersion());
            environment.setVersion(v);
        }

        private void parseLoggingSetup() {
            IRPLevel level = IRPLevel.get(root.general.logLevel);
            if(level == null) {
                LOGGER.warn("invalid log level {}, set level to default ({}) ", root.general.logLevel, IRPLevel.getDefault());
                level = IRPLevel.getDefault();
            }
            IRPLogging.setLevel(level);

            SectionLoggingFilter filter = new SectionLoggingFilter();
            IRPLogging.setFilter(filter);

            if(root.general.logParamInit) {
                filter.add(IRPSection.INITIALIZATION_PARAMETER);
            }
            if(root.general.logAgentCreation) {
                filter.add(IRPSection.INITIALIZATION_AGENT);
            }
            if(root.general.logGraphCreation) {
                filter.add(IRPSection.INITIALIZATION_NETWORK);
            }
            if(root.general.logPlatformCreation) {
                filter.add(IRPSection.INITIALIZATION_PLATFORM);
            }
            if(root.general.logTools) {
                filter.add(IRPSection.TOOLS);
            }
        }

        private void parseSeed() {
            long seed = root.general.seed;
            final Rnd rnd;
            if(seed == -1L) {
                long rndSeed = Rnd.randomSeed();
                LOGGER.info("use random master seed: {}", rndSeed);
                rnd = new Rnd(rndSeed);
            } else {
                LOGGER.info("use master seed: {}", seed);
                rnd = new Rnd(seed);
            }
            environment.setSimulationRandom(rnd);
        }

        private void parseLifeCycleControl() {
            long timeout = root.general.timeout;
            if(timeout < 1L) {
                log("timeout disabled");
            } else {
                log("timeout: {}", timeout);
            }
            lifeCycleControl.setKillSwitchTimeout(timeout);

            int endYear = root.general.endYear;
            initData.setEndYear(endYear);
            log("custom endyear: {}", endYear);
        }

        private void parseConsumerAgentGroups() {
            for(InConsumerAgentGroup inCag: root.consumerAgentGroups) {
                JadexConsumerAgentGroup jcag = new JadexConsumerAgentGroup();
                jcag.setEnvironment(environment);
                jcag.setName(inCag.getName());
                jcag.setInformationAuthority(inCag.getInformationAuthority());
                if(agentManager.hasConsumerAgentGroup(jcag.getName())) {
                    throw new IllegalArgumentException("ConsumerAgentGroup '" + jcag.getName() + "' already exists");
                }

                InAwareness inAwa = inCag.getAwareness();
                Awareness<Product> awa = inAwa.createInstance();
                jcag.setProductAwareness(awa);

                for(InConsumerAgentGroupAttribute inCagAttr: inCag.getAttributes()) {
                    BasicConsumerAgentGroupAttribute bCagAttr = new BasicConsumerAgentGroupAttribute();
                    bCagAttr.setName(inCagAttr.getCagAttrName().getName());
                    if(jcag.hasGroupAttribute(bCagAttr.getName())) {
                        throw new IllegalArgumentException("ConsumerAgentGroupAttribute '" + bCagAttr.getName() + "' already exists in " + jcag.getName());
                    }

                    InUnivariateDoubleDistribution inDist = inCagAttr.getCagAttrDistribution();
                    UnivariateDoubleDistribution dist = inDist.getInstance(environment.getSimulationRandom());
                    bCagAttr.setDistribution(dist);

                    log("add ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", bCagAttr.getName(), inCagAttr.getName(), jcag.getName());
                    jcag.addGroupAttribute(bCagAttr);
                }

                log("add ConsumerAgentGroup '{}'", jcag.getName());
                agentManager.addConsumerAgentGroup(jcag);
                initData.setInitialNumberOfConsumerAgents(jcag, inCag.getNumberOfAgents());
            }
        }

        private void parseAffinity() {
            BasicConsumerAgentGroupAffinityMapping mapping = new BasicConsumerAgentGroupAffinityMapping();
            for(InAffinityEntry entry: root.affinityEntries) {
                ConsumerAgentGroup srcCag = agentManager.getConsumerAgentGroup(entry.srcCag.getName());
                if(srcCag == null) {
                    throw new IllegalArgumentException("Affinity-Source-ConsumerGroup '" + entry.srcCag.getName() + "' not found");
                }
                ConsumerAgentGroup tarCag = agentManager.getConsumerAgentGroup(entry.tarCag.getName());
                if(tarCag == null) {
                    throw new IllegalArgumentException("Affinity-Target-ConsumerGroup '" + entry.tarCag.getName() + "' not found");
                }
                mapping.put(srcCag, tarCag, entry.affinityValue);
            }

            agentManager.setConsumerAgentGroupAffinityMapping(mapping);
        }

        private void parseSpatialInformation() {
            for(InSpatialDistribution inSpa: root.spatialDistributions) {
                if(inSpa instanceof InConstantSpatialDistribution2D) {
                    parseInConstantSpatialDistribution2D((InConstantSpatialDistribution2D) inSpa);
                }
                else {
                    throw new IllegalArgumentException("unsupported InSpatialDistribution: " + inSpa.getClass());
                }
            }
        }

        private void parseInConstantSpatialDistribution2D(InConstantSpatialDistribution2D inSpa) {
            JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) agentManager.getConsumerAgentGroup(inSpa.getConsumerAgentGroup().getName());
            if(jcag == null) {
                throw new IllegalArgumentException("SpatialDistribution-ConsumerGroup '" + inSpa.getConsumerAgentGroup().getName() + "' not found");
            }
            SpatialDistribution spa = inSpa.getInstance();
            jcag.setSpatialDistribution(spa);
        }

        private void parseNetwork() {
            InGraphTopologyScheme inTopo = root.graphTopologySchemes[0];
            if(inTopo instanceof InFreeNetworkTopology) {
                parseInFreeNetworkTopology((InFreeNetworkTopology) inTopo);
            }
            else {
                throw new IllegalArgumentException("unsupported InGraphTopologyScheme: " + inTopo.getClass());
            }
        }

        private void parseInFreeNetworkTopology(InFreeNetworkTopology inTopo) {
            log("use FreeNetworkTopology");
            Map<ConsumerAgentGroup, Integer> edgeCountMap = new HashMap<>();
            for(InNumberOfTies entry: inTopo.getNumberOfTies()) {
                ConsumerAgentGroup cag = agentManager.getConsumerAgentGroup(entry.getCag().getName());
                if(cag == null) {
                    throw new IllegalArgumentException("NumberOfTies-ConsumerGroup '" + entry.getCag().getName() + "' not found");
                }
                edgeCountMap.put(cag, entry.getCount());
            }

            if(edgeCountMap.size() != agentManager.getConsumerAgentGroups().size()) {
                Set<ConsumerAgentGroup> cagSet = new HashSet<>(agentManager.getConsumerAgentGroups());
                cagSet.removeAll(edgeCountMap.keySet());
                for(ConsumerAgentGroup cag: cagSet) {
                    LOGGER.error("missing NumberOfTies-ConsumerGroup: '{}'", cag.getName());
                }
                throw new IllegalArgumentException(cagSet.size() + " NumberOfTies-ConsumerGroup(s) missing");
            }

            DistanceEvaluator eval = inTopo.getDistanceEvaluator().getInstance();
            Rnd rnd = environment.getSimulationRandom().createNewRandom();
            log("InFreeNetworkTopology rnd-seed: {}", rnd.getInitialSeed());

            FreeNetworkTopology topo = new FreeNetworkTopology(
                    SocialGraph.Type.COMMUNICATION,
                    edgeCountMap,
                    agentManager.getConsumerAgentGroupAffinityMapping(),
                    eval,
                    inTopo.getInitialWeight(),
                    rnd
            );

            graphConfiguration.setGraphTopologyScheme(topo);
        }

        private void parseSocialGraph() {
            log("(fixed) graph: DirectedAdjacencyListMultiGraph");
            DirectedMultiGraph<SocialGraph.Node, SocialGraph.Edge, SocialGraph.Type> graph = new DirectedAdjacencyListMultiGraph<>();
            BasicSocialGraph socialGraph = new BasicSocialGraph(graph);
            socialNetwork.setGraph(socialGraph);
        }

        private void parseSpatialModel() {
            InSpatialModel inSpa = root.spatialModel[0];
            if(inSpa instanceof InSpace2D) {
                parseInSpace2D((InSpace2D) inSpa);
            }
            else {
                throw new IllegalArgumentException("unsupported InSpatialModel: " + inSpa.getClass());
            }
        }

        private void parseInSpace2D(InSpace2D inSpa) {
            log("use Space2D");
            Space2D space2D = new Space2D();
            space2D.setName(inSpa.getName());
            if(inSpa.useEuclid()) {
                space2D.setMetric(Metric2D.EUCLIDEAN);
            } else {
                space2D.setMetric(Metric2D.MANHATTEN);
            }
            log("Metric: {}", space2D.getMetric());
            environment.setSpatialModel(space2D);
        }

        private void parseProcessModel() {
            InProcessModel inPM = root.processModel[0];
            if(inPM instanceof InRAProcessModel) {
                parseInRAProcessModel((InRAProcessModel) inPM);
            }
            else {
                throw new IllegalArgumentException("unsupported InProcessModel: " + inPM.getClass());
            }
        }

        private void parseInRAProcessModel(InRAProcessModel inPM) {
            log("use RAProcessModel");
            RAModelData data = new RAModelData();
            data.setA(inPM.getA());
            data.setB(inPM.getB());
            data.setC(inPM.getC());
            data.setD(inPM.getD());
            data.setAdopterPoints(inPM.getAdopterPoints());
            data.setInterestedPoints(inPM.getInterestedPoints());
            data.setAwarePoints(inPM.getAwarePoints());
            data.setUnknownPoints(inPM.getUnknownPoints());

            Rnd rnd = environment.getSimulationRandom().createNewRandom();
            log("RAProcessModel rnd-seed: {}", rnd.getInitialSeed());

            RAProcessModel model = new RAProcessModel();
            model.setName(inPM.getName());
            model.setEnvironment(environment);
            model.setModelData(data);
            model.setRnd(rnd);

            //momentan erstmal fix
            FixProcessModelFindingScheme findingScheme = new FixProcessModelFindingScheme();
            findingScheme.setModel(model);

            for(ConsumerAgentGroup cag: agentManager.getConsumerAgentGroups()) {
                JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) cag;
                jcag.setProcessFindingScheme(findingScheme);
            }

            if(processModelManager.hasProcessModel(model.getName())) {
                throw new IllegalArgumentException("model name '" + model.getName() + "' already exists");
            } else {
                processModelManager.addProcessModel(model);
            }

            parseInOrientationSupplier(model);
            parseInSlopeSupplier(model);
        }

        private void parseInOrientationSupplier(RAProcessModel model) {
            if(root.orientationSupplier.length > 0) {
                log("parse orientation supplier for '{}'", model.getName());
                InOrientationSupplier inOri = root.orientationSupplier[0];
                UnivariateDoubleDistribution dist = inOri.getDistribution().getInstance(environment.getSimulationRandom());

                RADataSupplier oriSupp = new RADataSupplier();
                oriSupp.setName(inOri.getName());
                oriSupp.setDistribution(dist);
                model.setOrientationSupplier(oriSupp);
            }
        }

        private void parseInSlopeSupplier(RAProcessModel model) {
            if(root.slopeSupplier.length > 0) {
                log("parse slope supplier for '{}'", model.getName());
                InSlopeSupplier inOri = root.slopeSupplier[0];
                UnivariateDoubleDistribution dist = inOri.getDistribution().getInstance(environment.getSimulationRandom());

                RADataSupplier oriSupp = new RADataSupplier();
                oriSupp.setName(inOri.getName());
                oriSupp.setDistribution(dist);
                model.setSlopeSupplier(oriSupp);
            }
        }

        private void parseProducts() {
            for(InProductGroup inPg: root.productGroups) {
                BasicProductGroup bPg = new BasicProductGroup();
                bPg.setName(inPg.getName());
                if(productManager.has(bPg.getName())) {
                    throw new IllegalArgumentException("ProductGroup '" + bPg.getName() + "' already exists");
                }

                for(InProductGroupAttribute inPgAttr: inPg.getAttributes()) {
                    BasicProductGroupAttribute bPgAttr = new BasicProductGroupAttribute();
                    bPgAttr.setName(inPgAttr.getAttrName().getName());
                    if(bPg.hasAttribute(bPgAttr.getName())) {
                        throw new IllegalArgumentException("ProductGroupAttribute '" + bPgAttr.getName() + "' already exists in " + bPg.getName());
                    }

                    InUnivariateDoubleDistribution inDist = inPgAttr.getAttrDistribution();
                    UnivariateDoubleDistribution dist = inDist.getInstance(environment.getSimulationRandom());
                    bPgAttr.setDistribution(dist);

                    log("add ProductGroupAttribute '{}' ('{}') to group '{}'", bPgAttr.getName(), inPgAttr.getName(), bPg.getName());
                    bPg.addAttribute(bPgAttr);
                }

                log("add ProductGroup '{}'", bPg.getName());
                productManager.add(bPg);
            }
        }

        private void parseTimeModel() {
            InTimeModel inTime = root.timeModel[0];
            if(inTime instanceof InDiscreteTimeModel) {
                parseInDiscreteTimeModel((InDiscreteTimeModel) inTime);
            }
            else {
                throw new IllegalArgumentException("unsupported InTimeModel: " + inTime.getClass());
            }
        }

        private void parseInDiscreteTimeModel(InDiscreteTimeModel inTime) {
            log("use DiscreteTimeModel");
            DiscreteTimeModel timeModel = new DiscreteTimeModel();
            timeModel.setEnvironment(environment);
            timeModel.setStoredDelta(1L);
            timeModel.setStoredTimePerTickInMs(inTime.getTimePerTickInMs());
            environment.setTimeModel(timeModel);
        }
    }
}
