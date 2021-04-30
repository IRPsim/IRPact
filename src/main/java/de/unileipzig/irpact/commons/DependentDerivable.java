package de.unileipzig.irpact.commons;

/**
 * @author Daniel Abitz
 */
public interface DependentDerivable<D, S> extends DerivableBase {

    D derive(S input);
}
