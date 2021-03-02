package de.unileipzig.irpact.io.param.inout.persist.param.util;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface UIDElement {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    static String str(long uid) {
        return Long.toString(uid);
    }

    long getUID();
}
