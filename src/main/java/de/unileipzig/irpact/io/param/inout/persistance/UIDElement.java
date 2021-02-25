package de.unileipzig.irpact.io.param.inout.persistance;

import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface UIDElement {

    static String str(long uid) {
        return Long.toString(uid);
    }

    long getUID();
}
