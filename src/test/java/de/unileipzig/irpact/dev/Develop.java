package de.unileipzig.irpact.dev;

import de.unileipzig.irpact.start.Start;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Daniel Abitz
 */
@Disabled("dev tests")
class Develop {

    @Test
    void doGams() {
        String[] args = {
                "--tools",
                "--createAllDefaults"
        };
        Start.main(args);
    }
}
