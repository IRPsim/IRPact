package de.unileipzig.irpact.core.agent.company.advertisement;

import de.unileipzig.irpact.core.agent.company.CompanyAgent;

/**
 * @author Daniel Abitz
 */
public class NoAdvertisement implements AdvertisementScheme {

    public static final String NAME = NoAdvertisement.class.getSimpleName();
    public static final NoAdvertisement INSTANCE = new NoAdvertisement();

    @Override
    public void advertise(CompanyAgent agent) {
    }
}
