package de.unileipzig.irpact.start.irpact.input.distribution;

import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface UnivariateDistribution {

    de.unileipzig.irpact.commons.distribution.UnivariateDistribution createInstance();
}
