package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.postprocessing.image.d2v.DataToVisualize;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.visualisation.network.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGnuPlotOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InROutputImage;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.network.InCompleteGraphTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedSpatialInformationSupplier;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.irpopt.SideCustom;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irpact.start.optact.network.IFreeMultiGraphTopology;
import de.unileipzig.irpact.start.optact.network.IWattsStrogatzModel;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.graphviz.StandardLayoutAlgorithm;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public class InExample implements DefaultScenarioFactory {

    public InExample() {
    }

    @Override
    public InRoot createDefaultScenario() {
        return createExample();
    }

    @Todo("TASK TESTEN")
    @SuppressWarnings("unused")
    public static InRoot createExample() {
        //===
        InUnivariateDoubleDistribution constant0 = new InDiracUnivariateDistribution("constant0", 0);

        //cag0
        InPVactConsumerAgentGroup cag0 = new InPVactConsumerAgentGroup();
        cag0.setName("TRA");
        cag0.setForAll(constant0);

        //cag1
        InPVactConsumerAgentGroup cag1 = new InPVactConsumerAgentGroup();
        cag1.setName("BUM");
        cag1.setForAll(constant0);

        //Population
        InFixConsumerAgentPopulation populationSize = new InFixConsumerAgentPopulation();
        populationSize.setName("PopSize");
        populationSize.setSize(1);
        populationSize.setConsumerAgentGroups(new InConsumerAgentGroup[]{cag0, cag1});

        //affinity
        InComplexAffinityEntry cag0_cag0 = new InComplexAffinityEntry(cag0.getName() + "_" + cag0.getName(), cag0, cag0, 0.7);
        InComplexAffinityEntry cag0_cag1 = new InComplexAffinityEntry(cag0.getName() + "_" + cag1.getName(), cag0, cag1, 0.3);
        InComplexAffinityEntry cag1_cag1 = new InComplexAffinityEntry(cag1.getName() + "_" + cag1.getName(), cag1, cag1, 0.9);
        InComplexAffinityEntry cag1_cag0 = new InComplexAffinityEntry(cag1.getName() + "_" + cag0.getName(), cag1, cag0, 0.1);

        //InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("unlinked");
        InCompleteGraphTopology topology = new InCompleteGraphTopology("complete", 1.0);

        InPVFile pvFile = new InPVFile("Barwertrechner");

        //process
        InPVactGroupBasedDeffuantUncertainty uncertainty = new InPVactGroupBasedDeffuantUncertainty();
        uncertainty.setName("UNCERT");
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(new InConsumerAgentGroup[]{cag0, cag1});

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA");
        processModel.setDefaultValues();
        processModel.setNodeFilterScheme(new InRAProcessPlanMaxDistanceFilterScheme("RA_maxFilter", 100, true));
        processModel.setPvFile(pvFile);
        processModel.setUncertainty(uncertainty);
        processModel.setSpeedOfConvergence(0.0);

        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");
        InFileBasedPVactMilieuSupplier spaDist = new InFileBasedPVactMilieuSupplier();
        spaDist.setName("testdist");
        spaDist.setFile(tableFile);
        cag0.setSpatialDistribution(spaDist);
        cag1.setSpatialDistribution(spaDist);

        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.EUCLIDEAN);

        //images
        List<InOutputImage> images = new ArrayList<>();
        Collections.addAll(images, InGenericOutputImage.createDefaultImages());
        images.add(new InGnuPlotOutputImage("gnutest1", DataToVisualize.ANNUAL_ZIP));
        images.add(new InGnuPlotOutputImage("gnutest2", DataToVisualize.COMPARED_ANNUAL_ZIP));
        images.add(new InGnuPlotOutputImage("gnutest3", DataToVisualize.CUMULATIVE_ANNUAL_PHASE));
        images.add(new InROutputImage("rtest1", DataToVisualize.ANNUAL_ZIP));
        images.add(new InROutputImage("rtest2", DataToVisualize.COMPARED_ANNUAL_ZIP));
        images.add(new InROutputImage("rtest3", DataToVisualize.CUMULATIVE_ANNUAL_PHASE));
        images.forEach(InOutputImage::disableAll);

        //time
        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel("DiscreteUnitStep", 1, ChronoUnit.WEEKS);

        //general
        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.setLogLevel(IRPLevel.ALL);
        general.logAllIRPact = true;
        general.enableAllDataLogging();
        general.enableAllResultLogging();
        general.enableAllScriptLogging();

        //=====
        InRoot root = new InRoot();
        initOptAct(root);
        initGV(root);

//        PredefinedPostAgentCreationTask task = new PredefinedPostAgentCreationTask();
//        task.setInfo("INC");
//        task.setTask(PredefinedPostAgentCreationTask.ADD_ONE_AGENT_TO_EVERY_GROUP);
//        VisibleBinaryData vbd = new VisibleBinaryData();
//        vbd.setID(task.getID());
//        vbd.setBytes(task.getBytes());
//        root.visibleBinaryData = new VisibleBinaryData[]{vbd};

        //graphviz
        InConsumerAgentGroupColor cag0Color = InConsumerAgentGroupColor.RED.derive(cag0);
        InConsumerAgentGroupColor cag1Color = InConsumerAgentGroupColor.GREEN.derive(cag1);
        root.setConsumerAgentGroupColors(new InConsumerAgentGroupColor[]{cag0Color, cag1Color});

        root.getGraphvizGeneral().setLayoutAlgorithm(StandardLayoutAlgorithm.NEATO);
        root.getGraphvizGeneral().setStoreDotFile(false);

        //=====
        root.version = new InScenarioVersion[]{InScenarioVersion.currentVersion()};
        root.general = general;
        root.setAffinities(new InAffinities("affs", new InComplexAffinityEntry[]{cag0_cag0, cag0_cag1, cag1_cag1, cag1_cag0}));
        root.consumerAgentGroups = new InConsumerAgentGroup[]{cag0, cag1};
        root.setAgentPopulationSize(populationSize);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};
        root.setImages(images);

        return root;
    }

    @Todo("TASK TESTEN")
    @SuppressWarnings("unused")
    public static InRoot createExample_OLD() {
        //===
        InAttributeName Mic_Dominantes_Milieu = new InAttributeName(RAConstants.DOM_MILIEU);
        InAttributeName PLZ = new InAttributeName(RAConstants.ZIP);

        //===
        InUnivariateDoubleDistribution constant0 = new InDiracUnivariateDistribution("constant0", 0);

        //cag0
        InPVactConsumerAgentGroup cag0 = new InPVactConsumerAgentGroup();
        cag0.setName("TRA");
        cag0.setForAll(constant0);

        //cag1
        InPVactConsumerAgentGroup cag1 = new InPVactConsumerAgentGroup();
        cag1.setName("BUM");
        cag1.setForAll(constant0);

        //Population
        InFixConsumerAgentPopulation populationSize = new InFixConsumerAgentPopulation();
        populationSize.setName("PopSize");
        populationSize.setSize(1);
        populationSize.setConsumerAgentGroups(new InConsumerAgentGroup[]{cag0, cag1});

        //affinity
        InComplexAffinityEntry cag0_cag0 = new InComplexAffinityEntry(cag0.getName() + "_" + cag0.getName(), cag0, cag0, 0.7);
        InComplexAffinityEntry cag0_cag1 = new InComplexAffinityEntry(cag0.getName() + "_" + cag1.getName(), cag0, cag1, 0.3);
        InComplexAffinityEntry cag1_cag1 = new InComplexAffinityEntry(cag1.getName() + "_" + cag1.getName(), cag1, cag1, 0.9);
        InComplexAffinityEntry cag1_cag0 = new InComplexAffinityEntry(cag1.getName() + "_" + cag0.getName(), cag1, cag0, 0.1);

        //InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("unlinked");
        InCompleteGraphTopology topology = new InCompleteGraphTopology("complete", 1.0);

        InPVFile pvFile = new InPVFile("Barwertrechner");

        //process
        InPVactGroupBasedDeffuantUncertainty uncertainty = new InPVactGroupBasedDeffuantUncertainty();
        uncertainty.setName("UNCERT");
        uncertainty.setDefaultValues();
        uncertainty.setConsumerAgentGroups(new InConsumerAgentGroup[]{cag0, cag1});

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA");
        processModel.setDefaultValues();
        processModel.setNodeFilterScheme(new InRAProcessPlanMaxDistanceFilterScheme("RA_maxFilter", 100, true));
        processModel.setPvFile(pvFile);
        processModel.setUncertainty(uncertainty);
        processModel.setSpeedOfConvergence(0.0);

        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");
        InFileBasedSpatialInformationSupplier spaDist = new InFileBasedSpatialInformationSupplier();
        spaDist.setName("testdist");
        spaDist.setXPositionKey(new InAttributeName(RAConstants.X_CENT));
        spaDist.setYPositionKey(new InAttributeName(RAConstants.Y_CENT));
        spaDist.setIdKey(new InAttributeName(RAConstants.ID));
        spaDist.setFile(tableFile);
        cag0.setSpatialDistribution(spaDist);
        cag1.setSpatialDistribution(spaDist);

        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.EUCLIDEAN);

        //images
        List<InOutputImage> images = new ArrayList<>();
        Collections.addAll(images, InGenericOutputImage.createDefaultImages());
        images.add(new InGnuPlotOutputImage("gnutest1", DataToVisualize.ANNUAL_ZIP));
        images.add(new InGnuPlotOutputImage("gnutest2", DataToVisualize.COMPARED_ANNUAL_ZIP));
        images.add(new InGnuPlotOutputImage("gnutest3", DataToVisualize.CUMULATIVE_ANNUAL_PHASE));
        images.add(new InROutputImage("rtest1", DataToVisualize.ANNUAL_ZIP));
        images.add(new InROutputImage("rtest2", DataToVisualize.COMPARED_ANNUAL_ZIP));
        images.add(new InROutputImage("rtest3", DataToVisualize.CUMULATIVE_ANNUAL_PHASE));
        images.forEach(InOutputImage::disableAll);

        //time
        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel("DiscreteUnitStep", 1, ChronoUnit.WEEKS);

        //general
        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.setLogLevel(IRPLevel.ALL);
        general.logAllIRPact = true;
        general.enableAllDataLogging();
        general.enableAllResultLogging();
        general.enableAllScriptLogging();

        //=====
        InRoot root = new InRoot();
        initOptAct(root);
        initGV(root);

//        PredefinedPostAgentCreationTask task = new PredefinedPostAgentCreationTask();
//        task.setInfo("INC");
//        task.setTask(PredefinedPostAgentCreationTask.ADD_ONE_AGENT_TO_EVERY_GROUP);
//        VisibleBinaryData vbd = new VisibleBinaryData();
//        vbd.setID(task.getID());
//        vbd.setBytes(task.getBytes());
//        root.visibleBinaryData = new VisibleBinaryData[]{vbd};

        //graphviz
        InConsumerAgentGroupColor cag0Color = InConsumerAgentGroupColor.RED.derive(cag0);
        InConsumerAgentGroupColor cag1Color = InConsumerAgentGroupColor.GREEN.derive(cag1);
        root.setConsumerAgentGroupColors(new InConsumerAgentGroupColor[]{cag0Color, cag1Color});

        root.getGraphvizGeneral().setLayoutAlgorithm(StandardLayoutAlgorithm.NEATO);
        root.getGraphvizGeneral().setStoreDotFile(false);

        //=====
        root.version = new InScenarioVersion[]{InScenarioVersion.currentVersion()};
        root.general = general;
        root.setAffinities(new InAffinities("affs", new InComplexAffinityEntry[]{cag0_cag0, cag0_cag1, cag1_cag1, cag1_cag0}));
        root.consumerAgentGroups = new InConsumerAgentGroup[]{cag0, cag1};
        root.setAgentPopulationSize(populationSize);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};
        root.setImages(images);

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

    public static void initGV(de.unileipzig.irpact.io.param.input.InRoot root) {
//        GraphvizColor gc1 = GraphvizColor.RED;
//        GraphvizColor gc2 = GraphvizColor.GREEN;
//        GraphvizColor gc3 = new GraphvizColor("BLUE", Color.BLUE);
//        GraphvizColor gc4 = GraphvizColor.PINK;
//        root.colors = new GraphvizColor[]{gc1, gc2, gc3, gc4};
//
//        AgentGroup ag1 = new AgentGroup("Gruppe1", 10, gc1);
//        AgentGroup ag2 = new AgentGroup("Gruppe2", 15, gc2);
//        AgentGroup ag3 = new AgentGroup("Gruppe3", 20, gc3);
//        AgentGroup ag4 = new AgentGroup("Gruppe4", 25, gc4);
//        root.agentGroups = new AgentGroup[]{ag1, ag2, ag3, ag4};

        root.getGraphvizGeneral().setLayoutAlgorithm(StandardLayoutAlgorithm.NEATO);
        root.getGraphvizGeneral().setStoreDotFile(false);
    }

    @Todo("default rausgenommen")
    public static void initOptAct(InRoot root) {
//        SideFares SMS = new SideFares("SMS");
//        SideFares NS = new SideFares("NS");
//        SideFares PS = new SideFares("PS");

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
//        global.energy.put(SMS, loadE1, new DoubleTimeSeries("0"));
//        global.energy.put(NS, loadE1, new DoubleTimeSeries("0"));
//        global.energy.put(PS, loadE1, new DoubleTimeSeries("0"));
//        global.energy.put(SMS, loadE2, new DoubleTimeSeries("0"));
//        global.energy.put(NS, loadE2, new DoubleTimeSeries("0"));
//        global.energy.put(PS, loadE2, new DoubleTimeSeries("0"));
        global.marktpreis = new DoubleTimeSeries("0");
        global.zuweisung = Table.newLinked();
        global.zuweisung.put(E, loadE1, 0.0);
        global.zuweisung.put(E, loadE2, 0.0);

        root.global = global;
        root.sectors = new Sector[] {E};
        root.customs = new SideCustom[] {grp1, grp2};
//        root.fares = new SideFares[] {SMS, NS, PS};
        root.dse = new LoadDSE[] {loadE1, loadE2};
        root.deses = new TechDESES[]{techES1};
        root.despv = new TechDESPV[]{techPV1};
    }
}
