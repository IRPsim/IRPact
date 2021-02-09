package de.unileipzig.irpact.io.input;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.io.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.input.network.*;
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
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.graphviz.def.GraphvizGlobal;
import de.unileipzig.irptools.graphviz.def.GraphvizLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.def.GraphvizOutputFormat;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;

import java.util.ArrayList;
import java.util.List;

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

    public static InRoot createExample() {
        //===
        InAttributeName A1 = new InAttributeName(RAConstants.PURCHASE_POWER);
        InAttributeName A2 = new InAttributeName(RAConstants.NOVELTY_SEEKING);
        InAttributeName A2_1 = new InAttributeName(RAConstants.NOVELTY_SEEKING_UNCERTAINTY);
        InAttributeName A2_2 = new InAttributeName(RAConstants.NOVELTY_SEEKING_CONVERGENCE);
        InAttributeName A3 = new InAttributeName(RAConstants.DEPENDENT_JUDGMENT_MAKING);
        InAttributeName A3_1 = new InAttributeName(RAConstants.DEPENDENT_JUDGMENT_MAKING_UNCERTAINTY);
        InAttributeName A3_2 = new InAttributeName(RAConstants.DEPENDENT_JUDGMENT_MAKING_CONVERGENCE);
        InAttributeName A4 = new InAttributeName(RAConstants.ENVIRONMENTAL_CONCERN);
        InAttributeName A4_1 = new InAttributeName(RAConstants.ENVIRONMENTAL_CONCERN_UNCERTAINTY);
        InAttributeName A4_2 = new InAttributeName(RAConstants.ENVIRONMENTAL_CONCERN_CONVERGENCE);
        InAttributeName A5 = new InAttributeName(RAConstants.SHARE_1_2_HOUSE);
        InAttributeName A6 = new InAttributeName(RAConstants.HOUSE_OWNER);
        InAttributeName A7 = new InAttributeName(RAConstants.CONSTRUCTION_RATE);
        InAttributeName A8 = new InAttributeName(RAConstants.RENOVATION_RATE);

        InAttributeName C1 = new InAttributeName(RAConstants.COMMUNICATION_FREQUENCY_SN);

        InAttributeName D1 = new InAttributeName(RAConstants.INITIAL_PRODUCT_AWARENESS);
        InAttributeName D2 = new InAttributeName(RAConstants.INTEREST_THRESHOLD);
        InAttributeName D3 = new InAttributeName(RAConstants.FINANCIAL_THRESHOLD);
        InAttributeName D4 = new InAttributeName(RAConstants.ADOPTION_THRESHOLD);

        InAttributeName ORIENTATION = new InAttributeName(RAConstants.ORIENTATION);
        InAttributeName SLOPE = new InAttributeName(RAConstants.SLOPE);

        InAttributeName E1 = new InAttributeName("E1");

        //===
        InUnivariateDoubleDistribution constant01 = new InConstantUnivariateDistribution("constant01", 0.1);
        InUnivariateDoubleDistribution constant09 = new InConstantUnivariateDistribution("constant09", 0.9);
        InUnivariateDoubleDistribution constant42 = new InConstantUnivariateDistribution("constant42", 42);
        InUnivariateDoubleDistribution constant24 = new InConstantUnivariateDistribution("constant24", 24);

        //cag0
        String name = "cag0";
        InUnivariateDoubleDistribution dist = constant01;
        List<InConsumerAgentGroupAttribute> list = new ArrayList<>();
        InConsumerAgentGroupAttribute cag0_A1_attr = build(name, A1, dist, list);
        InConsumerAgentGroupAttribute cag0_A2_attr = build(name, A2, dist, list);
        InConsumerAgentGroupAttribute cag0_A2_1_attr = build(name, A2_1, dist, list);
        InConsumerAgentGroupAttribute cag0_A2_2_attr = build(name, A2_2, dist, list);
        InConsumerAgentGroupAttribute cag0_A3_attr = build(name, A3, dist, list);
        InConsumerAgentGroupAttribute cag0_A3_1_attr = build(name, A3_1, dist, list);
        InConsumerAgentGroupAttribute cag0_A3_2_attr = build(name, A3_2, dist, list);
        InConsumerAgentGroupAttribute cag0_A4_attr = build(name, A4, dist, list);
        InConsumerAgentGroupAttribute cag0_A4_1_attr = build(name, A4_1, dist, list);
        InConsumerAgentGroupAttribute cag0_A4_2_attr = build(name, A4_2, dist, list);
        InConsumerAgentGroupAttribute cag0_A5_attr = build(name, A5, dist, list);
        InConsumerAgentGroupAttribute cag0_A6_attr = build(name, A6, dist, list);
        InConsumerAgentGroupAttribute cag0_A7_attr = build(name, A7, dist, list);
        InConsumerAgentGroupAttribute cag0_A8_attr = build(name, A8, dist, list);

        InConsumerAgentGroupAttribute cag0_C1_attr = build(name, C1, dist, list);

        InConsumerAgentGroupAttribute cag0_D1_attr = build(name, D1, dist, list);
        InConsumerAgentGroupAttribute cag0_D2_attr = build(name, D2, dist, list);
        InConsumerAgentGroupAttribute cag0_D3_attr = build(name, D3, dist, list);
        InConsumerAgentGroupAttribute cag0_D4_attr = build(name, D4, dist, list);

        InConsumerAgentGroup cag0 = new InConsumerAgentGroup(name, 1.0, 8, list);

        //cag1
        name = "cag1";
        dist = constant09;
        list.clear();
        InConsumerAgentGroupAttribute cag1_A1_attr = build(name, A1, dist, list);
        InConsumerAgentGroupAttribute cag1_A2_attr = build(name, A2, dist, list);
        InConsumerAgentGroupAttribute cag1_A2_1_attr = build(name, A2_1, dist, list);
        InConsumerAgentGroupAttribute cag1_A2_2_attr = build(name, A2_2, dist, list);
        InConsumerAgentGroupAttribute cag1_A3_attr = build(name, A3, dist, list);
        InConsumerAgentGroupAttribute cag1_A3_1_attr = build(name, A3_1, dist, list);
        InConsumerAgentGroupAttribute cag1_A3_2_attr = build(name, A3_2, dist, list);
        InConsumerAgentGroupAttribute cag1_A4_attr = build(name, A4, dist, list);
        InConsumerAgentGroupAttribute cag1_A4_1_attr = build(name, A4_1, dist, list);
        InConsumerAgentGroupAttribute cag1_A4_2_attr = build(name, A4_2, dist, list);
        InConsumerAgentGroupAttribute cag1_A5_attr = build(name, A5, dist, list);
        InConsumerAgentGroupAttribute cag1_A6_attr = build(name, A6, dist, list);
        InConsumerAgentGroupAttribute cag1_A7_attr = build(name, A7, dist, list);
        InConsumerAgentGroupAttribute cag1_A8_attr = build(name, A8, dist, list);

        InConsumerAgentGroupAttribute cag1_C1_attr = build(name, C1, dist, list);

        InConsumerAgentGroupAttribute cag1_D1_attr = build(name, D1, dist, list);
        InConsumerAgentGroupAttribute cag1_D2_attr = build(name, D2, dist, list);
        InConsumerAgentGroupAttribute cag1_D3_attr = build(name, D3, dist, list);
        InConsumerAgentGroupAttribute cag1_D4_attr = build(name, D4, dist, list);

        InConsumerAgentGroup cag1 = new InConsumerAgentGroup(name, 1.0, 12, list);

        //affinity
        InAffinityEntry cag0_cag0 = new InAffinityEntry("cag0_cag0", cag0, cag0, 0.7);
        InAffinityEntry cag0_cag1 = new InAffinityEntry("cag0_cag1", cag0, cag1, 0.3);
        InAffinityEntry cag1_cag1 = new InAffinityEntry("cag1_cag1", cag1, cag1, 0.9);
        InAffinityEntry cag1_cag0 = new InAffinityEntry("cag1_cag0", cag1, cag0, 0.1);

        //network
        InNumberOfTies cag0_ties = new InNumberOfTies("cag0_ties", cag0, 3);
        InNumberOfTies cag1_ties = new InNumberOfTies("cag1_ties", cag1, 4);

        InNoDistance[] noDistances = new InNoDistance[]{new InNoDistance("NoDistance")};
        InNumberOfTies[] numberOfTies = new InNumberOfTies[]{cag0_ties, cag1_ties};
        InFreeNetworkTopology topology = new InFreeNetworkTopology(
                "FreeTopo",
                noDistances,
                numberOfTies,
                1.0
        );

        //process
        InRAProcessModel processModel = new InRAProcessModel(
                "RA",
                0.25, 0.25, 0.25, 0.25,
                3, 2, 1, 0
        );
        InOrientationSupplier orientationSupplier = new InOrientationSupplier(
                "OrientationSupplier",
                ORIENTATION,
                constant24
        );
        InSlopeSupplier slopeSupplier = new InSlopeSupplier(
                "SlopeSupplier",
                SLOPE,
                constant42
        );

        //Product
        InProductGroupAttribute pv_e1 = new InProductGroupAttribute(
                "PV_E1",
                E1,
                constant09
        );
        InProductGroup pv = new InProductGroup("PV", new InProductGroupAttribute[]{pv_e1});

        //spatial
        InConstantSpatialDistribution2D cag0_spatial = new InConstantSpatialDistribution2D("cag0_spatial", cag0, 0, 1);
        InConstantSpatialDistribution2D cag1_spatial = new InConstantSpatialDistribution2D("cag1_spatial", cag1, 1, 0);

        InSpace2D space2D = new InSpace2D("Space2D");

        //time
        InDiscreteTimeModel timeModel = new InDiscreteTimeModel("Discrete", 604800000L);

        //general
        InGeneral general = new InGeneral();
        general.seed = 42;
        general.logGraphCreation = false;
        general.logAgentCreation = false;
        general.runOptActDemo = true;

        //=====
        InRoot root = new InRoot();
        initOptAct(root);

        //graphviz
        GraphvizColor gc1 = GraphvizColor.RED;
        GraphvizColor gc2 = GraphvizColor.GREEN;
        root.colors = new GraphvizColor[]{gc1, gc2};

        InConsumerAgentGroupColor cag0Color = new InConsumerAgentGroupColor("cag0_color", cag0, gc1);
        InConsumerAgentGroupColor cag1Color = new InConsumerAgentGroupColor("cag1_color", cag1, gc2);
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
        root.general = general;
        root.affinityEntries = new InAffinityEntry[]{cag0_cag0, cag0_cag1, cag1_cag1, cag1_cag0};
        root.consumerAgentGroups = new InConsumerAgentGroup[]{cag0, cag1};
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.distanceEvaluators = new InDistanceEvaluator[0];
        root.numberOfTies = numberOfTies;
        root.processModel = new InProcessModel[]{processModel};
        root.orientationSupplier = new InOrientationSupplier[]{orientationSupplier};
        root.slopeSupplier = new InSlopeSupplier[]{slopeSupplier};
        root.productGroups = new InProductGroup[]{pv};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.spatialDistributions = new InSpatialDistribution[]{cag0_spatial, cag1_spatial};
        root.timeModel = new InTimeModel[]{timeModel};

        return root;
    }

    private static InConsumerAgentGroupAttribute build(String prefix, InAttributeName name, InUnivariateDoubleDistribution dist, List<InConsumerAgentGroupAttribute> out) {
        InConsumerAgentGroupAttribute attr = new InConsumerAgentGroupAttribute(prefix + "_" + name.getName(), name, dist);
        out.add(attr);
        return attr;
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

        SideCustom grp1 = new SideCustom("grp1", 10, 5, new DoubleTimeSeries("0"));
        SideCustom grp2 = new SideCustom("grp2", 20, 10, new DoubleTimeSeries("0"));

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
