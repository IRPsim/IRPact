package de.unileipzig.irpact.util.gnuplot;

import de.unileipzig.irpact.util.script.Engine;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public final class GnuPlot implements Engine {

    private final String execCmd;

    public GnuPlot() {
        this.execCmd = "gnuplot";
    }

    public GnuPlot(Path gnuPlotPath) {
        this.execCmd = "\"" + gnuPlotPath + "\"";
    }

    @Override
    public String printCommand() {
        return execCmd;
    }
}
