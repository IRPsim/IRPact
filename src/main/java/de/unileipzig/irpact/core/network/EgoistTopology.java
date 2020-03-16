package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.commons.annotation.ToImpl;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

/**
 * @author Daniel Abitz
 */
@ToImpl("einfuegen aller Komponenten")
public class EgoistTopology implements GraphTopologyScheme {

    public static final String NAME = EgoistTopology.class.getSimpleName();
    public static final EgoistTopology INSTANCE = new EgoistTopology();

    @Override
    public void initalize(SocialGraph graph) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addSubsequently(SocialGraph graph, ConsumerAgent agent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeSubsequently(SocialGraph graph, ConsumerAgent agent) {
        throw new UnsupportedOperationException();
    }
}
