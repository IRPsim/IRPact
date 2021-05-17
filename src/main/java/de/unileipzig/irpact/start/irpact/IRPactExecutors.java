package de.unileipzig.irpact.start.irpact;

import de.unileipzig.irpact.start.irpact.modes.RunWithoutSimulation;
import de.unileipzig.irpact.start.irpact.modes.RunFully;

/**
 * @author Daniel Abitz
 */
public final class IRPactExecutors {

    public static final String DEFAULT_MODE = RunFully.ID_STR;

    private IRPactExecutors() {
    }

    public static IRPactExecutor get(int id) {
        switch (id) {
            case RunFully.ID:
                return RunFully.INSTANCE;

            case RunWithoutSimulation.ID:
                return RunWithoutSimulation.INSTANCE;

            default:
                throw new IllegalArgumentException("unsupported id: " + id);
        }
    };
}
