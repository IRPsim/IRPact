package de.unileipzig.irpact.util.gnuplot;

import de.unileipzig.irpact.util.script.Engine;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public final class GnuPlotEngine implements Engine {

    private final String execCmd;

    public GnuPlotEngine() {
        this.execCmd = "gnuplot";
    }

    public GnuPlotEngine(Path gnuPlotPath) {
        this.execCmd = "\"" + gnuPlotPath + "\"";
    }

    @Override
    public String printCommand() {
        return execCmd;
    }
}
