package de.unileipzig.irpact.start.optact;

import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.start.optact.network.IFreeMultiGraphTopology;
import de.unileipzig.irpact.start.optact.network.IWattsStrogatzModel;
import de.unileipzig.irptools.graphviz.def.GraphvizResource;
import de.unileipzig.irptools.util.MultiAnnotationResource;
import de.unileipzig.irptools.util.Util;

import static de.unileipzig.irptools.Constants.*;

/**
 * @author Daniel Abitz
 */
public class OptActRes extends MultiAnnotationResource {

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
        checkedPutKeyValue(toKey(AgentGroup.class, "numberOfAgents", GAMS_IDENTIFIER), "Anzahl der Agenten");
        checkedPutKeyValue(toKey(AgentGroup.class, "numberOfAgents", GAMS_DESCRIPTION), "Anzahl der Agenten");
        checkedPutKeyValue(toKey(AgentGroup.class, "agentColor", GAMS_IDENTIFIER), "zu nutzende Farbe");
        checkedPutKeyValue(toKey(AgentGroup.class, "agentColor", GAMS_DESCRIPTION), "Farbe, welche diese Gruppe im Graphen haben soll. Wichtig: es wird nur der erste Wert verwendet! Falls keine Farbe gewählt wird, ist die Farbe schwarz.");

        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmK", GAMS_IDENTIFIER), "Knotengrad");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmK", GAMS_DESCRIPTION), "Knotengrad k");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmBeta", GAMS_IDENTIFIER), "Rewire Wahrscheinlichkeit");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmBeta", GAMS_DESCRIPTION), "Wahrscheinlichkeit, dass eine Kante umgelegt wird.");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmSelfReferential", GAMS_IDENTIFIER), "Selbstreferenzierung erlaubt?");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmSelfReferential", GAMS_DESCRIPTION), "On Knoten Kanten zu sich selber erzeugen dürfen.");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmSeed", GAMS_IDENTIFIER), "Seed");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmSeed", GAMS_DESCRIPTION), "Seed für den Zufallsgenerator.");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmUseThis", GAMS_IDENTIFIER), "Diese Topologie nutzen?");
        checkedPutKeyValue(toKey(IWattsStrogatzModel.class, "wsmUseThis", GAMS_DESCRIPTION), "Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!");

        checkedPutKeyValue(toKey(IFreeMultiGraphTopology.class, "ftEdgeCount", GAMS_IDENTIFIER), "Knotengrad");
        checkedPutKeyValue(toKey(IFreeMultiGraphTopology.class, "ftEdgeCount", GAMS_DESCRIPTION), "Knotengrad k");
        checkedPutKeyValue(toKey(IFreeMultiGraphTopology.class, "ftSelfReferential", GAMS_IDENTIFIER), "Selbstreferenzierung erlaubt?");
        checkedPutKeyValue(toKey(IFreeMultiGraphTopology.class, "ftSelfReferential", GAMS_DESCRIPTION), "On Knoten Kanten zu sich selber erzeugen dürfen.");
        checkedPutKeyValue(toKey(IFreeMultiGraphTopology.class, "ftSeed", GAMS_IDENTIFIER), "Seed");
        checkedPutKeyValue(toKey(IFreeMultiGraphTopology.class, "ftSeed", GAMS_DESCRIPTION), "Seed für den Zufallsgenerator.");
        checkedPutKeyValue(toKey(IFreeMultiGraphTopology.class, "ftUseThis", GAMS_IDENTIFIER), "Diese Topologie nutzen?");
        checkedPutKeyValue(toKey(IFreeMultiGraphTopology.class, "ftUseThis", GAMS_DESCRIPTION), "Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!");

        //edn

        checkedPutEdn(
                AgentGroup.class,
                Util.arrayListOf(GRAPHVIZ, "Agentengruppen"),
                Util.arrayListOf("0", "0"),
                Util.arrayListOf(GRAPHVIZ_DESC, "Agentengruppe, welche im Graphen dargestellt werden sollen.")
        );
        checkedPutEdn(
                IWattsStrogatzModel.class,
                Util.arrayListOf(GRAPHVIZ, TOPO, "Watts-Strogatz-Model"),
                Util.arrayListOf("0", "0", "0"),
                Util.arrayListOf(GRAPHVIZ_DESC, TOPO_DESC, "Watts-Strogatz-Model")
        );
        checkedPutEdn(
                IFreeMultiGraphTopology.class,
                Util.arrayListOf(GRAPHVIZ, TOPO, "Freie Topologie"),
                Util.arrayListOf("0", "0", "0"),
                Util.arrayListOf(GRAPHVIZ_DESC, TOPO_DESC, "Freie Topologie")
        );
    }
}
