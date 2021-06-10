package de.unileipzig.irpact.util.script;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public interface Script<E> {

    String print();

    void execute(E engine) throws IOException, InterruptedException, ScriptException;
}
