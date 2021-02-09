package de.unileipzig.irpact.experimental.deprecated.input.distribution;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface IUnivariateDoubleDistribution {

    UnivariateDoubleDistribution createInstance();
}
