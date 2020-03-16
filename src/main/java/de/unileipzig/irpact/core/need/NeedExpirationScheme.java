package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

import java.util.Collection;

/**
 * Beschreibt wie auf nicht mehr gueltige Beduerfnisse reagiert werden soll.
 * @author Daniel Abitz
 */
public interface NeedExpirationScheme extends Scheme {

    Collection<? extends Need> expiredNeeds(
            ConsumerAgent agent
    );
}