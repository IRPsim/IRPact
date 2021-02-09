package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InDistanceEvaluator {

    DistanceEvaluator getInstance();
}
