package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class SimpleCommand implements Command {

    protected String text;

    public SimpleCommand(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append(text);
    }
}
