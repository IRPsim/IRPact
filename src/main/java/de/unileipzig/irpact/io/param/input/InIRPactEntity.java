package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InIRPactEntity extends InEntity<IRPactInputParser> {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
