package de.unileipzig.irpact.util.R;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public interface RScript {

    void execute(R engine) throws IOException, InterruptedException, RScriptException;
}
