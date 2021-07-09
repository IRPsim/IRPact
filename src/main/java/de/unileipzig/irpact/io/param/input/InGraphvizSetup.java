package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
public class InGraphvizSetup {

    public InGraphvizSetup() {
    }

    public static void initRes(TreeAnnotationResource res) {
    }

    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, GraphvizColor.class, InRootUI.GRAPHVIZ_COLOR);
        addEntry(res, GraphvizColor.class, "rgba");
    }
}
