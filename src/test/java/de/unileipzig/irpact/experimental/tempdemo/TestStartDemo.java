package de.unileipzig.irpact.experimental.tempdemo;

import de.unileipzig.irpact.start.Start;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Daniel Abitz
 */
@Disabled
class TestStartDemo {

    @Test
    void runPrintAll() {
        String[] args = {
                "--tools",
                "--createAllDefaults"
        };
        Start.main(args);
    }
}
