package de.unileipzig.irpact.start.optact;

import de.unileipzig.irptools.graphviz.def.GraphvizResource;
import de.unileipzig.irptools.util.SimpleResource;

import static de.unileipzig.irpact.v2.io.input2.InputResource.*;
import static de.unileipzig.irptools.graphviz.def.GraphvizResource.*;

/**
 * @author Daniel Abitz
 */
public class OptActRes extends SimpleResource {

    private static final String GRAPHVIZ = "Graphviz";
    private static final String GRAPHVIZ_DESC = "Graphviz Einstellungen";
    private static final String TOPO = "Topologie";
    private static final String TOPO_DESC = "unterstützte Topologien";

    public OptActRes() {
        initGraphViz();
        initTopo();
    }

    private void initGraphViz() {
        putCopyAll(GraphvizResource.DEFAULT);
    }

    private void initTopo() {
        //gams
        checkedPut(IWATTSSTROGATZMODEL_WSMK, "k");
        checkedPut(IWATTSSTROGATZMODEL_WSMBETA, "beta");
        checkedPut(IWATTSSTROGATZMODEL_WSMSELFREFERENTIAL, "Ist Selbstreferenzierung erlaubt?");
        checkedPut(IWATTSSTROGATZMODEL_WSMSEED, "Seed für den Zufallsgenerator.");

        checkedPut(IFREEMULTIGRAPHTOPOLOGY_FTEDGECOUNT, "Anzahl Kanten je Knoten");
        checkedPut(IFREEMULTIGRAPHTOPOLOGY_FTSELFREFERENTIAL, "Ist Selbstreferenzierung erlaubt?");
        checkedPut(IFREEMULTIGRAPHTOPOLOGY_FTSEED, "Seed für den Zufallsgenerator.");

        //edn
        checkedPut(IWATTSSTROGATZMODEL_LABEL, GRAPHVIZ, TOPO, "WattsStrogatzModel");
        checkedPut(IWATTSSTROGATZMODEL_PRIORITIES, "0", "0", "0");
        checkedPut(IWATTSSTROGATZMODEL_DESCRIPTION, GRAPHVIZ_DESC, TOPO_DESC, "Watts Strogatz Model");

        checkedPut(IFREEMULTIGRAPHTOPOLOGY_LABEL, GRAPHVIZ, TOPO, "FreeTopology");
        checkedPut(IFREEMULTIGRAPHTOPOLOGY_PRIORITIES, "0", "0", "0");
        checkedPut(IFREEMULTIGRAPHTOPOLOGY_DESCRIPTION, GRAPHVIZ_DESC, TOPO_DESC, "Freie Topologie");
    }
}
