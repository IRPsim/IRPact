package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

/**
 * Beschreibt, wie auf ein erfuelltes Beduerfnis reagiert werden soll.
 * @author Daniel Abitz
 */
public interface NeedSatisfyScheme extends Scheme {

    void handle(
            ConsumerAgent agent,
            Need need
    );
}
