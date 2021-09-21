package de.unileipzig.irpact.experimental.tests.serviceAndThreads;

import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface Sender {

    void oh(Data data);
}
