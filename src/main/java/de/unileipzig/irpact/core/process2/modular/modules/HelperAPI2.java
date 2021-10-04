package de.unileipzig.irpact.core.process2.modular.modules;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.logging.LoggingHelper;

/**
 * @author Daniel Abitz
 */
public interface HelperAPI2 extends Nameable, LoggingHelper {

    //=========================
    //general
    //=========================

    default void traceModuleInfo() {
        trace("call module {}={}", getClass().getSimpleName(), getName());
    }
}
