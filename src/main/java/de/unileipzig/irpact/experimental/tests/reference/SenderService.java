package de.unileipzig.irpact.experimental.tests.reference;

/**
 * @author Daniel Abitz
 */
public interface SenderService {

    NonRefData<X> getNonRefX();

    RefData<X> getRefX();
}
