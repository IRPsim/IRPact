package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.misc.Initialization;

/**
 * @author Daniel Abitz
 */
public interface SocialNetwork extends Initialization {

    SocialGraph getGraph();

    void setGraph(SocialGraph graph);

    GraphConfiguration getConfiguration();

    void setConfiguration(GraphConfiguration configuration);
}
