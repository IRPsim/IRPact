package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

/**
 * @author Daniel Abitz
 */
public class IgnoreNeedSatisfy implements NeedSatisfyScheme {

    public static final String NAME = IgnoreNeedSatisfy.class.getSimpleName();
    public static final IgnoreNeedSatisfy INSTANCE = new IgnoreNeedSatisfy();

    @Override
    public void handle(ConsumerAgent agent, Need need) {
    }
}
