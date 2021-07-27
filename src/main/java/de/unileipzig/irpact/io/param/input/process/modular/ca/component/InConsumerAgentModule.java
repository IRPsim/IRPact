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
                label = "TEST - modularer Graph",
                icon = ICON_TEST,
                description = "TEST TEST TEST",
                priority = "0",

                edgesTags = {GRAPHEDGE},
                edgesHeading = "Kanten",

                nodesSet = "set_InConsumerAgentModule",
                showIcon = false,
                borderHeading = "Rahmenfarbe",
                colorHeading = "Farbe",
                shapeHeading = "Form"
        )
)
public interface InConsumerAgentModule extends InModule {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
