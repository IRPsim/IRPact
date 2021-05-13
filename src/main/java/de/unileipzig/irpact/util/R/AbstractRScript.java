package de.unileipzig.irpact.util.R;

import de.unileipzig.irptools.util.ProcessResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractRScript implements RScript {

    protected final List<String> options = new ArrayList<>();
    protected final List<String> args = new ArrayList<>();
    protected Charset terminalCharset;

    public AbstractRScript() {
    }

    protected abstract String printRScriptPath();

    public void setTerminalCharset(Charset terminalCharset) {
        this.terminalCharset = terminalCharset;
    }

    public void addOption(String option) {
        options.add(option);
    }

    public List<String> getOptions() {
        return options;
    }

    public void addArg(String arg) {
        args.add(arg);
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public void execute(R engine) throws IOException, InterruptedException, RScriptException {
        List<String> command = new ArrayList<>();
        command.add(engine.printRPatch());
        command.addAll(options);
        command.add(printRScriptPath());
        command.addAll(args);

        ProcessBuilder pb = new ProcessBuilder();
        pb.command(command);

        Process process = pb.start();
        ProcessResult result = ProcessResult.waitFor(process);

        if(result.isError()) {
            Charset cs = terminalCharset == null ? Charset.defaultCharset() : terminalCharset;
            String msg = result.printErr(cs);
            throw new RScriptException(msg);
        }
    }
}
