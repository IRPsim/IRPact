package de.unileipzig.irpact.commons.listener;

import de.unileipzig.irpact.commons.exception.IRPactException;

/**
 * @author Daniel Abitz
 */
public interface Listener {

    void accept() throws IRPactException;
}
