package de.unileipzig.irpact.experimental.tests.servicesSearch;

import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface SchedulerService {

    void runTask(ClientService client, Task task);
}
