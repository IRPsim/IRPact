package de.unileipzig.irpact.io.param.input.process.modular.ca.component;

import de.unileipzig.irpact.io.param.input.process.modular.InModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Graph;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import static de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graph = @Graph(
                id = MPM_GRAPH,
                label = "[TEST] Graph - Modulares Prozessmodell",
                icon = GRAPH_ICON,
                description = "[TEST] Interaktiver Graph fuer das modulare Prozessmodell.",
                priority = "-10",

                edgesTags = {GRAPHEDGE},
                edgesHeading = "Kantenbedeutung",

                nodesSet = "set_InConsumerAgentModule",
                showIcon = false,
                borderHeading = "Rahmenfarbe",
                colorHeading = "Fuellfarbe",
                shapeHeading = "Form"
        )
)
public interface InConsumerAgentModule extends InModule {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
