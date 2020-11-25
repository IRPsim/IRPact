package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;
import de.unileipzig.irptools.util.Util;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class InRoot implements RootClass, DefaultScenarioFactory {

    public static final List<ParserInput> CLASSES_WITHOUT_ROOT = Util.arrayListOf(
            ParserInput.newInstance(Type.INPUT, Ii.class),
            ParserInput.newInstance(Type.INPUT, InGlobal.class),
            ParserInput.newInstance(Type.INPUT, LoadDS.class),
            ParserInput.newInstance(Type.INPUT, LoadDSE.class),
            ParserInput.newInstance(Type.INPUT, LoadDSLOA.class),
            ParserInput.newInstance(Type.INPUT, Pss.class),
            ParserInput.newInstance(Type.INPUT, Sector.class),
            ParserInput.newInstance(Type.INPUT, Side.class),
            ParserInput.newInstance(Type.INPUT, SideCustom.class),
            ParserInput.newInstance(Type.INPUT, SideFares.class),
            ParserInput.newInstance(Type.INPUT, TechDEGEN.class),
            ParserInput.newInstance(Type.INPUT, TechDES.class),
            ParserInput.newInstance(Type.INPUT, TechDESES.class),
            ParserInput.newInstance(Type.INPUT, TechDESPV.class),
            ParserInput.newInstance(Type.INPUT, TechDESTO.class)
    );

    public static final List<ParserInput> CLASSES = Util.mergedArrayListOf(
            Util.arrayListOf(ParserInput.newInstance(Type.INPUT, InRoot.class)),
            CLASSES_WITHOUT_ROOT
    );

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

    public InRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES;
    }

    @Override
    public InRoot createDefaultScenario() {
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

        InRoot root = new InRoot();
        root.global = global;
        root.sectors = new Sector[] {E};
        root.customs = new SideCustom[] {grp1, grp2};
        root.fares = new SideFares[] {SMS, NS, PS};
        root.dse = new LoadDSE[] {loadE1, loadE2};
        root.deses = new TechDESES[]{techES1};
        root.despv = new TechDESPV[]{techPV1};

        return root;
    }
}
