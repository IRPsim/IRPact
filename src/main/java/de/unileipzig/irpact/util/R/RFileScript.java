package de.unileipzig.irpact.util.R;

import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.script.ProcessBasedFileScript;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class RFileScript extends ProcessBasedFileScript<RscriptEngine> implements RScript {

    public RFileScript() {
        super();
    }

    public RFileScript(String text) {
        super(text);
    }

    public RFileScript(Path path, Charset charset) {
        super(path, charset);
    }

    public void addPathArgument(Path path) {
        addArgument(GnuPlotBuilder.print(path));
    }

    @Override
    protected boolean isOnlyWarning(String errMsg) {
        if(errMsg == null) {
            return false;
        }
        String errMsg2 = errMsg.toLowerCase();
        if(errMsg2.contains("null device")) {
            return true;
        }
        return errMsg2.contains("warn") && !(errMsg2.contains("error") || errMsg2.contains("fehler"));
    }

    @Override
    protected void addCommands(RscriptEngine engine, List<String> commands) {
        commands.add(engine.printCommand());
        commands.add(printPath());
        commands.addAll(args);
    }
}
