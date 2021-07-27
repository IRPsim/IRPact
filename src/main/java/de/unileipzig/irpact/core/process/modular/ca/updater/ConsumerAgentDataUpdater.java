package de.unileipzig.irpact.core.process.modular.ca.updater;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentDataUpdater extends Nameable {

    boolean update(ConsumerAgentData data) throws Throwable;
}
