package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Daniel Abitz
 */
public class NoNeedExpiration implements NeedExpirationScheme {

    public static final String NAME = NoNeedExpiration.class.getSimpleName();
    public static final NoNeedExpiration INSTANCE = new NoNeedExpiration();

    @Override
    public Collection<? extends Need> expiredNeeds(ConsumerAgent agent) {
        return Collections.emptySet();
    }
}
