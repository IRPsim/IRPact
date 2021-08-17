package de.unileipzig.irpact.util.gnuplot;

import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.script.ProcessBasedFileScript;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class GnuPlotFileScript extends ProcessBasedFileScript<GnuPlotEngine> implements GnuPlotScript {

    public GnuPlotFileScript() {
        super();
    }

    public GnuPlotFileScript(String text) {
        super(text);
    }

    public GnuPlotFileScript(Path path, Charset charset) {
        super(path, charset);
    }

    @Override
    public void addPathArgument(Path path) {
        addArgument(GnuPlotBuilder.print(path));
    }

    @Override
    protected boolean isOnlyWarning(String errMsg) {
        return errMsg != null && (errMsg.contains("WARNING") || errMsg.contains("Warning"));
    }

    @Override
    protected void addCommands(GnuPlotEngine engine, List<String> commands) {
        commands.add(engine.printCommand());
        if(args.size() > 0) {
            commands.add("-c");
        }
        commands.add(printPath());
        commands.addAll(args);
    }
}
