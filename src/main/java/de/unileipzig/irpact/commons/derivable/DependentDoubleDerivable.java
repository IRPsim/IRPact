package de.unileipzig.irpact.commons.derivable;

/**
 * @author Daniel Abitz
 */
public interface DependentDoubleDerivable<D> extends DependentDerivable<D, Number> {

    D derive(double value);
}
