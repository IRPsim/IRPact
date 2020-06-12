package de.unileipzig.irpact.experimental.tests.clockStepsTicks;

import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface Task {

    void run();
}
