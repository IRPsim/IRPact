package de.unileipzig.irpact.io.param.input.process2.modular;

import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.graph.*;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODULAR4_GRAPH;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graph = @Graph(
//                id = MODULAR_GRAPH,
//                label = "[TEST] Graph - Modulares Prozessmodell v3",
//                icon = GRAPH_ICON,
//                description = "[TEST] Interaktiver Graph fuer das modulare Prozessmodell.",
//                priority = "-9",
//
//                edgesTags = {GRAPH_EDGE},
//                edgesHeading = "Kantenbedeutung",
//
//                nodesSet = NODE_SET,
//                showIcon = false,
//                borderHeading = "Rahmenfarbe",
//                colorHeading = "Fuellfarbe",
//                shapeHeading = "Form"
//        ),
        edn = @Edn(
                icon = CAMPMGraphSettings.GRAPH_ICON
        ),
        graph3 = @Graph(
                id = CAMPMGraphSettings.GRAPH_ID,
                ui = @UiSettings(
                        useEdnSettings = true
                ),
                edges = @EdgesSettings(
                ),
                nodes = @NodesSettings(
                        set = CAMPMGraphSettings.NODE_SET,
                        color = @ColorSettings(
                                fix = @Fix(CAMPMGraphSettings.GRAPH_COLOR)
                        ),
                        icon = @IconSettings(
                                enabled = false
                        )
                )
        )
)
@LocalizedUiResource.PutGraph(PROCESS_MODULAR4_GRAPH)
public interface InModularProcessModel2 extends InProcessModel {

    @TreeAnnotationResource.Init
    static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(LocalizedUiResource res) {
    }
}
