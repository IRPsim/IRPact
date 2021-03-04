package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.spatial.DiscreteSpatialDistribution;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Demo0 {

    @Test
    void runAllDemos() throws IOException, ParsingException {

        new Demo1().runAll();
        new Demo2().runAll();
        new Demo3().runAll();
        new Demo4().runAll();
        new Demo5().runAll();
        new Demo6().runAll();
        new Demo7().runAll();
        new Demo8().runAll();
        new Demo9().runAll();
        new Demo10().runAll();
        new Demo11().runAll();
    }
}
