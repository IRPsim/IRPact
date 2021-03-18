package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.util.Todo;

/**
 * @author Daniel Abitz
 */
@Todo("PR adden")
@Todo("Spec adden")
public interface UncertaintyAttribute extends ConsumerAgentAttribute {

    boolean isAutoAdjustment();

    double getUncertainty();

    double getConvergence();
}
