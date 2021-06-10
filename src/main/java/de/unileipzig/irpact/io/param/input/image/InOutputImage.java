package de.unileipzig.irpact.io.param.input.image;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InOutputImage extends InIRPactEntity {

    int MODE_NOTHING = 0;
    int MODE_ADOPTION_LINECHART = 1;
    int MODE_ADOPTION_INTERACTION_LINECHART = 2;
    int MODE_ADOPTION_PHASE_BARCHART = 3;

    int ENGINE_UNKNOWN = 0;
    int ENGINE_R = 1;
    int ENGINE_GNUPLOT = 2;

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    default String getBaseFileName() {
        return getName();
    }

    int getEngine();

    int getMode();

    boolean isStoreImage();

    boolean isStoreData();

    boolean isStoreScript();
}
