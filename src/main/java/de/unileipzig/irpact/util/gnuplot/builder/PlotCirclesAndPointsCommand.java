package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class PlotCirclesAndPointsCommand implements Command {

    protected String source;

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getSource());
        target.append(" u 1:2:3 w circles, ");
        target.append(getSource());
        target.append(" u 1:2 w points");
    }
}
