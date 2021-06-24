package de.unileipzig.irpact.core.process.ra.uncert;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

/**
 * @author Daniel Abitz
 */
public interface UncertaintyManager extends Nameable {

    void initalize();

    boolean register(UncertaintySupplier supplier);

    boolean unregister(UncertaintySupplier supplier);

    Uncertainty createFor(ConsumerAgent agent);
}
