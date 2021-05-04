package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentDoubleAttribute;
import de.unileipzig.irpact.develop.Todo;

/**
 * @author Daniel Abitz
 */
@Todo("PR adden")
@Todo("Spec adden")
public interface UncertaintyAttribute extends ConsumerAgentDoubleAttribute {

    boolean isAutoAdjustment();

    double getUncertainty();

    double getConvergence();
}
