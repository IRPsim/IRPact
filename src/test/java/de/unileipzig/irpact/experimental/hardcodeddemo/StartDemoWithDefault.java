package de.unileipzig.irpact.experimental.hardcodeddemo;

import de.unileipzig.irpact.start.hardcodeddemo.HardCodedAgentDemo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
class StartDemoWithDefault {

    @Test
    void runIt() throws IOException {
        String[] args = {
                "-i", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\res_backup\\resources_2020_09_15_from_dev\\scenarios", "default.json").toString(),
                "-o", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\hardcodeddemo", "default-out.json").toString()
        };
        HardCodedAgentDemo.main(args);
    }
}
