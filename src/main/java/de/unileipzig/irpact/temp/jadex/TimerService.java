package de.unileipzig.irpact.temp.jadex;

import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import jadex.bridge.service.annotation.Reference;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface TimerService {

    IFuture<Boolean> isValid2();

    IFuture<Void> finished(AgentGroupWrapper group);
}
