package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentValueAttribute;
import de.unileipzig.irpact.develop.Todo;

/**
 * @author Daniel Abitz
 */
@Todo("PR adden")
@Todo("Spec adden")
public interface UncertaintyAttribute extends ConsumerAgentValueAttribute {

    boolean isAutoAdjustment();

    double getUncertainty();

    double getConvergence();
}
