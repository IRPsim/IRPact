package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public interface Command {

    void print(StringSettings settings, Appendable target) throws IOException;
}
