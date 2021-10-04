package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractModule2<I, O>
        extends NameableBase
        implements Module2<I, O>, LoggingHelper {

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }
}
