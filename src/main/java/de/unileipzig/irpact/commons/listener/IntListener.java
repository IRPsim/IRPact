package de.unileipzig.irpact.commons.listener;

import de.unileipzig.irpact.commons.exception.IRPactException;

/**
 * @author Daniel Abitz
 */
public interface IntListener {

    void accept(int value) throws IRPactException;
}
