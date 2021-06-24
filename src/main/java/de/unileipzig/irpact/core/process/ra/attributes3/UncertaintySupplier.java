package de.unileipzig.irpact.core.process.ra.attributes3;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

/**
 * @author Daniel Abitz
 */
public interface UncertaintySupplier extends Nameable {

    void initalize();

    boolean isSupported(ConsumerAgent agent);

    Uncertainty createFor(ConsumerAgent agent);
}
