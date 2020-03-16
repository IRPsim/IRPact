package de.unileipzig.irpact.jadex.agent.pos;

import de.unileipzig.irpact.core.agent.pos.PointOfSaleAgent;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public interface PointOfSaleAgentService {

    PointOfSaleAgent getPointOfSaleAgentSyn();

    IFuture<? extends PointOfSaleAgent> getPointOfSaleAgentAsyn();
}
