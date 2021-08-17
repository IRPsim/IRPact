package de.unileipzig.irpact.core.process.ra.alg;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;

/**
 * @author Daniel Abitz
 */
public interface RelativeAgreementAlgorithm extends Nameable {

    boolean apply(
            String name1,
            ConsumerAgentAttribute opinion1,
            Uncertainty uncertainty1,
            String name2,
            ConsumerAgentAttribute opinion2,
            Uncertainty uncertainty2);
}
