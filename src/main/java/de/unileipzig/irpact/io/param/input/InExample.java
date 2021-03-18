package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.simulation.tasks.PredefinedSimulationTask;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.*;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.*;
import de.unileipzig.irpact.io.param.input.product.*;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomSelectedGroupedSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irpact.start.optact.network.IFreeMultiGraphTopology;
import de.unileipzig.irpact.start.optact.network.IGraphTopology;
import de.unileipzig.irpact.start.optact.network.IWattsStrogatzModel;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.graphviz.def.GraphvizGlobal;
import de.unileipzig.irptools.graphviz.def.GraphvizLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.def.GraphvizOutputFormat;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public class InExample implements DefaultScenarioFactory {

    public InExample() {
    }

    @Override
    public de.unileipzig.irpact.io.param.input.InRoot createDefaultScenario() {
        return createExample();
    }

    @SuppressWarnings("unused")
    public static de.unileipzig.irpact.io.param.input.InRoot createExample() {
        //===
        InAttributeName A1 = new InAttributeName(RAConstants.PURCHASE_POWER);
        InAttributeName A2 = new InAttributeName(RAConstants.NOVELTY_SEEKING);
        InAttributeName A3 = new InAttributeName(RAConstants.DEPENDENT_JUDGMENT_MAKING);
        InAttributeName A4 = new InAttributeName(RAConstants.ENVIRONMENTAL_CONCERN);
        InAttributeName A5 = new InAttributeName(RAConstants.SHARE_1_2_HOUSE);
        InAttributeName A6 = new InAttributeName(RAConstants.HOUSE_OWNER);
        InAttributeName A7 = new InAttributeName(RAConstants.CONSTRUCTION_RATE);
        InAttributeName A8 = new InAttributeName(RAConstants.RENOVATION_RATE);

        InAttributeName B6 = new InAttributeName(RAConstants.REWIRING_RATE);

        InAttributeName C1 = new InAttributeName(RAConstants.COMMUNICATION_FREQUENCY_SN);

        InAttributeName D1 = new InAttributeName(RAConstants.INITIAL_PRODUCT_INTEREST);
        InAttributeName D2 = new InAttributeName(RAConstants.INTEREST_THRESHOLD);
        InAttributeName D3 = new InAttributeName(RAConstants.FINANCIAL_THRESHOLD);
        InAttributeName D4 = new InAttributeName(RAConstants.ADOPTION_THRESHOLD);
        InAttributeName D5 = new InAttributeName(RAConstants.INITIAL_ADOPTER);

        InAttributeName E1 = new InAttributeName(RAConstants.INVESTMENT_COST);

        InAttributeName Mic_Dominantes_Milieu = new InAttributeName(RAConstants.DOM_MILIEU);
        InAttributeName PLZ = new InAttributeName(RAConstants.ZIP);

        //===
        InUnivariateDoubleDistribution constant0 = new InConstantUnivariateDistribution("constant0", 0);
        InUnivariateDoubleDistribution constant1 = new InConstantUnivariateDistribution("constant1", 1);
        InUnivariateDoubleDistribution constant10 = new InConstantUnivariateDistribution("constant10", 10);
        InUnivariateDoubleDistribution constant01 = new InConstantUnivariateDistribution("constant01", 0.1);
        InUnivariateDoubleDistribution constant09 = new InConstantUnivariateDistribution("constant09", 0.9);
        InUnivariateDoubleDistribution constant42 = new InConstantUnivariateDistribution("constant42", 42);
        InUnivariateDoubleDistribution constant24 = new InConstantUnivariateDistribution("constant24", 24);

        //cag0
        String name = "TRA";
        InGeneralConsumerAgentGroup cag0 = new InGeneralConsumerAgentGroup();
        cag0.setName(name);
        cag0.setInformationAuthority(1);
        cag0.setNumberOfAgents(1);
        InUnivariateDoubleDistribution dist = constant0;
        List<InConsumerAgentGroupAttribute> list = new ArrayList<>();
        createAttribute(cag0, A1, dist, list);
        createAttribute(cag0, A2, dist, list);
        createAttribute(cag0, A3, dist, list);
        createAttribute(cag0, A4, dist, list);
        createAttribute(cag0, A5, dist, list);
        createAttribute(cag0, A6, dist, list);
        createAttribute(cag0, A7, dist, list);
        createAttribute(cag0, A8, dist, list);

        createAttribute(cag0, B6, dist, list);

        createAttribute(cag0, C1, dist, list);

        createAttribute(cag0, D1, dist, list);
        createAttribute(cag0, D2, dist, list);
        createAttribute(cag0, D3, dist, list);
        createAttribute(cag0, D4, dist, list);
        createAttribute(cag0, D5, constant1, list);

        InProductThresholdInterestSupplyScheme cag0_awa = new InProductThresholdInterestSupplyScheme(name + "_awa", constant10);
        cag0.setInterest(cag0_awa);

        //cag1
        name = "BUM";
        InGeneralConsumerAgentGroup cag1 = new InGeneralConsumerAgentGroup();
        dist = constant0;
        list.clear();
        createAttribute(cag1, A1, dist, list);
        createAttribute(cag1, A2, dist, list);
        createAttribute(cag1, A3, dist, list);
        createAttribute(cag1, A4, dist, list);
        createAttribute(cag1, A5, dist, list);
        createAttribute(cag1, A6, dist, list);
        createAttribute(cag1, A7, dist, list);
        createAttribute(cag1, A8, dist, list);

        createAttribute(cag1, B6, dist, list);

        createAttribute(cag1, C1, dist, list);

        createAttribute(cag1, D1, dist, list);
        createAttribute(cag1, D2, dist, list);
        createAttribute(cag1, D3, dist, list);
        createAttribute(cag1, D4, dist, list);
        createAttribute(cag1, D5, dist, list);

        InProductThresholdInterestSupplyScheme cag1_awa = new InProductThresholdInterestSupplyScheme(name + "_awa", constant10);
        cag1.setInterest(cag1_awa);

        //affinity
        InComplexAffinityEntry cag0_cag0 = new InComplexAffinityEntry(cag0.getName() + "_" + cag0.getName(), cag0, cag0, 0.7);
        InComplexAffinityEntry cag0_cag1 = new InComplexAffinityEntry(cag0.getName() + "_" + cag1.getName(), cag0, cag1, 0.3);
        InComplexAffinityEntry cag1_cag1 = new InComplexAffinityEntry(cag1.getName() + "_" + cag1.getName(), cag1, cag1, 0.9);
        InComplexAffinityEntry cag1_cag0 = new InComplexAffinityEntry(cag1.getName() + "_" + cag0.getName(), cag1, cag0, 0.1);

        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("unlinked");

        InPVFile pvFile = new InPVFile("BarwertrechnerMini_ES");

        InNameBasedUncertaintyWithConvergenceGroupAttribute uncert = new InNameBasedUncertaintyWithConvergenceGroupAttribute();
        uncert.setName("Uncerter");
        uncert.cags = new InConsumerAgentGroup[]{cag0, cag1};
        uncert.names = new InAttributeName[]{A2, A3, A4};
        uncert.setUncertaintyDistribution(constant01);
        uncert.setConvergenceDistribution(constant09);

        //process
        InRAProcessModel processModel = new InRAProcessModel(
                "RA",
                0.25, 0.25, 0.25, 0.25,
                3, 2, 1, 0,
                1.0,
                pvFile,
                new InSlopeSupplier[0],
                new InOrientationSupplier[0],
                new InUncertaintyGroupAttribute[]{uncert}
        );

        //Product
        InProductGroupAttribute pv_e1 = new InProductGroupAttribute(
                "PV_E1",
                E1,
                constant09
        );
        InProductGroup pv = new InProductGroup("PV", new InProductGroupAttribute[]{pv_e1});

        InFixProductAttribute fix_pv_e1 = new InFixProductAttribute("PV_E1_fix", pv_e1, 1.0);
        InFixProduct fix_pv = new InFixProduct("PV_fix", pv, new InFixProductAttribute[]{fix_pv_e1});
        InFixProductFindingScheme fixScheme = new InFixProductFindingScheme("PV_fix_scheme", fix_pv);
        cag0.productFindingSchemes = new InProductFindingScheme[]{fixScheme};
        cag1.productFindingSchemes = new InProductFindingScheme[]{fixScheme};

        InSpatialTableFile tableFile = new InSpatialTableFile("210225_Datensatz");
        InCustomSelectedGroupedSpatialDistribution2D spaDist = new InCustomSelectedGroupedSpatialDistribution2D(
                "testdist",
                constant0,
                constant0,
                tableFile,
                Mic_Dominantes_Milieu,
                PLZ
        );
        cag0.spatialDistribution = new InSpatialDistribution[]{spaDist};
        cag1.spatialDistribution = new InSpatialDistribution[]{spaDist};

        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.EUCLIDEAN);

        //time
        InDiscreteTimeModel timeModel = new InDiscreteTimeModel("Discrete", 604800000L);

        //general
        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(5);
        general.runOptActDemo = true;
        general.logLevel = IRPLevel.ALL.getLevelId();
        general.logAll = true;

        //=====
        de.unileipzig.irpact.io.param.input.InRoot root = new de.unileipzig.irpact.io.param.input.InRoot();
        initOptAct(root);
        initGV(root);

        PredefinedSimulationTask task = new PredefinedSimulationTask();
        task.setInfo("INC");
        task.setTask(PredefinedSimulationTask.ADD_ONE_AGENT_TO_EVERY_GROUP);
        VisibleBinaryData vbd = new VisibleBinaryData();
        vbd.setID(task.getID());
        vbd.setBytes(task.getBytes());
        root.visibleBinaryData = new VisibleBinaryData[]{vbd};

        //graphviz
        GraphvizColor gc1 = GraphvizColor.RED;
        GraphvizColor gc2 = GraphvizColor.GREEN;
        root.colors = new GraphvizColor[]{gc1, gc2};

        InConsumerAgentGroupColor cag0Color = new InConsumerAgentGroupColor(cag0.getName() + "_color", cag0, gc1);
        InConsumerAgentGroupColor cag1Color = new InConsumerAgentGroupColor(cag1.getName() + "_color", cag1, gc2);
        root.consumerAgentGroupColors = new InConsumerAgentGroupColor[]{cag0Color, cag1Color};

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
        root.affinityEntries = new InComplexAffinityEntry[]{cag0_cag0, cag0_cag1, cag1_cag1, cag1_cag0};
        root.consumerAgentGroups = new InConsumerAgentGroup[]{cag0, cag1};
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.productGroups = new InProductGroup[]{pv};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};

        return root;
    }

    private static void createAttribute(
            InConsumerAgentGroup cag,
            InAttributeName name,
            InUnivariateDoubleDistribution dist,
            List<InConsumerAgentGroupAttribute> out) {
        InGeneralConsumerAgentGroupAttribute attr = new InGeneralConsumerAgentGroupAttribute(cag.getName() + "_" + name.getName(), cag, name, dist);
        out.add(attr);
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

    private static void initGV(de.unileipzig.irpact.io.param.input.InRoot root) {
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
