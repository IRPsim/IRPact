package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;

/**
 * @author Daniel Abitz
 */
public interface GraphTopologyScheme extends Scheme {

    void initalize(SocialGraph graph);

    void addSubsequently(SocialGraph graph, ConsumerAgent agent);

    void removeSubsequently(SocialGraph graph, ConsumerAgent agent);
}
