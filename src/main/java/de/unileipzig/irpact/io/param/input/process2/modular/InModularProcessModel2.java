package de.unileipzig.irpact.io.param.input.process2.modular;

import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Graph;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graph = @Graph(
                id = MODULAR_GRAPH,
                label = "[TEST] Graph - Modulares Prozessmodell v3",
                icon = GRAPH_ICON,
                description = "[TEST] Interaktiver Graph fuer das modulare Prozessmodell.",
                priority = "-9",

                edgesTags = {GRAPH_EDGE},
                edgesHeading = "Kantenbedeutung",

                nodesSet = NODE_SET,
                showIcon = false,
                borderHeading = "Rahmenfarbe",
                colorHeading = "Fuellfarbe",
                shapeHeading = "Form"
        )
)
public interface InModularProcessModel2 extends InProcessModel {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
