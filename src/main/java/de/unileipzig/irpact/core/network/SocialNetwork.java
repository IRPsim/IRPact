package de.unileipzig.irpact.core.network;

import de.unileipzig.irpact.core.misc.Initialization;

/**
 * @author Daniel Abitz
 */
public interface SocialNetwork extends Initialization {

    SocialGraph getGraph();

    GraphConfiguration getConfiguration();
}
