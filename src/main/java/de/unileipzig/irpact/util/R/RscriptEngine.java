package de.unileipzig.irpact.util.R;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.util.script.Engine;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public final class RscriptEngine implements Engine {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RscriptEngine.class);

    public static final String DEFAULT_COMMAND = "Rscript";

    private final String execCmd;
    protected Boolean usable = null;
    protected String version = "NOT CALLED";

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
    public String printVersion() {
        return version;
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
            ProcessResult result = ProcessResult.waitFor(process);
            version = result.printData(StandardCharsets.UTF_8);
            return true;
        } catch (Throwable t) {
            LOGGER.warn("engine '{}' not found (error message: '{}')", printCommand(), t.getMessage());
            version = "INVALID";
            return false;
        }
    }
}
