package de.unileipzig.irpact.io.param.input.image;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.start.irpact.IRPact;
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
    Object[] DEFAULT_MODES = {IRPact.IMAGE_ANNUAL_ADOPTIONS, IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS, IRPact.IMAGE_ANNUAL_CUMULATIVE_ADOPTIONS};

    int ENGINE_R = 1;
    int ENGINE_GNUPLOT = 2;
    Object[] DEFAULT_ENGINE = {ENGINE_GNUPLOT};

    static String printEngineDomain() {
        return "[" + ENGINE_R + ", " + ENGINE_GNUPLOT + "]";
    }

    static String printModeDomain() {
        return "[" + MODE_NOTHING + ", " + MODE_ADOPTION_PHASE_BARCHART + "]";
    }

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
