package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Daniel Abitz
 */
public class NoNeedDevelopment implements NeedDevelopmentScheme {

    public static final String NAME = NoNeedDevelopment.class.getSimpleName();
    public static final NoNeedDevelopment INSTANCE = new NoNeedDevelopment();

    @Override
    public Collection<? extends Need> developNeeds(ConsumerAgent agent) {
        return Collections.emptySet();
    }
}
