package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public interface VarCollectionWriter {

    void write(VarCollection vcoll) throws IOException;
}
