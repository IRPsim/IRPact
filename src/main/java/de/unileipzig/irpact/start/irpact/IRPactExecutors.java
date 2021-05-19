package de.unileipzig.irpact.start.irpact;

import de.unileipzig.irpact.start.irpact.modes.*;

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

            case RunMinimalSimulation.ID:
                return RunMinimalSimulation.INSTANCE;

            case TestMode.ID:
                return TestMode.INSTANCE;

            case PrintInput.ID:
                return PrintInput.INSTANCE;

            case ThrowError.ID:
                return ThrowError.INSTANCE;

            default:
                throw new IllegalArgumentException("unsupported id: " + id);
        }
    };
}
