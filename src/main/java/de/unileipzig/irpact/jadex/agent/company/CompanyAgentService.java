package de.unileipzig.irpact.jadex.agent.company;

import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
public interface CompanyAgentService {

    CompanyAgent getCompanyAgentSyn();

    IFuture<? extends CompanyAgent> getCompanyAgentAsyn();
}
