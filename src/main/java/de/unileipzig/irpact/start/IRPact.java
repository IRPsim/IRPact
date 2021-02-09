package de.unileipzig.irpact.start;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.Logger;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;

/**
 * @author Daniel Abitz
 */
public class IRPact {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IRPact.class);

    private Start param;

    public IRPact(Start param) {
        this.param = param;
    }

    public void start() {
    }
}
