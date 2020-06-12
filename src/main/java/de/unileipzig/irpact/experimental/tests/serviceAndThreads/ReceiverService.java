package de.unileipzig.irpact.experimental.tests.serviceAndThreads;

import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface ReceiverService {

    void receive(Sender sender, Data data);
}
