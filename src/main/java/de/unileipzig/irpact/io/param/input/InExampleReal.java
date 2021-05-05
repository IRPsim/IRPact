package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.simulation.tasks.PredefinedPostAgentCreationTask;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InNoDistance;
import de.unileipzig.irpact.io.param.input.network.InNumberOfTies;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InPVactUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileSelectedSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irpact.start.optact.network.IFreeMultiGraphTopology;
import de.unileipzig.irpact.start.optact.network.IGraphTopology;
import de.unileipzig.irpact.start.optact.network.IWattsStrogatzModel;
import de.unileipzig.irpact.util.PVactUtil;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.graphviz.def.GraphvizGlobal;
import de.unileipzig.irptools.graphviz.def.GraphvizLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.def.GraphvizOutputFormat;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;

import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Todo
public class InExampleReal implements DefaultScenarioFactory {

    public InExampleReal() {
    }

    @Override
    public InRoot createDefaultScenario() {
        return createExample();
    }

    @SuppressWarnings("unused")
    public static InRoot createExample() {
        //===
        InAttributeName Mic_Dominantes_Milieu = new InAttributeName(RAConstants.DOM_MILIEU);
        InAttributeName PLZ = new InAttributeName(RAConstants.ZIP);

        //===
        InUnivariateDoubleDistribution diraq0 = new InConstantUnivariateDistribution("diraq0", 0);

        InPVactConsumerAgentGroup[] cags = PVactUtil.MILIEUS.stream()
                .map(name -> {
                    InPVactConsumerAgentGroup cag = new InPVactConsumerAgentGroup();
                    cag.setName(name);
                    cag.setForAll(diraq0);
                    return cag;
                })
                .toArray(InPVactConsumerAgentGroup[]::new);

        //Population

        InFixConsumerAgentPopulationSize populationSize = new InFixConsumerAgentPopulationSize();
        populationSize.setName("PopSize");
        populationSize.setSize(1);
        populationSize.setConsumerAgentGroups(cags);

        //affinity
        InAffinityEntry[] entries = InNameSplitAffinityEntry.createAllWithSelfAffinity(cags);

        //InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("unlinked");
        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName("free_topo");
        topology.setDistanceEvaluator(new InNoDistance("No_dist"));
        topology.setInitialWeight(1.0);
        topology.setNumberOfTies(
                new InNumberOfTies("AllTies", cags, 1)
        );

        InPVactUncertaintyGroupAttribute uncert = new InPVactUncertaintyGroupAttribute();
        uncert.setName("PVact_Uncert");
        uncert.setGroups(cags);
        uncert.setNoveltySeekingUncertainty(diraq0);
        uncert.setDependentJudgmentMakingUncertainty(diraq0);
        uncert.setEnvironmentalConcernUncertainty(diraq0);

        //process
        InRAProcessModel processModel = new InRAProcessModel(
                "RA",
                0.25, 0.25, 0.25, 0.25,
                3, 2, 1, 0,
                1.0,
                new InRAProcessPlanMaxDistanceFilterScheme("RA_maxFilter", 100, true),
                PVactUtil.pvFile,
                new InUncertaintyGroupAttribute[]{uncert}
        );

        InFileSelectedSpatialDistribution2D spatialDist = new InFileSelectedSpatialDistribution2D(
                "SpatialDist",
                new InAttributeName(RAConstants.X_CENT),
                new InAttributeName(RAConstants.Y_CENT),
                PVactUtil.tableFile,
                new InAttributeName(RAConstants.DOM_MILIEU)
        );
        spatialDist.setToAll(cags);

        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.EUCLIDEAN);

        //time
        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel("DiscreteUnitStep", 1, ChronoUnit.WEEKS);

        //general
        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.logLevel = IRPLevel.ALL.getLevelId();
        general.logAllIRPact = true;
        general.enableAllDataLogging();

        //=====
        InRoot root = new InRoot();
        initOptAct(root);
        initGV(root);

        PredefinedPostAgentCreationTask task = new PredefinedPostAgentCreationTask();
        task.setInfo("INC");
        task.setTask(PredefinedPostAgentCreationTask.ADD_ONE_AGENT_TO_EVERY_GROUP);
        VisibleBinaryData vbd = new VisibleBinaryData();
        vbd.setID(task.getID());
        vbd.setBytes(task.getBytes());
        root.visibleBinaryData = new VisibleBinaryData[]{vbd};

        //graphviz
        root.colors = new GraphvizColor[]{GraphvizColor.BLACK};

        InConsumerAgentGroupColor[] colors = Arrays.stream(cags)
                .map(cag -> new InConsumerAgentGroupColor(cag.getName() + "_color", cag, GraphvizColor.BLACK))
                .toArray(InConsumerAgentGroupColor[]::new);
        root.setConsumerAgentGroupColors(colors);

        GraphvizLayoutAlgorithm.DOT.useLayout = false;
        GraphvizLayoutAlgorithm.CIRCO.useLayout = true;
        root.layoutAlgorithms = GraphvizLayoutAlgorithm.DEFAULTS;
        GraphvizOutputFormat.PNG.useFormat = true;
        root.outputFormats = new GraphvizOutputFormat[] { GraphvizOutputFormat.PNG };

        root.graphvizGlobal = new GraphvizGlobal();
        root.graphvizGlobal.fixedNeatoPosition = false;
        root.graphvizGlobal.scaleFactor = 0.0;

        //=====
        root.version = new InVersion[]{InVersion.currentVersion()};
        root.general = general;
        root.affinityEntries = entries;
        root.consumerAgentGroups = cags;
        root.setAgentPopulationSize(populationSize);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return root;
    }

    @SuppressWarnings("SameParameterValue")
    private static IWattsStrogatzModel createWattsStrogatzModel(String name, int k, double beta, long seed, boolean use) {
        IWattsStrogatzModel model = new IWattsStrogatzModel();
        model._name = name;
        model.wsmSelfReferential = false;
        model.wsmBeta = beta;
        model.wsmK = k;
        model.wsmSeed = seed;
        model.wsmUseThis = use;
        return model;
    }

    @SuppressWarnings("SameParameterValue")
    private static IFreeMultiGraphTopology createFreeMultiGraphTopology(String name, int edgeCount, long seed, boolean use) {
        IFreeMultiGraphTopology model = new IFreeMultiGraphTopology();
        model._name = name;
        model.ftEdgeCount = edgeCount;
        model.ftSelfReferential = false;
        model.ftSeed = seed;
        model.ftUseThis = use;
        return model;
    }

    private static void initGV(InRoot root) {
        GraphvizColor gc1 = GraphvizColor.RED;
        GraphvizColor gc2 = GraphvizColor.GREEN;
        GraphvizColor gc3 = new GraphvizColor("BLUE", Color.BLUE);
        GraphvizColor gc4 = GraphvizColor.PINK;
        root.colors = new GraphvizColor[]{gc1, gc2, gc3, gc4};

        AgentGroup ag1 = new AgentGroup("Gruppe1", 10, gc1);
        AgentGroup ag2 = new AgentGroup("Gruppe2", 15, gc2);
        AgentGroup ag3 = new AgentGroup("Gruppe3", 20, gc3);
        AgentGroup ag4 = new AgentGroup("Gruppe4", 25, gc4);
        root.agentGroups = new AgentGroup[]{ag1, ag2, ag3, ag4};

        GraphvizLayoutAlgorithm.DOT.useLayout = false;
        GraphvizLayoutAlgorithm.CIRCO.useLayout = true;
        root.layoutAlgorithms = GraphvizLayoutAlgorithm.DEFAULTS;
        GraphvizOutputFormat.PNG.useFormat = true;
        root.outputFormats = new GraphvizOutputFormat[] { GraphvizOutputFormat.PNG };
        root.topologies = new IGraphTopology[] {
                createWattsStrogatzModel("WSM1", 4, 0.0, 42, true),
                createFreeMultiGraphTopology("FREE1", 3, 24, false)
        };

        root.graphvizGlobal = new GraphvizGlobal();
        root.graphvizGlobal.fixedNeatoPosition = false;
        root.graphvizGlobal.scaleFactor = 0.0;
    }

    private static void initOptAct(InRoot root) {
        SideFares SMS = new SideFares("SMS");
        SideFares NS = new SideFares("NS");
        SideFares PS = new SideFares("PS");

        LoadDSE loadE1 = new LoadDSE("load_E1");
        loadE1.ldse = new DoubleTimeSeries("0");

        LoadDSE loadE2 = new LoadDSE("load_E2");
        loadE2.ldse = new DoubleTimeSeries("0");

        Sector E = new Sector("E");

        TechDESES techES1 = new TechDESES("tech_ES1");
        techES1.foerderung = 0.5;

        TechDESPV techPV1 = new TechDESPV("tech_PV1");
        techPV1.a = 25.0;

        SideCustom grp1 = new SideCustom("p1", 10, 5, new DoubleTimeSeries("0"));
        SideCustom grp2 = new SideCustom("p2", 20, 10, new DoubleTimeSeries("0"));

        InGlobal global = new InGlobal();
        global.mwst = 0.19;
        global.de = true;
        global.ch = false;
        global.energy = Table.newLinked();
        global.energy.put(SMS, loadE1, new DoubleTimeSeries("0"));
        global.energy.put(NS, loadE1, new DoubleTimeSeries("0"));
        global.energy.put(PS, loadE1, new DoubleTimeSeries("0"));
        global.energy.put(SMS, loadE2, new DoubleTimeSeries("0"));
        global.energy.put(NS, loadE2, new DoubleTimeSeries("0"));
        global.energy.put(PS, loadE2, new DoubleTimeSeries("0"));
        global.marktpreis = new DoubleTimeSeries("0");
        global.zuweisung = Table.newLinked();
        global.zuweisung.put(E, loadE1, 0.0);
        global.zuweisung.put(E, loadE2, 0.0);

        root.global = global;
        root.sectors = new Sector[] {E};
        root.customs = new SideCustom[] {grp1, grp2};
        root.fares = new SideFares[] {SMS, NS, PS};
        root.dse = new LoadDSE[] {loadE1, loadE2};
        root.deses = new TechDESES[]{techES1};
        root.despv = new TechDESPV[]{techPV1};
    }
}
