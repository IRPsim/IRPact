package de.unileipzig.irpact.start.optact.gvin;

import de.unileipzig.irpact.io.param.irpopt.SideCustom;
import de.unileipzig.irpact.start.optact.OptActRes;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irpact.commons.graph.topology.GraphTopology;
import de.unileipzig.irpact.start.optact.network.IFreeMultiGraphTopology;
import de.unileipzig.irpact.start.optact.network.IGraphTopology;
import de.unileipzig.irpact.start.optact.network.IWattsStrogatzModel;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.defstructure.DefinitionType;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.def.*;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;
import de.unileipzig.irptools.util.UiEdn;
import de.unileipzig.irptools.util.Util;

import java.awt.*;
import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("SameParameterValue")
@Definition(
        root = true
)
public class GvInRoot implements RootClass, DefaultScenarioFactory {

    public static final List<ParserInput> CLASSES_WITHOUT_ROOT = Util.arrayListOf(
            ParserInput.newInstance(DefinitionType.INPUT, AgentGroup.class),
            ParserInput.newInstance(DefinitionType.INPUT, IGraphTopology.class),
            ParserInput.newInstance(DefinitionType.INPUT, IWattsStrogatzModel.class),
            ParserInput.newInstance(DefinitionType.INPUT, IFreeMultiGraphTopology.class)
    );

    public static final List<ParserInput> CLASSES_WITHOUT_ROOT_AND_GRAPHVIZ = Util.mergedArrayListOf(
            InRoot.CLASSES_WITHOUT_ROOT,
            CLASSES_WITHOUT_ROOT
    );

    public static final List<ParserInput> CLASSES = Util.mergedArrayListOf(
            Util.arrayListOf(ParserInput.newInstance(DefinitionType.INPUT, GvInRoot.class)),
            InRoot.CLASSES_WITHOUT_ROOT,
            GraphvizRoot.CLASSES_WITHOUT_ROOT,
            CLASSES_WITHOUT_ROOT
    );

    //new stuff

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public IGraphTopology[] topologies;

    //Graphviz

    @FieldDefinition
    public GraphvizLayoutAlgorithm[] layoutAlgorithms;

    @FieldDefinition
    public GraphvizOutputFormat[] outputFormats;

    @FieldDefinition
    public GraphvizColor[] colors;

    @FieldDefinition
    public GraphvizGlobal graphvizGlobal;

    //OptAct

    @FieldDefinition()
    public InGlobal global;

    @FieldDefinition
    public Sector[] sectors;

    @FieldDefinition
    public SideCustom[] customs;

    @FieldDefinition
    public SideFares[] fares;

    @FieldDefinition
    public LoadDSE[] dse;

    @FieldDefinition
    public TechDESES[] deses;

    @FieldDefinition
    public TechDESPV[] despv;

    public GvInRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES;
    }

    @Override
    public void peekEdn(Sections sections, UiEdn ednType) {
    }

    @Override
    public OptActRes getResources() {
        return new OptActRes();
    }

    @Override
    public GvInRoot createDefaultScenario() {
        GvInRoot root = new GvInRoot();
        initOptAct(root);
        initGV(root);
        return root;
    }

    public GraphvizColor getColor(String agentName) {
        for(AgentGroup grp: agentGroups) {
            if(agentName.startsWith(grp._name)) {
                return grp.agentColor;
            }
        }
        return null;
    }

    public OutputFormat getOutputFormat() {
        for(GraphvizOutputFormat f: outputFormats) {
            if(f.useFormat) {
                return f.toOutputFormat();
            }
        }
        return null;
    }

    public LayoutAlgorithm getLayoutAlgorithm() {
        for(GraphvizLayoutAlgorithm a: layoutAlgorithms) {
            if(a.useLayout) {
                return a.toLayoutAlgorithm();
            }
        }
        return null;
    }

    public <N, E> GraphTopology<N, E> getTopology() {
        for(IGraphTopology topology: topologies) {
            if(topology.use()) {
                return topology.createInstance();
            }
        }
        return null;
    }

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

    private static IFreeMultiGraphTopology createFreeMultiGraphTopology(String name, int edgeCount, long seed, boolean use) {
        IFreeMultiGraphTopology model = new IFreeMultiGraphTopology();
        model._name = name;
        model.ftEdgeCount = edgeCount;
        model.ftSelfReferential = false;
        model.ftSeed = seed;
        model.ftUseThis = use;
        return model;
    }

    private void initGV(GvInRoot root) {
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

    private void initOptAct(GvInRoot root) {
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
