package de.unileipzig.irpact.io.param.input.postdata;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InPostDataAnalysis extends InIRPactEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    default String getBaseFileName() {
        return getName();
    }

    boolean isEnabled();

    default boolean isDisabled() {
        return !isEnabled();
    }

    boolean hasSomethingToStore();

    default boolean hasNothingToStore() {
        return !hasSomethingToStore();
    }
}
