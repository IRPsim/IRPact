package de.unileipzig.irpact.jadex.examples.deprecated.tests.dummy.x;

import jadex.commons.future.IFuture;

/**
 * Service zum Beenden der Simulation.
 * @author Daniel Abitz
 * @since 0.0
 */
public interface KillPlatformServiceX {

    IFuture<Void> killPlatform();
}
