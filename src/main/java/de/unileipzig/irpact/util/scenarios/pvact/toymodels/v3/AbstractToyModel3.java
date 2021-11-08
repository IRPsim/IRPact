package de.unileipzig.irpact.util.scenarios.pvact.toymodels.v3;

import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.util.scenarios.pvact.AbstractPVactScenario;

/**
 * @author Daniel Abitz
 */
public class AbstractToyModel3 extends AbstractPVactScenario {

    protected InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected InDiracUnivariateDistribution dirac049 = new InDiracUnivariateDistribution("dirac049", 0.49);
    protected InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);
    protected InDiracUnivariateDistribution dirac100 = new InDiracUnivariateDistribution("dirac100", 100);







}
