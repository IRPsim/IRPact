package de.unileipzig.irpact.util.R;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.script.Engine;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public final class RscriptEngine implements Engine {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RscriptEngine.class);

    public static final String DEFAULT_COMMAND = "Rscript";

    private final String execCmd;
    protected Boolean usable = null;

    public RscriptEngine() {
        this.execCmd = DEFAULT_COMMAND;
    }

    public RscriptEngine(Path gnuPlotPath) {
        this.execCmd = "\"" + gnuPlotPath + "\"";
    }

    public RscriptEngine(String command) {
        this.execCmd = DEFAULT_COMMAND.equals(command)
                ? DEFAULT_COMMAND
                : "\"" + command + "\"";
    }

    @Override
    public String printCommand() {
        return execCmd;
    }

    @Override
    public boolean isUsable(boolean tryAgain) {
        if(tryAgain || usable == null) {
            usable = callVersion();
        }
        return usable;
    }

    protected boolean callVersion() {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(printCommand(), "--version");
            Process process = builder.start();
            process.waitFor();
            return true;
        } catch (Throwable t) {
            LOGGER.warn("engine '{}' not found (error message: '{}')", printCommand(), t.getMessage());
            return false;
        }
    }
}
