package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.io.input.InRoot;
import org.junit.jupiter.api.Disabled;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Demo1 {

    private static InRoot getRoot() {
        InRoot root = new InRoot();









        root.timeModel = DemoUtil.getDiscrete1D();
        return root;
    }
}
