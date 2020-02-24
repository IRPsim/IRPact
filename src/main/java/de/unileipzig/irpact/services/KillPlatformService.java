package de.unileipzig.irpact.services;

import jadex.commons.future.IFuture;

/**
 * Service zum Beenden der Simulation.
 * @author Daniel Abitz
 * @since 0.0
 */
public interface KillPlatformService {

    IFuture<Void> killPlatform();
}
