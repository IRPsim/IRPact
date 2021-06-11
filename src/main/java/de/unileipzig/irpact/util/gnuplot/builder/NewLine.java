package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public final class NewLine implements Command {

    public static final NewLine INSTANCE = new NewLine();

    public NewLine() {
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append(settings.getNewLine());
    }
}
