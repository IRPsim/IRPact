package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Comment implements Command {

    protected String text;

    public Comment(String text) {
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
        target.append(settings.getComment());
        target.append(" ");
        target.append(text);
    }
}
