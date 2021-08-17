package de.unileipzig.irpact.start.optact;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.irpopt.SideCustom;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irptools.graphviz.StandardLayoutAlgorithm;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;

/**
 * @author Daniel Abitz
 */
@Deprecated
public class OptActUtil {

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
}
