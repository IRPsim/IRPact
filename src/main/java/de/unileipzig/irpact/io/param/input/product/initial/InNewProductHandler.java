package de.unileipzig.irpact.io.param.input.product.initial;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InNewProductHandler extends InIRPactEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }
}
