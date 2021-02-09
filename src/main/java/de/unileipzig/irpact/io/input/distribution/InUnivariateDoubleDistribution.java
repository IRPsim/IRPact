package de.unileipzig.irpact.io.input.distribution;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InUnivariateDoubleDistribution {

    String getName();

    UnivariateDoubleDistribution getInstance();
}
