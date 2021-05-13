package de.unileipzig.irpact.util.R;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class ExistingRScript extends AbstractRScript {

    protected final Path rScriptPath;

    public ExistingRScript(Path rScriptPath) {
        this.rScriptPath = rScriptPath;
    }

    public Path getRScriptPath() {
        return rScriptPath;
    }

    @Override
    protected String printRScriptPath() {
        return "\"" + getRScriptPath() + "\"";
    }

    @Override
    public void execute(R engine) throws IOException {

    }
}
