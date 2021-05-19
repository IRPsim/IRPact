package de.unileipzig.irpact.commons.derivable;

/**
 * @author Daniel Abitz
 */
public interface NamendDependentDoubleDerivable<D> extends DependentDoubleDerivable<D> {

    D derive(String str, Number value);

    D derive(String str, double value);
}
