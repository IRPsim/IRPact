package de.unileipzig.irpact.v2.io.input.distribution;

import de.unileipzig.irpact.v2.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface IUnivariateDoubleDistribution {

    UnivariateDoubleDistribution createInstance();
}
