package de.unileipzig.irpact.util.R;

import de.unileipzig.irpact.util.script.Engine;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public final class RscriptEngine implements Engine {

    private final String execCmd;

    public RscriptEngine() {
        this.execCmd = "Rscript";
    }

    public RscriptEngine(Path gnuPlotPath) {
        this.execCmd = "\"" + gnuPlotPath + "\"";
    }

    @Override
    public String printCommand() {
        return execCmd;
    }
}
