package de.unileipzig.irpact.util.script;

import de.unileipzig.irptools.util.ProcessResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public abstract class ProcessBasedFileScript<E extends Engine> extends FileScript<E> {

    protected final List<String> args = new ArrayList<>();
    protected Charset terminalCharset = Charset.defaultCharset();

    protected ProcessResult lastResult;
    protected String lastWarnMessage;

    public ProcessBasedFileScript() {
        super();
    }

    public ProcessBasedFileScript(String text) {
        super(text);
    }

    public ProcessBasedFileScript(Path path, Charset charset) {
        super(path, charset);
    }

    public List<String> getArguments() {
        return args;
    }

    public void addArgument(String arg) {
        args.add(arg);
    }

    protected void addCommands(E engine, List<String> commands) {
        commands.add(engine.printCommand());
        commands.addAll(getArguments());
    }

    protected ProcessBuilder buildProcess(E engine) {
        List<String> commands = new ArrayList<>();
        addCommands(engine, commands);
        return new ProcessBuilder().command(commands);
    }

    public String printCommand(E engine) {
        ProcessBuilder builder = buildProcess(engine);
        return builder.command().toString();
    }

    public boolean hasResult() {
        return lastResult != null;
    }

    public ProcessResult getResult() {
        if(lastResult == null) {
            throw new NoSuchElementException();
        }
        return lastResult;
    }

    public boolean hasWarnMessage() {
        return lastWarnMessage != null;
    }

    public String getWarnMessage() {
        return lastWarnMessage;
    }

    public void setTerminalCharset(Charset terminalCharset) {
        this.terminalCharset = terminalCharset;
    }

    public Charset getTerminalCharset() {
        return terminalCharset;
    }

    protected Charset getTerminalCharsetOrDefault() {
        return terminalCharset == null
                ? Charset.defaultCharset()
                : terminalCharset;
    }

    protected boolean isOnlyWarning(String errMsg) {
        return false;
    }

    protected void resetExecute() {
        lastResult = null;
        lastWarnMessage = null;
    }

    @Override
    protected void execute0(E engine) throws IOException, InterruptedException, ScriptException {
        resetExecute();

        ProcessBuilder builder = buildProcess(engine);

        Process process = builder.start();
        ProcessResult result = ProcessResult.waitFor(process);
        lastResult = result;

        if(result.isError()) {
            Charset cs = getTerminalCharsetOrDefault();
            String msg = result.printErr(cs);
            if(isOnlyWarning(msg)) {
                lastWarnMessage = msg;
            } else {
                throw new ScriptException(msg);
            }
        }
    }
}
