package de.unileipzig.irpact.temp.jadex;

import jadex.bridge.service.annotation.Reference;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface TimerService {

    IFuture<Boolean> isValid2();
}
