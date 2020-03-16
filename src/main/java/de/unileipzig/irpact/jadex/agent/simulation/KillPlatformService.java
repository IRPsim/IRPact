package de.unileipzig.irpact.jadex.agent.simulation;

import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public interface KillPlatformService {

    IFuture<Boolean> killPlatform(String platformName);
}
