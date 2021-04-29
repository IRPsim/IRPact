package de.unileipzig.irpact;

import de.unileipzig.irpact.core.log.IRPLogging;

/**
 * @author Daniel Abitz
 */
public class TestLogging {

    @SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
    private static boolean print = false;

    public static void init() {
        if(print) {
            IRPLogging.initConsole();
        } else {
            IRPLogging.disableAll();
        }
    }

    public static void noLogging() {
        IRPLogging.disableAll();
    }
}
