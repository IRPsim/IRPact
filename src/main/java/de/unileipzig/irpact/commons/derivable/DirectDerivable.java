package de.unileipzig.irpact.commons.derivable;

/**
 * @author Daniel Abitz
 */
public interface DirectDerivable<D> extends DerivableBase {

    D derive();
}
