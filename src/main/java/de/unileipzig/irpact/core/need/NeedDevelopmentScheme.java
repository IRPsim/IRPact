package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

import java.util.Collection;

/**
 * Beschreibt wie neue Beduerfnisse erstellt werden sollen.
 * @author Daniel Abitz
 */
public interface NeedDevelopmentScheme extends Scheme {

    Collection<? extends Need> developNeeds(
            ConsumerAgent agent
    );
}