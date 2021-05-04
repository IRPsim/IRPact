package de.unileipzig.irpact.commons.derivable;

/**
 * @author Daniel Abitz
 */
public interface DependentDerivable<D, S> extends DerivableBase {

    D derive(S input);
}
