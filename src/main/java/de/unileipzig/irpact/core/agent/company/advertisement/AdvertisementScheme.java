package de.unileipzig.irpact.core.agent.company.advertisement;

import de.unileipzig.irpact.core.Scheme;
import de.unileipzig.irpact.core.agent.company.CompanyAgent;

/**
 * @author Daniel Abitz
 */
public interface AdvertisementScheme extends Scheme {

    void advertise(CompanyAgent agent);
}
