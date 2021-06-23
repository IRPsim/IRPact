package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.attribute.DoubleAttribute;
import de.unileipzig.irpact.develop.Dev;

/**
 * @author Daniel Abitz
 */
public interface RelativeAgreementAlgorithm extends Nameable {

    boolean apply(
            double speedOfConvergence,
            double opinioni, double uncertaintyi,
            double opionionj, double uncertaintyj,
            DoubleAttribute opinioniAttr, DoubleAttribute uncertaintyiAttr
    );

    default boolean apply(
            double speedOfConvergence,
            double opinioni, double uncertaintyi,
            double opionionj, double uncertaintyj,
            DoubleAttribute opinioniAttr, DoubleAttribute uncertaintyiAttr,
            DoubleAttribute opinionjAttr, DoubleAttribute uncertaintyjAttr
    ) {
        return Dev.throwException();
    }
}
