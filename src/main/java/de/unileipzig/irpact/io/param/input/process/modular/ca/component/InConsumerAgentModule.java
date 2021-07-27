package de.unileipzig.irpact.io.param.input.process.modular.ca.component;

import de.unileipzig.irpact.io.param.input.process.modular.InModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.ModuleHelper;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Graph;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition(
        graph = @Graph(
                id = ModuleHelper.MODULAR_GRAPH,
                label = "TEST - modularer Graph",
                icon = ModuleHelper.ICON_TEST,
                description = "TEST TEST TEST",
                priority = "0",

                edgesTags = {"graphedge"},
                edgesHeading = "Kanten",

                nodesSet = "set_InConsumerAgentModule",
                showBorder = false,
                showIcon = false,
                colorHeading = "Farben",
                shapeHeading = "Formen"
        )
)
public interface InConsumerAgentModule extends InModule {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
